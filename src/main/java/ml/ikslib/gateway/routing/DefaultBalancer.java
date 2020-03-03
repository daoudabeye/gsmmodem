
package ml.ikslib.gateway.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.helper.GatewayOutboundTrafficComparator;
import ml.ikslib.gateway.helper.GatewayUssdTrafficComparator;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.ussd.USSDRequest;

public class DefaultBalancer extends AbstractBalancer {
	@Override
	public Collection<AbstractGateway> balance(OutboundMessage message, Collection<AbstractGateway> candidates) {
		ArrayList<AbstractGateway> gatewayList = new ArrayList<>(candidates);
		GatewayOutboundTrafficComparator comp = new GatewayOutboundTrafficComparator();
		Collections.sort(gatewayList, comp);
		return gatewayList;
	}

	@Override
	public Collection<AbstractGateway> balance(USSDRequest ussdRequest, Collection<AbstractGateway> candidates) {
		ArrayList<AbstractGateway> gatewayList = new ArrayList<>(candidates);
		GatewayUssdTrafficComparator comp = new GatewayUssdTrafficComparator();
		Collections.sort(gatewayList, comp);
		return gatewayList;
	}
}
