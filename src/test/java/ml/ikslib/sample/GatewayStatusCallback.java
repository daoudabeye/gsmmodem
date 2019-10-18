
package ml.ikslib.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.callback.IGatewayStatusCallback;
import ml.ikslib.gateway.callback.events.GatewayStatusCallbackEvent;

public class GatewayStatusCallback implements IGatewayStatusCallback
{
	final Logger logger = LoggerFactory.getLogger(GatewayStatusCallback.class);

	@Override
	public boolean process(GatewayStatusCallbackEvent event)
	{
		logger.info("[GatewayStatusCallback] " + event.getOldStatus() + " -> " + event.getNewStatus());
		return true;
	}
}
