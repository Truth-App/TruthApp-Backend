package com.tech.truthapp.question.repository;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tech.truthapp.model.question.Question;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;

@Repository
public class QuestionElasticRespository {

	@Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "question";
    
    
    public String saveQuestion(Question question) throws IOException {

    	question.setId(UUID.randomUUID().toString());
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(question.getId())
                .document(question)
        );
        if (response.result().name().equals("Created")) {
            return new StringBuilder("Document has been successfully created.").toString();
        } else if (response.result().name().equals("Updated")) {
            return new StringBuilder("Document has been successfully updated.").toString();
        }
        return new StringBuilder("Error while performing the operation.").toString();
    }
}
