package com.exmaple.aicc.service;

import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Service
public class OpenAIService {

    private final OpenAiApi api;

    public OpenAIService(@Value("${openai.api.key}") String apiKey) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                        .header("Authorization", "Bearer " + apiKey)
                        .build()))
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/v1/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.api = retrofit.create(OpenAiApi.class);
    }

    public String getCompletion(String prompt) {
        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-4") // 최신 모델 사용
                .prompt(prompt)
                .maxTokens(100)
                .build();

        try {
            CompletionResult result = api.createCompletion(request).blockingGet();
            if (result != null && !result.getChoices().isEmpty()) {
                return result.getChoices().get(0).getText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error generating response from OpenAI.";
    }
}
