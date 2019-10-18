
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.InboundMessageCallbackEvent;

public interface IInboundMessageCallback
{
	public boolean process(InboundMessageCallbackEvent event);
}
