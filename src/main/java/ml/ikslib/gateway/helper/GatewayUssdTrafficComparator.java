
package ml.ikslib.gateway.helper;

import ml.ikslib.gateway.AbstractGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Comparator;

public class GatewayUssdTrafficComparator implements Comparator<AbstractGateway>, Serializable {
	static Logger logger = LoggerFactory.getLogger(GatewayUssdTrafficComparator.class);

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(AbstractGateway g1, AbstractGateway g2) {
		try {
			return (Integer.compare(g1.getStatistics().getTotalUssdSent() + g1.getQueueLoad(), g2.getStatistics().getTotalUssdSent()
					+ g2.getQueueLoad()));
		} catch (Exception e) {
			logger.error("Unhandled exception!", e);
			return 0;
		}
	}
}
