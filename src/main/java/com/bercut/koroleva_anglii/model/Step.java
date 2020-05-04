package com.bercut.koroleva_anglii.model;

import java.util.List;

public class Step {
    private String message;
    private List<Answer> answers;

    public Step(String message) {
        this.message = message;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getMessage() {
        return message;
    }

    public Answer getAnswer(String message) {
        for (Answer answer : answers) {
            if (answer.handle(message)) {
                return answer;
            }
        }
        return null;
    }
}
