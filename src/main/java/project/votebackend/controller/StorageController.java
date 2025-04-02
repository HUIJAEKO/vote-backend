package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.votebackend.dto.LoadMainPageVoteDto;
import project.votebackend.security.CustumUserDetails;
import project.votebackend.service.StorageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    //투표한 게시물 불러오기
    @GetMapping("/voted")
    public ResponseEntity<Page<LoadMainPageVoteDto>> getVotedPosts(
            @AuthenticationPrincipal CustumUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(storageService.getVotedPosts(userDetails.getId(), pageable));
    }


    //좋아요한 게시물 불러오기
    @GetMapping("/liked")
    public ResponseEntity<Page<LoadMainPageVoteDto>> getLikedPosts(
            @AuthenticationPrincipal CustumUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(storageService.getLikedPosts(userDetails.getId(), pageable));
    }


    //북마크한 게시물 불러오기
    @GetMapping("/bookmarked")
    public ResponseEntity<Page<LoadMainPageVoteDto>> getBookmarkedPosts(
            @AuthenticationPrincipal CustumUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(storageService.getBookmarkedPosts(userDetails.getId(), pageable));
    }
}
