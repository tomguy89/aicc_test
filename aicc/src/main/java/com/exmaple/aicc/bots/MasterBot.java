package com.exmaple.aicc.bots;

import com.exmaple.aicc.model.UserState;
import com.exmaple.aicc.service.AICCBotService;
import com.exmaple.aicc.service.UserStateService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class MasterBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;
    private final AICCBotService aiccBotService;
    private final UserStateService userStateService;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            UserState userState = userStateService.getUserState(chatId);

            switch (userState.getCurrentState()) {
                case "IDLE":
                    handleIdleState(chatId, messageText, userState);
                    break;
                case "WAITING_FOR_REGISTER_BOT_NAME":
                    handleWaitingForBotNameState(chatId, messageText, userState);
                    break;
                case "WAITING_FOR_REGISTER_BOT_TOKEN":
                    handleWaitingForBotNameState(chatId, messageText, userState);
                    break;
                case "WAITING_FOR_TRAIN_BOT_NAME":
                    handleWaitingForBotNameState(chatId, messageText, userState);
                    break;
                case "WAITING_FOR_TRAIN_DATA":
                    handleWaitingForTrainingDataState(chatId, messageText, userState);
                    break;
                default:
                    sendMessage(chatId, "오류가 발생했습니다. 잠시 후 다시 시도하세요.");
                    userState.setCurrentState("IDLE");
                    userStateService.setUserState(chatId, userState);
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIdleState(Long chatId, String messageText, UserState userState) {
        if (messageText.startsWith("/create")) {
            sendMessage(chatId, "BotFather에서 /newbot으로 봇을 생성한 뒤, /register <botName> <botToken> 명령어를 수행하세요.");
        } else if (messageText.startsWith("/register ")) {
            sendMessage(chatId, "등록할 봇 이름을 입력하세요.");
            userState.setCurrentState("WAITING_FOR_REGISTER_BOT_NAME");
            userStateService.setUserState(chatId, userState);
        } else if (messageText.startsWith("/train")) {
            sendMessage(chatId, "학습시킬 봇 이름을 입력하세요.");
            userState.setCurrentState("WAITING_FOR_TRAIN_BOT_NAME");
            userStateService.setUserState(chatId, userState);
        } else {
            sendMessage(chatId, "잘못된 명령어입니다.");
        }
    }

    private void handleWaitingForRegisterBotNameState(Long chatId, String messageText, UserState userState) {
        userState.setBotName(messageText.trim());
        sendMessage(chatId, "토큰을 입력하세요.");
        userState.setCurrentState("WAITING_FOR_REGISTER_BOT_TOKEN");
        userStateService.setUserState(chatId, userState);
    }

    private void handleWaitingForRegisterBotTokenState(Long chatId, String messageText, UserState userState) {
        String botName = userState.getBotName();
        aiccBotService.createAICCBot(botName, messageText.trim());
        sendMessage(chatId, "AICC Bot [" + botName + "] 가 정상적으로 등록되었습니다.");
        userState.setCurrentState("IDLE");
        userStateService.setUserState(chatId, userState);
    }

    private void handleWaitingForBotNameState(Long chatId, String messageText, UserState userState) {
        AICCBot bot = aiccBotService.getAICCBot(messageText.trim());
        if (bot == null) {
            sendMessage(chatId, "봇이 존재하지 않습니다.");
            userState.setCurrentState("IDLE");
            userStateService.setUserState(chatId, userState);
        } else {
            userState.setBotName(bot.getBotUsername());
            sendMessage(chatId, "학습시킬 데이터를 입력하세요.");
            userState.setCurrentState("WAITING_FOR_TRAIN_DATA");
            userStateService.setUserState(chatId, userState);
        }
    }

    private void handleWaitingForTrainingDataState(Long chatId, String messageText, UserState userState) {
        String botName = userState.getBotName();
        String trainingData = messageText.trim();
        aiccBotService.trainAICCBot(botName, trainingData);
        sendMessage(chatId, "제공된 데이터로 AICC Bot [" + botName + "]를 학습시켰습니다.");
        userState.setCurrentState("IDLE");
        userStateService.setUserState(chatId, userState);
    }
}
