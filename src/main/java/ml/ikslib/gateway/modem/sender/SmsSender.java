package ml.ikslib.gateway.modem.sender;

import ml.ikslib.gateway.modem.client.common.SmsMessage;

public interface SmsSender {
    void send(SmsMessage smsMessage);
}
