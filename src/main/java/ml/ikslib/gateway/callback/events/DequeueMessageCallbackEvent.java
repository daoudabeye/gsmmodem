
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.message.OutboundMessage;

public class DequeueMessageCallbackEvent extends BaseCallbackEvent
{
	OutboundMessage message;

	public DequeueMessageCallbackEvent(OutboundMessage message)
	{
		this.message = message;
	}

	public OutboundMessage getMessage()
	{
		return this.message;
	}
}
