package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.domain.Follow;
import project.votebackend.exception.FollowException;
import project.votebackend.repository.FollowRepository;
import project.votebackend.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    //팔로우
    public Long follow(Long followerId, Long followingId) {
        followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .ifPresent(f -> {
                    try {
                        throw new FollowException(ErrorCode.ALREADY_FOLLOW);
                    } catch (FollowException e) {
                        throw new RuntimeException(e);
                    }
                });

        Follow follow = Follow.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        followRepository.save(follow);
        return follow.getFollowId();
    }

    //언팔로우
    public void unfollow(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }
}
