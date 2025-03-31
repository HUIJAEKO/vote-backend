package project.votebackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String content;
    private LocalDateTime finishTime;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteSelection> selections = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}
