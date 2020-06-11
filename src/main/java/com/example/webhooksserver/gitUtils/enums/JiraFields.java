package com.example.webhooksserver.gitUtils.enums;

public enum JiraFields {

    KEY("key"), PARENT("parent"), DUE_DATE("duedate");

    private String field;

    public String getField() {
        return field;
    }

    private JiraFields(String field) {
        this.field = field;
    }

}