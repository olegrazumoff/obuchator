package com.bercut.koroleva_anglii.model;

public enum BlockType {
    INFO("continueBlock"), RANDOM("вопросы_в_случайном_порядке"), SEQUENTAL("вопросы_по_порядку");

    private String name;

    BlockType(String name) {
        this.name = name;
    }

    public static BlockType getByName(String name) {
        for (BlockType stepChooser : values()) {
            if (stepChooser.name.equalsIgnoreCase(name)) {
                return stepChooser;
            }
        }
        return null;
    }
}
