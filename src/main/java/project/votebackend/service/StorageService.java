package project.votebackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.votebackend.domain.Vote;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.repository.CommentRepository;
import project.votebackend.repository.ReactionRepository;
import project.votebackend.repository.VoteRepository;
import project.votebackend.repository.VoteSelectRepository;
import project.votebackend.util.VoteStatisticsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final VoteRepository voteRepository;
    private final VoteStatisticsUtil voteStatisticsUtil;


    //내가 투표한 게시물
    public Page<LoadVoteDto> getVotedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findVotedByUserId(userId, pageable);
        List<Long> voteIds = votes.getContent().stream().map(Vote::getVoteId).toList();
        Map<String, Object> stats = voteStatisticsUtil.collectVoteStatistics(userId, voteIds);
        return voteStatisticsUtil.getLoadVoteDtos(userId, votes, stats, pageable);
    }

    //좋아요한 게시물
    public Page<LoadVoteDto> getLikedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findLikedVotes(userId, pageable);
        List<Long> voteIds = votes.getContent().stream().map(Vote::getVoteId).toList();
        Map<String, Object> stats = voteStatisticsUtil.collectVoteStatistics(userId, voteIds);
        return voteStatisticsUtil.getLoadVoteDtos(userId, votes, stats, pageable);
    }

    //북마크한 게시물
    public Page<LoadVoteDto> getBookmarkedPosts(Long userId, Pageable pageable) {
        Page<Vote> votes = voteRepository.findBookmarkedVotes(userId, pageable);
        List<Long> voteIds = votes.getContent().stream().map(Vote::getVoteId).toList();
        Map<String, Object> stats = voteStatisticsUtil.collectVoteStatistics(userId, voteIds);
        return voteStatisticsUtil.getLoadVoteDtos(userId, votes, stats, pageable);
    }
}
