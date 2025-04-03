package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.votebackend.domain.Vote;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.repository.VoteRepository;
import project.votebackend.repository.VoteSelectRepository;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final VoteRepository voteRepository;
    private final VoteSelectRepository voteSelectRepository;

    //내가 투표한 게시물
    public Page<LoadVoteDto> getVotedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findVotedByUserId(userId, pageable);
        return votes.map(v -> LoadVoteDto.fromEntity(v, userId, voteSelectRepository));
    }

    //좋아요한 게시물
    public Page<LoadVoteDto> getLikedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findLikedVotes(userId, pageable);
        return votes.map(v -> LoadVoteDto.fromEntity(v, userId, voteSelectRepository));
    }

    //북마크한 게시물
    public Page<LoadVoteDto> getBookmarkedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findBookmarkedVotes(userId, pageable);
        return votes.map(v -> LoadVoteDto.fromEntity(v, userId, voteSelectRepository));
    }
}
