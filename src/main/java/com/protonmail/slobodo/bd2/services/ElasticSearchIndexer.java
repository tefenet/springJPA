package com.protonmail.slobodo.bd2.services;

import com.protonmail.slobodo.bd2.elastic_repositories.UserESRepository;
import com.protonmail.slobodo.bd2.model.User;
import com.protonmail.slobodo.bd2.repositories.dao.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticSearchIndexer {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    UserESRepository userESRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void indexData(){
        System.out.println("start data indexing**********");
        bulkInsert(getData());
        System.out.printf("Loading Completed");
    }

    private Iterable<User> getData() {
        return userRepository.findAll();
    }

    public void bulkInsert(Iterable<User> userList){
        var bulkRequest = new BulkRequest();
        userList.forEach(user -> {
            Map<String, Object> jsonMap = new HashMap<>();
            var indexRequest = new IndexRequest("users").id(user.getId().toString()).
                    source(jsonMap.put("data",user));
            bulkRequest.add(indexRequest);
        });
        try {
            highLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
