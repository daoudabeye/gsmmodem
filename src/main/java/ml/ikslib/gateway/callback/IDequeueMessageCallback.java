
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.DequeueMessageCallbackEvent;

public interface IDequeueMessageCallback
{
	public boolean process(DequeueMessageCallbackEvent event);
}
