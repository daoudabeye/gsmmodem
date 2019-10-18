
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.message.OutboundMessage;

public class MessageSentCallbackEvent extends BaseCallbackEvent
{
	OutboundMessage message;

	public MessageSentCallbackEvent(OutboundMessage message)
	{
		this.message = message;
	}

	public OutboundMessage getMessage()
	{
		return this.message;
	}
}
