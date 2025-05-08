package project.votebackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.votebackend.elasticSearch.UserDocument;
import project.votebackend.elasticSearch.VoteDocument;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    // 투표 검색
    public List<VoteDocument> searchVotes(String keyword) throws IOException {
        SearchResponse<VoteDocument> response = elasticsearchClient.search(s -> s
                        .index("votes")
                        .size(20)
                        .query(q -> q.bool(b -> b
                                .should(m -> m.match(mm -> mm
                                        .field("title.ngram")      // ngram 매칭
                                        .query(keyword)
                                        .boost(4.0f)))
                                .should(m -> m.matchPhrase(mp -> mp
                                        .field("title.standard")   // 정확한 문장
                                        .query(keyword)))
                                .should(m -> m.match(mm -> mm
                                        .field("title")
                                        .query(keyword)
                                        .fuzziness("AUTO")
                                        .boost(2.0f)))              // 오타 대응
                                .should(m -> m.match(mm -> mm
                                        .field("username")
                                        .query(keyword)
                                        .fuzziness("AUTO")))
                                .should(m -> m.term(t -> t
                                        .field("category.keyword")
                                        .value(keyword)))
                        )),
                VoteDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    // 유저 검색
    public List<UserDocument> searchUsers(String keyword) throws IOException {
        SearchResponse<UserDocument> response = elasticsearchClient.search(s -> s
                        .index("users")
                        .size(50)
                        .query(q -> q.bool(b -> b
                                .should(QueryBuilders.match(m -> m
                                        .field("username.edge")
                                        .query(keyword)
                                        .boost(3.0f)))
                                .should(QueryBuilders.match(m -> m
                                        .field("username.ngram")
                                        .query(keyword)
                                        .boost(2.0f)))
                                .should(QueryBuilders.match(m -> m
                                        .field("username.standard")
                                        .query(keyword)
                                        .fuzziness("AUTO")
                                        .boost(1.0f)))
                        )),
                UserDocument.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
