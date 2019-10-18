
package ml.ikslib.gateway.hook;

import ml.ikslib.gateway.message.OutboundMessage;

public interface IPreSendHook
{
	public boolean process(OutboundMessage message);
}
