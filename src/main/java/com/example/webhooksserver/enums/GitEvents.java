package com.example.webhooksserver.enums;

import java.util.HashMap;
import java.util.Map;

public enum GitEvents {
    PUSH("push"), PULL_REQUEST("pull_request");

    private String event;

    public String getEvent() {
        return event;
    }

    private GitEvents(String event) {
        this.event = event;
    }

    private static final Map<String, GitEvents> BY_EVENT = new HashMap<>();

    static {
        for (GitEvents e : values()) {
            BY_EVENT.put(e.event, e);
        }
    }

    public static GitEvents valueOfEvent(String event) {
        return BY_EVENT.get(event);
    }
}