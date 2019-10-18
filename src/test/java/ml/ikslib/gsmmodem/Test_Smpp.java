
package ml.ikslib.gsmmodem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import ml.ikslib.gateway.Service;
import ml.ikslib.gateway.api.smpp.Smpp;
import ml.ikslib.gateway.message.MsIsdn;
import ml.ikslib.gateway.message.OutboundMessage;

public class Test_Smpp extends TestCase
{
	static Logger logger = LoggerFactory.getLogger(Test_Smpp.class);

	public static String RECIPIENT = "";

	public void test() throws Exception
	{
		Service.getInstance().start();
		Smpp gateway = new Smpp("smpp", "smpp.my-smpp-server.com", 8000, "system-id", "password");
		Service.getInstance().registerGateway(gateway);
		Thread.sleep(10000);
		OutboundMessage m = new OutboundMessage("306974...", "Test");
		m.setOriginatorAddress(new MsIsdn("SMSLIB"));
		Service.getInstance().send(m);
		logger.debug(m.toString());
		m = new OutboundMessage("306974...", "Test");
		m.setOriginatorAddress(new MsIsdn("306974..."));
		Service.getInstance().send(m);
		logger.debug(m.toString());
		Thread.sleep(10000);
		Service.getInstance().unregisterGateway(gateway);
		Service.getInstance().stop();
		Service.getInstance().terminate();
	}
}
