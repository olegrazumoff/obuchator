package com.bercut.koroleva_anglii;

public class AnswerHandler {
    private Step step;
    private AnswerType answerType;
    private HandlerCallback callback;

    public AnswerHandler(Step step, AnswerType answerType, HandlerCallback callback) {
        this.step = step;
        this.callback = callback;
    }

    public Step getStep() {
        return step;
    }

    public boolean handle(String message) {
        //TODO logic from model here
        return false;
    }
}
