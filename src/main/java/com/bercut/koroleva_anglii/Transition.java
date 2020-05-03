package com.bercut.koroleva_anglii;

public class Transition {
    private Block block;
    private HandlerCallback callback;

    public Transition(Block block, HandlerCallback callback) {
        this.block = block;
        this.callback = callback;
    }

    public boolean isConditionSuccess(String message) {
        return callback.handle(message);
    }

    public Block getBlock() {
        return block;
    }
}
