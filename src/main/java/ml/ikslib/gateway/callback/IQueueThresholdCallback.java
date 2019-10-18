
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.QueueThresholdCallbackEvent;

public interface IQueueThresholdCallback
{
	public boolean process(QueueThresholdCallbackEvent event);
}
