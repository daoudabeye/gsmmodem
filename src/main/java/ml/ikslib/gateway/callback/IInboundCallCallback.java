
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.InboundCallCallbackEvent;

public interface IInboundCallCallback
{
	public boolean process(InboundCallCallbackEvent event);
}
