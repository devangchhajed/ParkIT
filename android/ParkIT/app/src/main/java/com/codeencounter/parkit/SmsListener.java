package com.codeencounter.parkit;

public interface SmsListener {
    public void messageReceived(String messageText, String sender);
}
