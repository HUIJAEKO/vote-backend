package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.*;
import project.votebackend.dto.CreateVoteRequest;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.dto.VoteUpdateRequest;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CategoryException;
import project.votebackend.exception.VoteException;
import project.votebackend.repository.*;
import project.votebackend.type.ErrorCode;
import project.votebackend.type.ReactionType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VoteSelectRepository voteSelectRepository;
    private final VoteOptionRepository voteOptionRepository;

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
            o.setOption(opt);
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

        return voteRepository.save(vote);
    }

    // 투표 수정
    @Transactional
    public void updateVote(Long voteId, VoteUpdateRequest request, String username) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(ErrorCode.VOTE_NOT_FOUND));

        if (!vote.getUser().getUsername().equals(username)) {
            throw new AuthException(ErrorCode.USER_NOT_MATCHED);
        }

        vote.setTitle(request.getTitle());
        vote.setContent(request.getContent());
        vote.setFinishTime(request.getFinishTime());

        // 카테고리 설정
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
        vote.setCategory(category);

        // 자식 테이블 먼저 삭제
        voteSelectRepository.deleteByOption_Vote_VoteId(voteId);

        // 기존 옵션 삭제 후 새 옵션 저장
        voteOptionRepository.deleteByVote_VoteId(voteId);
        List<VoteOption> newOptions = request.getOptions().stream()
                .map(option -> new VoteOption(option, vote))
                .collect(Collectors.toList());
        voteOptionRepository.saveAll(newOptions);
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
    }

    // 메인페이지 투표 불러오기 (자신이 작성한, 자신이 선택한 카테고리의 글)
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

    //상위 10개의 게시물
    public List<LoadVoteDto> getTop10LikedVotes(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, 10); // 상위 10개
        List<Vote> votes = voteRepository.findTop10ByReactionTypeOrderByLikeCountDesc(
                ReactionType.LIKE,
                pageable
        );

        return votes.stream()
                .map(vote -> LoadVoteDto.fromEntity(vote, user.getUserId(), voteSelectRepository))
                .collect(Collectors.toList());
    }
}
