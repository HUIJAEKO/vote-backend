package project.votebackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.*;
import project.votebackend.dto.CreateVoteRequest;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.elasticSearch.VoteDocument;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CategoryException;
import project.votebackend.exception.VoteException;
import project.votebackend.repository.*;
import project.votebackend.type.ErrorCode;
import project.votebackend.type.ReactionType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VoteSelectRepository voteSelectRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final VoteImageRepository voteImageRepository;


    //투표 생성
    @Transactional
    public Vote createVote(CreateVoteRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));

        Vote vote = Vote.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .finishTime(request.getFinishTime())
                .build();

        vote.setUser(user);
        vote.setCategory(category);


        // 옵션 추가
        Set<VoteOption> options = request.getOptions().stream().map(opt -> {
            VoteOption o = new VoteOption();
            o.setVote(vote);
            o.setOption(opt.getContent());
            o.setOptionImage(opt.getOptionImage());
            return o;
        }).collect(Collectors.toSet());

        vote.setOptions(options);

        // 이미지 추가
        if (request.getImageUrls() != null) {
            Set<VoteImage> images = request.getImageUrls().stream().map(url -> {
                VoteImage img = new VoteImage();
                img.setVote(vote);
                img.setImageUrl(url);
                return img;
            }).collect(Collectors.toSet());
            vote.setImages(images);
        }

        Vote savedVote = voteRepository.save(vote);

        //Elasticsearch에 저장
        try {
            VoteDocument doc = VoteDocument.fromEntity(savedVote);
            elasticsearchClient.index(i -> i
                    .index("votes")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        } catch (IOException e) {
            log.error("Elasticsearch 저장 실패", e);
        }

        return savedVote;
    }

    // 투표 재업로드
    @Transactional
    public Long reuploadVote(Long originalVoteId, LocalDateTime newFinishTime, String username) {
        Vote original = voteRepository.findById(originalVoteId)
                .orElseThrow(() -> new VoteException(ErrorCode.VOTE_NOT_FOUND));

        if (!original.getUser().getUsername().equals(username)) {
            throw new AuthException(ErrorCode.USER_NOT_MATCHED);
        }

        // 새로운 투표 객체 생성
        Vote newVote = new Vote();
        newVote.setTitle(original.getTitle());
        newVote.setContent(original.getContent());
        newVote.setCategory(original.getCategory());
        newVote.setUser(original.getUser());
        newVote.setFinishTime(newFinishTime);
        voteRepository.save(newVote);

        // 옵션 복사
        List<VoteOption> newOptions = original.getOptions().stream()
                .map(opt -> {
                    VoteOption newOpt = new VoteOption();
                    newOpt.setVote(newVote);
                    newOpt.setOption(opt.getOption());
                    newOpt.setOptionImage(opt.getOptionImage());
                    return newOpt;
                })
                .collect(Collectors.toList());
        voteOptionRepository.saveAll(newOptions);

        // 이미지 복사
        if (original.getImages() != null) {
            List<VoteImage> newImages = original.getImages().stream()
                    .map((VoteImage img) -> new VoteImage(img.getImageUrl(), newVote))
                    .collect(Collectors.toList());
            voteImageRepository.saveAll(newImages);
        }

        //Elasticsearch에 저장
        try {
            VoteDocument doc = VoteDocument.fromEntity(newVote);
            elasticsearchClient.index(i -> i
                    .index("votes")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        } catch (IOException e) {
            log.error("Elasticsearch 저장 실패", e);
        }

        return newVote.getVoteId();
    }

    // 투표 삭제
    @Transactional
    public void deleteVote(Long voteId, String username) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(ErrorCode.VOTE_NOT_FOUND));

        if (!vote.getUser().getUsername().equals(username)) {
            throw new AuthException(ErrorCode.USER_NOT_MATCHED);
        }

        voteRepository.delete(vote);

        // Elasticsearch에서도 삭제
        try {
            elasticsearchClient.delete(d -> d
                    .index("votes")
                    .id(String.valueOf(voteId))
            );
        } catch (IOException e) {
            log.error("Elasticsearch 삭제 실패", e);

        }
    }

    // 메인페이지 투표 불러오기 (자신이 작성한, 자신이 선택한 카테고리, 자신이 팔로우한 사람의 글)
    public Page<LoadVoteDto> getMainPageVotes(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        // 유저가 선택한 카테고리 ID 목록
        List<Long> categoryIds = user.getUserInterests().stream()
                .map(interest -> interest.getCategory().getCategoryId())
                .toList();

        // 투표 조회
        Page<Vote> votes = voteRepository.findMainPageVotes(userId, categoryIds, pageable);

        // DTO로 변환 (득표수, 선택 옵션)
        return votes.map(vote -> LoadVoteDto.fromEntity(vote, userId, voteSelectRepository));
    }

    //단일 투표 불러오기
    public LoadVoteDto getVoteById(Long voteId, Long userId) {
        Vote vote = voteRepository.findByIdWithUserAndOptions(voteId)
                .orElseThrow(() -> new VoteException(ErrorCode.VOTE_NOT_FOUND));
        return LoadVoteDto.fromEntity(vote, userId, voteSelectRepository);
    }

    //좋아요 상위 게시물
    public List<LoadVoteDto> getTopLikedVotes(String username, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, size);
        List<Vote> votes = voteRepository.findByReactionTypeOrderByLikeCountDesc(
                ReactionType.LIKE,
                pageable
        );

        return votes.stream()
                .map(vote -> LoadVoteDto.fromEntity(vote, user.getUserId(), voteSelectRepository))
                .collect(Collectors.toList());
    }

    //특정 카테고리의 글 조회
    public Page<LoadVoteDto> getVotesByCategorySortedByLike(Long categoryId, int page, int size, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        Page<Vote> votes = voteRepository.findByCategoryOrderByLikeCount(categoryId, pageable);

        return votes.map(vote -> LoadVoteDto.fromEntity(vote, user.getUserId(), voteSelectRepository));
    }

}
