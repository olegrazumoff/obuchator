package com.bercut.koroleva_anglii.progress;

import com.bercut.koroleva_anglii.model.Model;
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
        if(message.equalsIgnoreCase("reset")) {
            progress = new Progress(model);
            progressMap.put(userId, progress);
        }
        if (progress == null) {
            progress = new Progress(model);
            progressMap.put(userId, progress);
        }
        return progress.handleMessage(message);
    }

}
