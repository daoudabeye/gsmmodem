
package ml.ikslib.gsmmodem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.callback.IGatewayStatusCallback;
import ml.ikslib.gateway.callback.events.GatewayStatusCallbackEvent;

public class GatewayStatusCallback implements IGatewayStatusCallback {
	static Logger logger = LoggerFactory.getLogger(GatewayStatusCallback.class);

	@Override
	public boolean process(GatewayStatusCallbackEvent event) {
		System.err.println("teste ...");
		logger.info("[GatewayStatusCallback] " + event.getGateway().getGatewayId() + " = " + event.getOldStatus()
				+ " -> " + event.getNewStatus());

		return true;
	}
}
