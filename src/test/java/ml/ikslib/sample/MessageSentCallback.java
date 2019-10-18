
package ml.ikslib.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.callback.IMessageSentCallback;
import ml.ikslib.gateway.callback.events.MessageSentCallbackEvent;

public class MessageSentCallback implements IMessageSentCallback
{
	final Logger logger = LoggerFactory.getLogger(MessageSentCallback.class);

	@Override
	public boolean process(MessageSentCallbackEvent event)
	{
		logger.info("[MessageSentCallback] " + event.getMessage().toShortString());
		return true;
	}
}
