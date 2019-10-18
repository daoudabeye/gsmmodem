
package ml.ikslib.gateway.routing;

import java.util.Collection;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.message.OutboundMessage;

public class DefaultRouter extends AbstractRouter {
	@Override
	public Collection<AbstractGateway> customRoute(OutboundMessage message, Collection<AbstractGateway> gateways) {
		return gateways;
	}
}
