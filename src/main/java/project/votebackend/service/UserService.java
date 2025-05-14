package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.dto.UserPageDto;
import project.votebackend.exception.AuthException;
import project.votebackend.repository.FollowRepository;
import project.votebackend.repository.UserRepository;
import project.votebackend.repository.VoteRepository;
import project.votebackend.repository.VoteSelectRepository;
import project.votebackend.type.ErrorCode;
import project.votebackend.util.VoteStatisticsUtil;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final FollowRepository followRepository;
    private final VoteStatisticsUtil voteStatisticsUtil;

    //마이페이지 조회
    public UserPageDto getMyPage(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Vote> votes = voteRepository.findByUser_UserId(userId, sortedPageable);

        List<Long> voteIds = votes.getContent().stream()
                .map(Vote::getVoteId)
                .toList();

        Map<String, Object> stats = voteStatisticsUtil.collectVoteStatistics(userId, voteIds);
        Page<LoadVoteDto> voteDto = voteStatisticsUtil.getLoadVoteDtos(userId, votes, stats, sortedPageable);

        Long postCount = voteRepository.countByUser_UserId(userId);
        Long followerCount = followRepository.countByFollowingId(userId);
        Long followingCount = followRepository.countByFollowerId(userId);

        // 6. DTO 조립
        return UserPageDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .introduction(user.getIntroduction())
                .point(user.getPoint())
                .posts(voteDto)
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }

    //다른 유저의 프로필 조회
    public UserPageDto getUserPage(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.USERNAME_NOT_FOUND));

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Vote> votes = voteRepository.findByUser_UserId(userId, sortedPageable);
        List<Long> voteIds = votes.getContent().stream()
                .map(Vote::getVoteId)
                .toList();

        Map<String, Object> stats = voteStatisticsUtil.collectVoteStatistics(userId, voteIds);
        Page<LoadVoteDto> voteDto = voteStatisticsUtil.getLoadVoteDtos(userId, votes, stats, sortedPageable);

        Long postCount = voteRepository.countByUser_UserId(userId);
        Long followerCount = followRepository.countByFollowingId(userId);
        Long followingCount = followRepository.countByFollowerId(userId);

        return UserPageDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .introduction(user.getIntroduction())
                .point(user.getPoint())
                .posts(voteDto)
                .postCount(postCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
    }
}
