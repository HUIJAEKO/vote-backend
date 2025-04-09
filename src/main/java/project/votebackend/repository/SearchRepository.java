package project.votebackend.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import project.votebackend.elasticSearch.VoteDocument;

@Repository
public interface SearchRepository extends ElasticsearchRepository<VoteDocument, String> {
}
