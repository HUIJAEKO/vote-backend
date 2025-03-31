package project.votebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote_selections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteSelection extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selectionsId;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private VoteOption option;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
