package project.votebackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "connent_id")
    private Comment comment;

    private String content;
}
