package com.bercut.koroleva_anglii.model;

import java.util.List;

public interface TransitionCallback {
    boolean handle(Group group, List<Answer> message);
    boolean handle(String message);
}
