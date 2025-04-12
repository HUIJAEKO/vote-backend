package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.votebackend.service.FollowService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping
    public ResponseEntity<?> follow(@RequestBody Map<String, Long> body) {
        Long followerId = body.get("followerId");
        Long followingId = body.get("followingId");

        Long followId = followService.follow(followerId, followingId);
        return ResponseEntity.ok(Map.of("followId", followId, "message", "팔로우 성공"));
    }

    // 언팔로우
    @DeleteMapping
    public ResponseEntity<?> unfollow(@RequestBody Map<String, Long> body) {
        Long followerId = body.get("followerId");
        Long followingId = body.get("followingId");

        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok(Map.of("message", "팔로우 취소 완료"));
    }
}
