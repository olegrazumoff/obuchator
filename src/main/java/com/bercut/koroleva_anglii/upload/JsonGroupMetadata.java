package com.bercut.koroleva_anglii.upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonGroupMetadata {
    private Map<String, List<Question>> questionGroups = new HashMap<>();

    public Map<String, List<Question>> getQuestionGroups() {
        return questionGroups;
    }

    public void setQuestionGroups(Map<String, List<Question>> questionGroups) {
        this.questionGroups = questionGroups;
    }
}
