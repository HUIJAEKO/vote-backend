package project.votebackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vote_image")
@Getter
@Setter
public class VoteImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteImageId;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    private String imageUrl;
}
