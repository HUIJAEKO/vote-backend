package project.votebackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import project.votebackend.domain.Vote;
import project.votebackend.dto.LoadVoteDto;
import project.votebackend.repository.CommentRepository;
import project.votebackend.repository.ReactionRepository;
import project.votebackend.repository.VoteSelectRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VoteStatisticsUtil {

    private final VoteSelectRepository voteSelectRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    public Map<String, Object> collectVoteStatistics(Long userId, List<Long> voteIds) {
        Map<String, Object> result = new HashMap<>();

        Map<Long, Integer> optionVoteCountMap = voteSelectRepository.findOptionVoteCounts(voteIds).stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).intValue()
                ));

        Map<Long, Integer> commentCountMap = commentRepository.countParentCommentsByVoteIds(voteIds).stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).intValue()
                ));

        List<Object[]> reactionRows = reactionRepository.findReactionsByVoteIds(voteIds);

        Map<Long, Integer> likeCountMap = new HashMap<>();
        Map<Long, Boolean> isLikedMap = new HashMap<>();
        Map<Long, Boolean> isBookmarkedMap = new HashMap<>();

        for (Object[] row : reactionRows) {
            Long voteId = (Long) row[0];
            String reaction = row[1].toString();
            Long reactedUserId = (Long) row[2];

            if (reaction.equals("LIKE")) {
                likeCountMap.put(voteId, likeCountMap.getOrDefault(voteId, 0) + 1);
                if (reactedUserId.equals(userId)) {
                    isLikedMap.put(voteId, true);
                }
            }

            if (reaction.equals("BOOKMARK") && reactedUserId.equals(userId)) {
                isBookmarkedMap.put(voteId, true);
            }
        }

        result.put("optionVoteCountMap", optionVoteCountMap);
        result.put("commentCountMap", commentCountMap);
        result.put("likeCountMap", likeCountMap);
        result.put("isLikedMap", isLikedMap);
        result.put("isBookmarkedMap", isBookmarkedMap);

        return result;
    }

    public Page<LoadVoteDto> getLoadVoteDtos(Long userId, Page<Vote> votes, Map<String, Object> stats, Pageable pageable) {
        List<LoadVoteDto> dtos = votes.getContent().stream()
                .map(v -> LoadVoteDto.fromEntityWithAllMaps(
                        v, userId, voteSelectRepository,
                        (Map<Long, Integer>) stats.get("optionVoteCountMap"),
                        (Map<Long, Integer>) stats.get("commentCountMap"),
                        (Map<Long, Integer>) stats.get("likeCountMap"),
                        (Map<Long, Boolean>) stats.get("isLikedMap"),
                        (Map<Long, Boolean>) stats.get("isBookmarkedMap")
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, votes.getTotalElements());
    }
}
