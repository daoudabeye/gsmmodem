
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.MessageSentCallbackEvent;

public interface IMessageSentCallback
{
	public boolean process(MessageSentCallbackEvent event);
}
