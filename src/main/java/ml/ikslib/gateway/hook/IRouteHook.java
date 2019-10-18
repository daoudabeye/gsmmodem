
package ml.ikslib.gateway.hook;

import java.util.Collection;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.message.OutboundMessage;

public interface IRouteHook
{
	public Collection<AbstractGateway> process(OutboundMessage message, Collection<AbstractGateway> gateways);
}
