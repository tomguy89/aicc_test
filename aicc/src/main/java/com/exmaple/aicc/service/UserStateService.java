package com.exmaple.aicc.service;

import com.exmaple.aicc.model.UserState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserStateService {

    private final Map<Long, UserState> userStates = new HashMap<>();

    public UserState getUserState(Long chatId) {
        return userStates.computeIfAbsent(chatId, k -> new UserState());
    }

    public void setUserState(Long chatId, UserState userState) {
        userStates.put(chatId, userState);
    }
}