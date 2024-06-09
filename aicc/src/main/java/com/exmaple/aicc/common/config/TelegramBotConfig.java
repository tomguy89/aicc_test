package com.exmaple.aicc.common.config;

import com.exmaple.aicc.bots.MasterBot;
import com.exmaple.aicc.service.AICCBotService;
import com.exmaple.aicc.service.UserStateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
public class TelegramBotConfig {

    @Value("${telegram.master.bot.token}")
    private String masterBotToken;

    @Value("${telegram.master.bot.name}")
    private String masterBotUsername;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws Exception {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public AICCBotService aiccBotService(TelegramBotsApi telegramBotsApi) {
        return new AICCBotService(telegramBotsApi);
    }

    @Bean
    public MasterBot masterBot(AICCBotService aiccBotService, UserStateService userStateService) {
        return new MasterBot(masterBotToken, masterBotUsername, aiccBotService, userStateService);
    }
}