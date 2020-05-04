package com.bercut.koroleva_anglii.upload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonGroupMetadata {
    private Map<String, List<JsonQuestion>> questionGroups = new HashMap<>();

    public Map<String, List<JsonQuestion>> getQuestionGroups() {
        return questionGroups;
    }

    public void setQuestionGroups(Map<String, List<JsonQuestion>> questionGroups) {
        this.questionGroups = questionGroups;
    }
}
