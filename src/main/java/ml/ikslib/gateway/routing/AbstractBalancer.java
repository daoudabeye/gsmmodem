
package ml.ikslib.gateway.routing;

import java.util.Collection;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.ussd.USSDRequest;

public abstract class AbstractBalancer {
	public abstract Collection<AbstractGateway> balance(OutboundMessage message,
			Collection<AbstractGateway> candidates);

	public abstract Collection<AbstractGateway> balance(USSDRequest ussdRequest,
														Collection<AbstractGateway> candidates);
}
