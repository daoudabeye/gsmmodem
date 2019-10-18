
package ml.ikslib.gateway.threading;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.AbstractGateway.Status;
import ml.ikslib.gateway.Service;
import ml.ikslib.gateway.core.Settings;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.message.OutboundMessage.FailureCause;
import ml.ikslib.gateway.message.OutboundMessage.SentStatus;
import ml.ikslib.gateway.queue.IOutboundQueue;

public class GatewayMessageDispatcher extends Thread {
	static Logger logger = LoggerFactory.getLogger(GatewayMessageDispatcher.class);

	boolean shouldCancel = false;

	IOutboundQueue<OutboundMessage> messageQueue;

	AbstractGateway gateway;

	public GatewayMessageDispatcher(String name, IOutboundQueue<OutboundMessage> messageQueue,
			AbstractGateway gateway) {
		setName(name);
		setDaemon(false);
		this.messageQueue = messageQueue;
		this.gateway = gateway;
	}

	@Override
	public void run() {
		logger.debug("Started!");
		while (!this.shouldCancel) {
			try {
				OutboundMessage message = this.messageQueue.get(Settings.gatewayDispatcherQueueTimeout,
						TimeUnit.MILLISECONDS);
				if (message != null) {
					boolean sendOk;
					try {
						sendOk = this.gateway.send(message);
					} catch (Exception e) {
						logger.error("Send failed!", e);
						sendOk = false;
					}
					if (sendOk) {
						Service.getInstance().getCallbackManager().registerMessageSentEvent(message);
					} else {
						message.getRoutingTable().removeFirst();
						if (message.getRoutingTable().size() > 0) {
							logger.debug("Re-routing message to : " + message.getRoutingTable().get(0).getGatewayId());
							if (message.getRoutingTable().get(0).getStatus() != Status.Started) {
								logger.debug("Routing path contains inactive gateway, re-routing to main queue...");
								Service.getInstance().queue(message);
							} else
								message.getRoutingTable().get(0).queue(message);
						} else {
							message.setSentStatus(SentStatus.Failed);
							message.setFailureCause(FailureCause.NoRoute);
							Service.getInstance().getCallbackManager().registerMessageSentEvent(message);
						}
					}
				}
				sleep(Settings.gatewayDispatcherYield);
			} catch (InterruptedException e) {
				if (!this.shouldCancel)
					logger.error("Interrupted!", e);
			} catch (Exception e) {
				logger.error("Unhandled exception!", e);
			}
		}
		logger.debug("Stopped!");
	}

	public void cancel() {
		logger.debug("Cancelling!");
		this.shouldCancel = true;
	}
}
