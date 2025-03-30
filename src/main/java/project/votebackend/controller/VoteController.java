package project.votebackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.votebackend.domain.Vote;
import project.votebackend.dto.CreateVoteRequest;
import project.votebackend.dto.CreateVoteResponse;
import project.votebackend.security.CustumUserDetails;
import project.votebackend.service.VoteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/create")
    public ResponseEntity<?> createVote(
            @RequestBody CreateVoteRequest request,
            @AuthenticationPrincipal CustumUserDetails userDetails
    ) {
        Vote created = voteService.createVote(request, userDetails.getId());
        CreateVoteResponse response = new CreateVoteResponse("success", created.getVoteId());
        return ResponseEntity.ok(response);
    }
}
