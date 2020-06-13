package com.example.webhooksserver.enums;

import java.util.HashMap;
import java.util.Map;

public enum PullRequestAction {
    CLOSED("closed"), OPENED("opened"), SYNCHRONIZE("synchronize");

    private String action;

    public String getAction() {
        return action;
    }

    private PullRequestAction(String action) {
        this.action = action;
    }

    private static final Map<String, PullRequestAction> BY_ACTION = new HashMap<>();

    static {
        for (PullRequestAction a : values()) {
            BY_ACTION.put(a.action, a);
        }
    }

    public static PullRequestAction valueOfAction(String event) {
        return BY_ACTION.get(event);
    }
}