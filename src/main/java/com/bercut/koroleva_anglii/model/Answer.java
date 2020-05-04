package com.bercut.koroleva_anglii.model;

public class Answer {
    private AnswerType answerType;
    private HandlerCallback callback;

    public Answer(AnswerType answerType, HandlerCallback callback) {
        this.callback = callback;
        this.answerType = answerType;
    }

    public boolean handle(String message) {
        return callback.handle(message);
    }

    public AnswerType getAnswerType() {
        return answerType;
    }
}
