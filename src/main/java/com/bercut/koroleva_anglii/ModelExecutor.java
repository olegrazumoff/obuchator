package com.bercut.koroleva_anglii;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModelExecutor {
    private Model model;

    private Map<String, Progress> progressMap = new HashMap<>();

    public void setModel(Model model) {
        this.model = model;
    }

    public String handleMessage(String userId, String message) {
        Progress progress = progressMap.get(userId);
        if (progress == null) {
            progress = new Progress(model);
            progressMap.put(userId, progress);
        }
        return progress.handleMessage(message);
    }

}
