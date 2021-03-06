
package ml.ikslib.gateway.routing;

import java.util.ArrayList;
import java.util.Collection;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.AbstractGateway.Status;
import ml.ikslib.gateway.helper.Common;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.ussd.USSDRequest;

public abstract class AbstractRouter {
	public Collection<AbstractGateway> route(OutboundMessage message, Collection<AbstractGateway> gateways) {
		AbstractGateway candidateGateway;
		ArrayList<AbstractGateway> candidateGateways = new ArrayList<>();
		for (AbstractGateway g : gateways) {
			candidateGateway = null;
			if (Common.isNullOrEmpty(message.getGatewayId())) candidateGateway = g;
			else if (message.getGatewayId().equalsIgnoreCase(g.getGatewayId())) candidateGateway = g;
			if (candidateGateway == null) continue;
			if (candidateGateway.getStatus() != Status.Started) continue;
			if (!candidateGateway.getCapabilities().matches(message)) continue;
			candidateGateways.add(candidateGateway);
		}
		return customRoute(message, candidateGateways);
	}

	public Collection<AbstractGateway> route(USSDRequest ussdRequest, Collection<AbstractGateway> gateways) {
		AbstractGateway candidateGateway;
		ArrayList<AbstractGateway> candidateGateways = new ArrayList<>();
		for (AbstractGateway g : gateways) {
			candidateGateway = null;
			if (Common.isNullOrEmpty(ussdRequest.getGatewayId())) candidateGateway = g;
			else if (ussdRequest.getGatewayId().equalsIgnoreCase(g.getGatewayId())) candidateGateway = g;
			if (candidateGateway == null) continue;
			if (candidateGateway.getStatus() != Status.Started) continue;
			candidateGateways.add(candidateGateway);
		}
		return customRoute(ussdRequest, candidateGateways);
	}

	public abstract Collection<AbstractGateway> customRoute(OutboundMessage msg, Collection<AbstractGateway> gateways);

	public abstract Collection<AbstractGateway> customRoute(USSDRequest ussdRequest, Collection<AbstractGateway> gateways);
}
