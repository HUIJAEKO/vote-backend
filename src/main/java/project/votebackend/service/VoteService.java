package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.votebackend.domain.*;
import project.votebackend.dto.CreateVoteRequest;
import project.votebackend.dto.LoadMainPageVoteDto;
import project.votebackend.exception.AuthException;
import project.votebackend.exception.CategoryException;
import project.votebackend.repository.CategoryRepository;
import project.votebackend.repository.UserRepository;
import project.votebackend.repository.VoteRepository;
import project.votebackend.type.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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
        List<VoteOption> options = request.getOptions().stream().map(opt -> {
            VoteOption o = new VoteOption();
            o.setVote(vote);
            o.setOption(opt);
            return o;
        }).collect(Collectors.toList());

        vote.setOptions(options);

        // 이미지 추가
        if (request.getImageUrls() != null) {
            List<VoteImage> images = request.getImageUrls().stream().map(url -> {
                VoteImage img = new VoteImage();
                img.setVote(vote);
                img.setImageUrl(url);
                return img;
            }).collect(Collectors.toList());
            vote.setImages(images);
        }

        return voteRepository.save(vote);
    }

    // 메인페이지 투표 불러오기 (자신이 작성한, 자신이 선택한 카테고리의 글)
    public Page<LoadMainPageVoteDto> getMainPageVotes(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        List<Long> categoryIds = user.getUserInterests()
                .stream()
                .map(userInterest -> userInterest.getCategory().getCategoryId())
                .toList();

        Page<Vote> votes = voteRepository.findMainPageVotes(user.getUserId(), categoryIds, pageable);
        return votes.map(vote -> LoadMainPageVoteDto.fromEntity(vote, userId));
    }
}
