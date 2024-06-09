package com.exmaple.aicc.model;

public class UserState {

    private String currentState;
    private String botName;

    public UserState() {
        this.currentState = "IDLE";
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }
}