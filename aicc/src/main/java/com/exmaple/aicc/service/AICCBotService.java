package com.exmaple.aicc.service;

import com.exmaple.aicc.bots.AICCBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AICCBotService {

    private final TelegramBotsApi telegramBotsApi;
    private final Map<String, AICCBot> aiccBots = new HashMap<>();

    public void createAICCBot(String botName, String botToken) {
        AICCBot aiccBot = new AICCBot(botName, botToken);
        aiccBots.put(botName, aiccBot);
        try {
            telegramBotsApi.registerBot(aiccBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void trainAICCBot(String botName, String trainingData) {
        AICCBot aiccBot = aiccBots.get(botName);
        if (aiccBot != null) {
            aiccBot.train(trainingData);
        }
    }

    public AICCBot getAICCBot(String botName) {
        return aiccBots.get(botName);
    }
}
