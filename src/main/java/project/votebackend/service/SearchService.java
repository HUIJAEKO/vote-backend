package project.votebackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.elasticSearch.VoteDocument;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    //투표 검색
    public List<VoteDocument> searchVotes(String keyword) throws IOException {
        SearchResponse<VoteDocument> response = elasticsearchClient.search(s -> s
                        .index("votes")
                        .query(q -> q
                                .bool(b -> b
                                        .should(QueryBuilders.match(m -> m.field("title").query(keyword)))
                                        .should(QueryBuilders.match(m -> m.field("username").query(keyword)))
                                        .should(QueryBuilders.term(t -> t.field("category.keyword").value(keyword)))
                                )
                        ),
                VoteDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
