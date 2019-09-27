package in.zuts.parkitsmsserver;

public interface SmsListener {
    public void messageReceived(String messageText, String sender);
}
