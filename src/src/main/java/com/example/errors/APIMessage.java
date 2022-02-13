package com.example.errors;

//Все возможные сообщения
public class APIMessage {
    public static final String OK = "{\"status\": 1, \"message\": \"OK\"}";
    public static final String LoggedInAlready = "{\"status\": -1, \"message\": \"You're logged in already\"}";
    public static final String LoginIsBusy = "{\"status\": -2, \"message\": \"This login is busy\"}";
    public static final String IncorrectCredentials = "{\"status\": -3, \"message\": \"Incorrect credentials\"}";
    public static final String Unauthorized = "{\"status\": -4, \"message\": \"Unauthorized\"}";
    public static final String NotFound = "{\"status\": -5, \"message\": \"Not found\"}";
    public static final String Forbidden = "{\"status\": -6, \"message\": \"Forbidden\"}";

    public static String OK (String message) {
        return String.format("{\"status\": 1, \"message\": \"%s\"}",
                message.replace("\"", "\\\""));
    }
}