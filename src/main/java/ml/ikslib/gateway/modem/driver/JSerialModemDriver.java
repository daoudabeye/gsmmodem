
package ml.ikslib.gateway.modem.driver;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import ml.ikslib.gateway.callback.events.CnmiCallbackEvent;
import ml.ikslib.gateway.modem.Modem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;

import static com.fazecast.jSerialComm.SerialPort.*;

public class JSerialModemDriver extends AbstractModemDriver implements SerialPortDataListener {
    static Logger logger = LoggerFactory.getLogger(JSerialModemDriver.class);

    String portName;
    int baudRate;
    SerialPort serialPort;

    protected StringBuffer linebuffer = new StringBuffer(4096);

    public JSerialModemDriver(Modem modem, String port, int baudRate) {
        super(modem);
        this.portName = port;
        this.baudRate = baudRate;
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(ONE_STOP_BIT);
        serialPort.setParity(NO_PARITY);
        serialPort.addDataListener(this);
//		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    @Override
    public void openPort() throws NumberFormatException, IOException {
        logger.debug("Opening comm port: " + getPortInfo());
        if (!serialPort.openPort()) {
            throw new IOException("Port not opened");
        }
        this.in = serialPort.getInputStream();
        this.out = serialPort.getOutputStream();
//        this.serialPort.setComPortParameters(this.baudRate, 8, ONE_STOP_BIT, NO_PARITY);
    }

    @Override
    public void closePort() throws IOException {
        logger.debug("Closing comm port: " + getPortInfo());
        this.in.close();
        this.in = null;
        this.out.close();
        this.out = null;
        if (!serialPort.closePort()) {
            throw new IOException("Port not closed");
        }
    }

    @Override
    public String getPortInfo() {
        return this.portName + ":" + this.baudRate;
    }

    @Override
    public int getListeningEvents() {
        return LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
			if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
				return;
            this.readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readData() throws IOException {
        boolean foundClip = false;
        while (hasData()) {
            boolean breakWhile = false;
            try {
                char c = (char) read();
                JSerialModemDriver.this.buffer.append(c);
                this.linebuffer.append(c);
				logger.debug("> " + c);
                if (c == '\n' || c == '\r') {
                    String l = linebuffer.toString();
                    logger.info("Line >" + linebuffer.toString());
                    linebuffer.delete(0, linebuffer.length());
//                    readLine(l);
                    Matcher cmtiMatch = this.CMTI_REGEX.matcher(l);
                    if(cmtiMatch.find()){
                        String msgMemory = cmtiMatch.group(1);
                        String msgIndex = cmtiMatch.group(2);
                        this.modem.getMessageReader().addCnmiEvent(new CnmiCallbackEvent(msgMemory, msgIndex));
                    }
                }
            } catch (Exception e) {
                logger.debug("Error Reading :" + e.getMessage(), e);
                breakWhile = true;
            }

            if (JSerialModemDriver.this.buffer.indexOf("+CLIP") >= 0) {
                if (!foundClip) {
                    foundClip = true;
                    new ClipReader().start();
                }
            } else
                foundClip = false;

            if (breakWhile)
                break;
        }
    }

    public boolean process(CnmiCallbackEvent event) throws Exception {
        String msgMemory = event.getMsgMemory();
        String msgIndex = event.getMsgIndex();

        logger.warn("Reading " + event.getMsgIndex());
        String smsData = this.readStoredSms(msgIndex, msgMemory).getResponseData();
        logger.warn("Readed " + smsData);
        return true;
    }
}
