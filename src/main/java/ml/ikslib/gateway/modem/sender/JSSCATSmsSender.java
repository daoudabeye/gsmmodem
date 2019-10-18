package ml.ikslib.gateway.modem.sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPortException;
import ml.ikslib.gateway.modem.client.common.ATCommand;
import ml.ikslib.gateway.modem.client.common.SmsMessage;
import ml.ikslib.gateway.modem.client.jssc.ATDevice;

public class JSSCATSmsSender implements SmsSender {

    private final static Logger LOG =LogManager.getLogger(JSSCATSmsSender.class.getName());

    ATDevice device;
    private String deviceInformation;

//    @PostConstruct
//    public void init() throws Exception {
//        LOG.info("Initializing AT device ...");
//        device = new ATDevice(environment.getProperty("sms.at.port") , environment.getProperty("sms.at.pin"));
//        device.open();
//
//        deviceInformation = getDeviceInformation();
//        LOG.info("Device Information: \n" + deviceInformation);
//        selectTextFormat();
//        LOG.info("AT device initialized ");
//    }


    public void selectTextFormat() throws SerialPortException {
        device.execute(new ATCommand("AT+CMGF=1"));

    }

    public String getDeviceInformation() throws SerialPortException {
       return device.execute(new ATCommand("ATI"));
    }


//    @PreDestroy
    public void destroy() throws Exception {
        device.close();
    }


    @Override
    public void send(SmsMessage smsMessage) {
        try {
            LOG.info("Sending SMS message: " + smsMessage);
            device.execute(new ATCommand(String.format("AT+CMGS=\"%s\"", smsMessage.getRecipient()), smsMessage.getText()));
        } catch (SerialPortException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        return "JSSCATSmsSender{" +
                "device='" + device+ '\'' +
                "deviceInformation='" + deviceInformation + '\'' +
                '}';
    }
}
