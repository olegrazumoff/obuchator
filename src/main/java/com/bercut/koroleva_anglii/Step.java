package com.bercut.koroleva_anglii;

import java.util.List;

public class Step {
    private String message;
    private List<AnswerHandler> answerHandlers;
    private Block block;

    public Step(String message, Block block) {
        this.message = message;
        this.answerHandlers = answerHandlers;
        this.block = block;
    }

    public void setAnswerHandlers(List<AnswerHandler> answerHandlers) {
        this.answerHandlers = answerHandlers;
    }

    public String getMessage() {
        return message;
    }

    public List<AnswerHandler> getAnswerHandlers() {
        return answerHandlers;
    }

    public Block getBlock() {
        return block;
    }
}
