package com.protonmail.slobodo.bd2.controllers;

import com.protonmail.slobodo.bd2.services.ElasticSearchIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MLController {

    @Autowired
    private ElasticSearchIndexer bulkService;

    @PostMapping("/ml/indexData")
    public void bulkInsertWithFakeData(){
        bulkService.indexData();
    }
}