package project.votebackend.elasticSearch;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import project.votebackend.domain.Vote;

@Document(indexName = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDocument {
    private Long id;
    private String title;
    private String username;
    private String category;

    public static VoteDocument fromEntity(Vote vote) {
        return new VoteDocument(
                vote.getVoteId(),
                vote.getTitle(),
                vote.getUser().getUsername(),
                vote.getCategory().getName()
        );
    }
}