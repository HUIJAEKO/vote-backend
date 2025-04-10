package project.votebackend.elasticSearch;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import project.votebackend.domain.User;

@Document(indexName = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocument {
    private Long id;
    private String username;
    private String profileImage;

    public static UserDocument fromEntity(User user) {
        return new UserDocument(
                user.getUserId(),
                user.getUsername(),
                user.getProfileImage()
        );
    }
}
