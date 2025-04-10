package project.votebackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.domain.User;
import project.votebackend.domain.Vote;
import project.votebackend.elasticSearch.UserDocument;
import project.votebackend.elasticSearch.VoteDocument;
import project.votebackend.repository.UserRepository;
import project.votebackend.repository.VoteRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ElasticsearchClient elasticsearchClient;

    //투표 마이그레이션
    public void migrateVotesToElasticsearch() throws IOException {
        List<Vote> allVotes = voteRepository.findAll();

        for (Vote vote : allVotes) {
            VoteDocument doc = VoteDocument.fromEntity(vote);
            elasticsearchClient.index(i -> i
                    .index("votes")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        }
    }

    //유저 마이그레이션
    public void migrateUsersToElasticsearch() throws IOException {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            UserDocument doc = UserDocument.fromEntity(user);
            elasticsearchClient.index(i -> i
                    .index("users")
                    .id(String.valueOf(doc.getId()))
                    .document(doc)
            );
        }
    }
}
