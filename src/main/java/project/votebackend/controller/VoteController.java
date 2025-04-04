package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.votebackend.domain.Vote;
import project.votebackend.dto.*;
import project.votebackend.security.CustumUserDetails;
import project.votebackend.service.VoteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    //투표 생성
    @PostMapping("/create")
    public ResponseEntity<?> createVote(
            @RequestBody CreateVoteRequest request,
            @AuthenticationPrincipal CustumUserDetails userDetails
    ) {
        Vote created = voteService.createVote(request, userDetails.getId());
        CreateVoteResponse response = new CreateVoteResponse("success", created.getVoteId());
        return ResponseEntity.ok(response);
    }

    //메인페이지 투표 불러오기 (자신이 작성한, 자신이 선택한 카테고리의 글)
    @GetMapping("/load-main-page-votes")
    public ResponseEntity<Page<LoadVoteDto>> loadMainPageVotes(
            @AuthenticationPrincipal CustumUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LoadVoteDto> vote = voteService.getMainPageVotes(userDetails.getId(), pageable);
        return ResponseEntity.ok(vote);
    }

    //단일 투표 불러오기
    @GetMapping("/{voteId}")
    public ResponseEntity<LoadVoteDto> getVoteById(
            @PathVariable Long voteId,
            @AuthenticationPrincipal CustumUserDetails userDetails
    ) {
        LoadVoteDto voteDto = voteService.getVoteById(voteId, userDetails.getId());
        return ResponseEntity.ok(voteDto);
    }
}
