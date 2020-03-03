package ml.ikslib.gateway.modem.driver;

import static com.fazecast.jSerialComm.SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
import static com.fazecast.jSerialComm.SerialPort.NO_PARITY;
import static com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import ml.ikslib.gateway.modem.Modem;
import ml.ikslib.gateway.modem.client.jserialcomm.SerialEventExceptionListener;

public class J2SerialModemDriver extends AbstractModemDriver implements SerialPortDataListener {

	private final SerialPort serialPort;

	private SerialEventExceptionListener serialEventExceptionListener;

	static Logger logger = LoggerFactory.getLogger(J2SerialModemDriver.class);

	String portName;
	int baudRate;
	
	private boolean foundClip = false;

	public J2SerialModemDriver(Modem modem, String port, int baudRate) {
		super(modem);
		this.portName = port;
		this.baudRate = baudRate;
		serialPort = SerialPort.getCommPort(portName);
		serialPort.setBaudRate(baudRate);
		serialPort.setNumDataBits(8);
		serialPort.setNumStopBits(ONE_STOP_BIT);
		serialPort.setParity(NO_PARITY);
		serialPort.addDataListener(this);
	}

	@Override
	public int getListeningEvents() {
		return LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		try {
			byte[] readBytes = readBytes();
			if (readBytes != null) {
				for (byte readByte : readBytes) {
//					onReadByte(readByte);
					char c = (char) readByte;
					this.buffer.append(c);
					if (this.buffer.indexOf("+CLIP") >= 0) {
						if (!this.foundClip) {
							this.foundClip = true;
							new ClipReader().start();
						}
					} else
						this.foundClip = false;
				}
				
//				System.err.println(this.buffer.toString());
			}
		} catch (Exception e) {
			if (serialEventExceptionListener != null) {
				serialEventExceptionListener.onException(e);
			}
		}

	}

	@Override
	protected boolean hasData() throws IOException {
		return ((serialPort != null) && (this.serialPort.bytesAvailable() > 0)) && (this.in != null) && (this.in.available() > 0);
	}

	private byte[] readBytes() {
		int bytesAvailable = serialPort.bytesAvailable();
		if (bytesAvailable == 0) {
			return null;
		} else if (bytesAvailable > 0) {
			byte[] bytes = new byte[bytesAvailable];
			if (serialPort.readBytes(bytes, bytes.length) == bytes.length) {
				return bytes;
			} else {
				throw new RuntimeException("Error reading bytes");
			}
		} else {
			throw new RuntimeException("Port not opened");
		}
	}

	@Override
	public void openPort() throws IOException, TimeoutException, InterruptedException {
		// TODO Auto-generated method stub
		logger.debug("Opening comm port: " + getPortInfo());

		if (!serialPort.openPort()) {
			throw new IOException("Port not opened");
		}
		this.in = serialPort.getInputStream();
		this.out = serialPort.getOutputStream();

	}

	@Override
	public void closePort() throws IOException, TimeoutException, InterruptedException {
		// TODO Auto-generated method stub
		logger.debug("Closing comm port: " + getPortInfo());
		this.in.close();
		this.in = null;
		this.out.close();
		this.out = null;
		this.byteArrayOutputStream.close();
		if (!serialPort.closePort()) {
			throw new IOException("Port not closed");
		}

	}

	@Override
	public String getPortInfo() {
		// TODO Auto-generated method stub
		return this.portName + ":" + this.baudRate;
	}

}
