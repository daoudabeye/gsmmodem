
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.ussd.USSDResponse;

import java.util.Date;

public class IUSSDNotificationEvent extends BaseCallbackEvent{
	USSDResponse response;

	public IUSSDNotificationEvent(USSDResponse response) {
		this.response = response;
	}

	public USSDResponse getResponse() {
		return response;
	}
}
