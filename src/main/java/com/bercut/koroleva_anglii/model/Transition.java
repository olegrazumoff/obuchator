package com.bercut.koroleva_anglii.model;

import java.util.List;

public class Transition {
    private Node block;
    private TransitionCallback callback;
    private String message;

    public Transition(Node block, TransitionCallback callback) {
        this.block = block;
        this.callback = callback;
        //this.message = message;
    }

    public Node getBlock() {
        return block;
    }

    public boolean handle(Group group, List<Answer> currentAnswers) {
        return callback.handle(group, currentAnswers);
    }

    public String getMessage() {
        return block.getWelcomeMessage();
    }
}
