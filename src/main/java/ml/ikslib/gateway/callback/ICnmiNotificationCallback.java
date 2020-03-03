
package ml.ikslib.gateway.callback;

import ml.ikslib.gateway.callback.events.CnmiCallbackEvent;
import ml.ikslib.gateway.callback.events.InboundMessageCallbackEvent;

public interface ICnmiNotificationCallback {
	public boolean process(CnmiCallbackEvent event);
}
