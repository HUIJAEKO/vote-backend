package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.votebackend.domain.Vote;
import project.votebackend.dto.LoadMainPageVoteDto;
import project.votebackend.repository.VoteRepository;
import project.votebackend.repository.VoteSelectRepository;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final VoteRepository voteRepository;
    private final VoteSelectRepository voteSelectRepository;

    //내가 투표한 게시물
    public Page<LoadMainPageVoteDto> getVotedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findVotedByUserId(userId, pageable);
        return votes.map(v -> LoadMainPageVoteDto.fromEntity(v, userId, voteSelectRepository));
    }

    //좋아요한 게시물
    public Page<LoadMainPageVoteDto> getLikedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findLikedVotes(userId, pageable);
        return votes.map(v -> LoadMainPageVoteDto.fromEntity(v, userId, voteSelectRepository));
    }

    //북마크한 게시물
    public Page<LoadMainPageVoteDto> getBookmarkedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findBookmarkedVotes(userId, pageable);
        return votes.map(v -> LoadMainPageVoteDto.fromEntity(v, userId, voteSelectRepository));
    }
}
