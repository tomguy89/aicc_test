package com.exmaple.aicc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

    private final RetrievalService retrievalService;
    private final OpenAIService openAiService;

    public String generateResponse(String query) {
        // Retrieve relevant information
        String retrievedInfo = retrievalService.retrieve(query);

        // Generate response using OpenAI API
        String prompt = "Based on the following information: " + retrievedInfo + "\nQ: " + query + "\nA:";
        return openAiService.getCompletion(prompt);
    }
}
