
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.message.DeliveryReportMessage;

public class DeliveryReportCallbackEvent extends BaseCallbackEvent
{
	DeliveryReportMessage message;

	public DeliveryReportCallbackEvent(DeliveryReportMessage message)
	{
		this.message = message;
	}

	public DeliveryReportMessage getMessage()
	{
		return this.message;
	}
}
