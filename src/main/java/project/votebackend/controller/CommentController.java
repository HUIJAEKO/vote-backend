package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.votebackend.domain.Comment;
import project.votebackend.dto.CommentRequest;
import project.votebackend.dto.CommentResponse;
import project.votebackend.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/{voteId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long voteId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Comment comment = commentService.createComment(
                voteId,
                request.getContent(),
                userDetails.getUsername(),
                request.getParentId()
        );

        return ResponseEntity.ok(new CommentResponse(comment));
    }

    //댓글 조회
    @GetMapping("/{voteId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long voteId,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<CommentResponse> comments = commentService.getComments(voteId);
        return ResponseEntity.ok(comments);
    }
}
