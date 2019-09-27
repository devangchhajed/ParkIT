package com.codeencounter.nagarro;

public interface SmsListener {
    public void messageReceived(String messageText, String sender);
}
