
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.GatewayStatusCallbackEvent;

public interface IGatewayStatusCallback
{
	public boolean process(GatewayStatusCallbackEvent event);
}
