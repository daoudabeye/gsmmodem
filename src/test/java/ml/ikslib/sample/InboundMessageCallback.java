
package ml.ikslib.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.callback.IInboundMessageCallback;
import ml.ikslib.gateway.callback.events.InboundMessageCallbackEvent;

public class InboundMessageCallback implements IInboundMessageCallback {
	final Logger logger = LoggerFactory.getLogger(InboundMessageCallback.class);

	@Override
	public boolean process(InboundMessageCallbackEvent event) {
		// TODO Auto-generated method stub
		logger.info("[InboundMessageCallback] " + event.getMessage().getPayload().getText());
		logger.debug(event.getMessage().toString());
		
		return true;
	}
}
