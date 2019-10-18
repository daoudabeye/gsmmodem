
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.DeliveryReportCallbackEvent;

public interface IDeliveryReportCallback
{
	public boolean process(DeliveryReportCallbackEvent event);
}
