
package ml.ikslib.gateway.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.helper.GatewayPriorityComparator;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.ussd.USSDRequest;

public class PriorityBalancer extends AbstractBalancer {
	@Override
	public Collection<AbstractGateway> balance(OutboundMessage message, Collection<AbstractGateway> candidates) {
		ArrayList<AbstractGateway> gatewayList = new ArrayList<>(candidates);
		GatewayPriorityComparator comp = new GatewayPriorityComparator();
		Collections.sort(gatewayList, comp);
		Collections.reverse(gatewayList);
		return gatewayList;
	}

	@Override
	public Collection<AbstractGateway> balance(USSDRequest ussdRequest, Collection<AbstractGateway> candidates) {
		ArrayList<AbstractGateway> gatewayList = new ArrayList<>(candidates);
		GatewayPriorityComparator comp = new GatewayPriorityComparator();
		Collections.sort(gatewayList, comp);
		Collections.reverse(gatewayList);
		return gatewayList;
	}
}
