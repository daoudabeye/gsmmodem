
package ml.ikslib.gsmmodem;

import javax.crypto.spec.SecretKeySpec;

import ml.ikslib.gateway.ussd.USSDDcs;
import ml.ikslib.gateway.ussd.USSDRequest;
import ml.ikslib.gateway.ussd.USSDResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import ml.ikslib.gateway.Service;
import ml.ikslib.gateway.callback.IDeliveryReportCallback;
import ml.ikslib.gateway.callback.IInboundMessageCallback;
import ml.ikslib.gateway.callback.events.DeliveryReportCallbackEvent;
import ml.ikslib.gateway.callback.events.InboundMessageCallbackEvent;
import ml.ikslib.gateway.crypto.AESKey;
import ml.ikslib.gateway.modem.Modem;

public class Test_SerialModem extends TestCase {
	static Logger logger = LoggerFactory.getLogger(Test_SerialModem.class);

	public static String RECIPIENT = "+22365110347";

	public class InboundMessageCallback implements IInboundMessageCallback {
		@Override
		public boolean process(InboundMessageCallbackEvent event) {
			logger.info("[InboundMessageCallback] " + event.getMessage().toShortString());
			logger.info(event.getMessage().toString());
			return true;
		}
	}

	public class DeliveryReportCallback implements IDeliveryReportCallback {
		@Override
		public boolean process(DeliveryReportCallbackEvent event) {
			logger.info("[DeliveryReportCallback] " + event.getMessage().toShortString());
			logger.info(event.getMessage().toString());
			return true;
		}
	}

	public void test() throws Exception {
		if (RECIPIENT.length() > 0)
			Service.getInstance().getKeyManager().registerKey(RECIPIENT,
					new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
		Service.getInstance().setInboundMessageCallback(new InboundMessageCallback());
		Service.getInstance().setDeliveryReportCallback(new DeliveryReportCallback());
		Service.getInstance().start();
		Modem gateway = new Modem("modem", "COM8", "115200", "0000", "", "", "");
		
		Service.getInstance().registerGateway(gateway);

//		// Print out some device information.
		logger.info("Manufacturer       : " + gateway.getDeviceInformation().getManufacturer());
		logger.info("Signal (RSSI)      : " + gateway.getDeviceInformation().getRssi() + "dBm");
		logger.info("Mode               : " + gateway.getDeviceInformation().getMode());
		logger.info("Supported Encodings: " + gateway.getDeviceInformation().getSupportedEncodings());
		
		// Sleep to emulate async operation.
		
		logger.info("Gateway ready....:");
		//Thread.sleep(80000);
		
		
		if (RECIPIENT.length() > 0) {

			USSDRequest request = new USSDRequest("#555#");
			logger.info("Sending ussd:");
			USSDResponse res = gateway.send(request);

			logger.info("Gateway Response....:");
//			logger.info(res.toString());
			//Thread.sleep(60000);
		}
		Service.getInstance().unregisterGateway(gateway);
		try {
			gateway.refreshDeviceInfo();
			throw new RuntimeException("Should never get here!");
		} catch (Exception e) {
			// Normal to get here...
		}
		Service.getInstance().stop();
		Service.getInstance().terminate();
	}
}
