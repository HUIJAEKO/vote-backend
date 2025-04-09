package project.votebackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.domain.Vote;
import project.votebackend.elasticSearch.VoteDocument;
import project.votebackend.repository.VoteRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationService {
    private final VoteRepository voteRepository;
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
}
