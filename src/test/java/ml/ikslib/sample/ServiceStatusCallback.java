
package ml.ikslib.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.callback.IServiceStatusCallback;
import ml.ikslib.gateway.callback.events.ServiceStatusCallbackEvent;

public class ServiceStatusCallback implements IServiceStatusCallback
{
	final Logger logger = LoggerFactory.getLogger(ServiceStatusCallback.class);

	@Override
	public boolean process(ServiceStatusCallbackEvent event)
	{
		logger.info("[ServiceStatusCallback] " + event.getOldStatus() + " -> " + event.getNewStatus());
		return true;
	}
}
