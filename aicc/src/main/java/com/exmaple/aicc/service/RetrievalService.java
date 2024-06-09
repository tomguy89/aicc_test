package com.exmaple.aicc.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RetrievalService {

    private final Map<String, String> knowledgeBase = new HashMap<>();

    public RetrievalService() {
        // Initialize knowledge base
        knowledgeBase.put("What is AI?", "Artificial Intelligence (AI) is the simulation of human intelligence in machines.");
        // Add more knowledge pairs here
    }

    public String retrieve(String query) {
        // Simple retrieval logic
        return knowledgeBase.getOrDefault(query, "No information available.");
    }
}
