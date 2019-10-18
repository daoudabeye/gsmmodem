
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.message.InboundMessage;

public class InboundMessageCallbackEvent extends BaseCallbackEvent
{
	InboundMessage message;

	public InboundMessageCallbackEvent(InboundMessage message)
	{
		this.message = message;
	}

	public InboundMessage getMessage()
	{
		return this.message;
	}
}
