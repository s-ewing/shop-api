package com.example.shopapi.exceptions;

public class StripeSessionNotFoundException extends RuntimeException {
    public StripeSessionNotFoundException(String objectName, String sessionId) {
        super("Could not find " + objectName + "with session id " + sessionId);
    }
}
