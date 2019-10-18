
package ml.ikslib.gateway.hook;

import ml.ikslib.gateway.message.OutboundMessage;

public interface IPreQueueHook
{
	public boolean process(OutboundMessage message);
}
