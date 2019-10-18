
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.ServiceStatusCallbackEvent;

public interface IServiceStatusCallback
{
	public boolean process(ServiceStatusCallbackEvent event);
}
