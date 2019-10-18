
package ml.ikslib.gateway.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.helper.GatewayOutboundTrafficComparator;
import ml.ikslib.gateway.message.OutboundMessage;

public class DefaultBalancer extends AbstractBalancer {
	@Override
	public Collection<AbstractGateway> balance(OutboundMessage message, Collection<AbstractGateway> candidates) {
		ArrayList<AbstractGateway> gatewayList = new ArrayList<>(candidates);
		GatewayOutboundTrafficComparator comp = new GatewayOutboundTrafficComparator();
		Collections.sort(gatewayList, comp);
		return gatewayList;
	}
}
