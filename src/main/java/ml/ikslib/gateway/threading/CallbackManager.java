
package ml.ikslib.gateway.threading;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.Service;
import ml.ikslib.gateway.callback.IDeliveryReportCallback;
import ml.ikslib.gateway.callback.IDequeueMessageCallback;
import ml.ikslib.gateway.callback.IGatewayStatusCallback;
import ml.ikslib.gateway.callback.IInboundCallCallback;
import ml.ikslib.gateway.callback.IInboundMessageCallback;
import ml.ikslib.gateway.callback.IMessageSentCallback;
import ml.ikslib.gateway.callback.IQueueThresholdCallback;
import ml.ikslib.gateway.callback.IServiceStatusCallback;
import ml.ikslib.gateway.callback.events.BaseCallbackEvent;
import ml.ikslib.gateway.callback.events.DeliveryReportCallbackEvent;
import ml.ikslib.gateway.callback.events.DequeueMessageCallbackEvent;
import ml.ikslib.gateway.callback.events.GatewayStatusCallbackEvent;
import ml.ikslib.gateway.callback.events.InboundCallCallbackEvent;
import ml.ikslib.gateway.callback.events.InboundMessageCallbackEvent;
import ml.ikslib.gateway.callback.events.MessageSentCallbackEvent;
import ml.ikslib.gateway.callback.events.QueueThresholdCallbackEvent;
import ml.ikslib.gateway.callback.events.ServiceStatusCallbackEvent;
import ml.ikslib.gateway.core.Settings;
import ml.ikslib.gateway.message.DeliveryReportMessage;
import ml.ikslib.gateway.message.InboundMessage;
import ml.ikslib.gateway.message.MsIsdn;
import ml.ikslib.gateway.message.OutboundMessage;

public class CallbackManager {
	static Logger logger = LoggerFactory.getLogger(CallbackManager.class);

	LinkedBlockingQueue<BaseCallbackEvent> eventQueue = new LinkedBlockingQueue<>();

	IServiceStatusCallback serviceStatusCallback = null;

	IGatewayStatusCallback gatewayStatusCallback = null;

	IMessageSentCallback messageSentCallback = null;

	IInboundMessageCallback inboundMessageCallback = null;

	IInboundCallCallback inboundCallCallback = null;

	IDeliveryReportCallback deliveryReportCallback = null;

	IDequeueMessageCallback dequeueMessageCallback = null;

	IQueueThresholdCallback queueThresholdCallback = null;

	CallbackManagerDispatcher dispatcher = null;

	boolean shouldCancel = false;

	public boolean registerServiceStatusEvent(Service.Status oldStatus, Service.Status newStatus) {
		return (this.serviceStatusCallback == null ? false
				: this.eventQueue.add(new ServiceStatusCallbackEvent(oldStatus, newStatus)));
	}

	public boolean registerGatewayStatusEvent(AbstractGateway gateway, AbstractGateway.Status oldStatus,
			AbstractGateway.Status newStatus) {
		return (this.gatewayStatusCallback == null ? false
				: this.eventQueue.add(new GatewayStatusCallbackEvent(gateway, oldStatus, newStatus)));
	}

	public boolean registerMessageSentEvent(OutboundMessage message) {
		return (this.messageSentCallback == null ? false : this.eventQueue.add(new MessageSentCallbackEvent(message)));
	}

	public boolean registerInboundMessageEvent(InboundMessage message) {
		return (this.inboundMessageCallback == null ? false
				: this.eventQueue.add(new InboundMessageCallbackEvent(message)));
	}

	public boolean registerInboundCallEvent(MsIsdn msisdn, String gatewayId) {
		return (this.inboundCallCallback == null ? false
				: this.eventQueue.add(new InboundCallCallbackEvent(msisdn, gatewayId)));
	}

	public boolean registerDeliveryReportEvent(DeliveryReportMessage message) {
		return (this.deliveryReportCallback == null ? false
				: this.eventQueue.add(new DeliveryReportCallbackEvent(message)));
	}

	public boolean registerDequeueMessageEvent(OutboundMessage message) {
		return (this.dequeueMessageCallback == null ? false
				: this.eventQueue.add(new DequeueMessageCallbackEvent(message)));
	}

	public boolean registerQueueThresholdEvent(int queueLoad) {
		return (this.queueThresholdCallback == null ? false
				: this.eventQueue.add(new QueueThresholdCallbackEvent(queueLoad)));
	}

	public void setServiceStatusCallback(IServiceStatusCallback serviceStatusCallback) {
		this.serviceStatusCallback = serviceStatusCallback;
	}

	public void setGatewayStatusCallback(IGatewayStatusCallback gatewayStatusCallback) {
		this.gatewayStatusCallback = gatewayStatusCallback;
	}

	public void setMessageSentCallback(IMessageSentCallback messageSentCallback) {
		this.messageSentCallback = messageSentCallback;
	}

	public void setInboundMessageCallback(IInboundMessageCallback inboundMessageCallback) {
		this.inboundMessageCallback = inboundMessageCallback;
	}

	public void setInboundCallCallback(IInboundCallCallback inboundCallCallback) {
		this.inboundCallCallback = inboundCallCallback;
	}

	public void setDeliveryReportCallback(IDeliveryReportCallback deliveryReportCallback) {
		this.deliveryReportCallback = deliveryReportCallback;
	}

	public void setDequeueMessageCallback(IDequeueMessageCallback dequeueMessageCallback) {
		this.dequeueMessageCallback = dequeueMessageCallback;
	}

	public void setQueueThresholdCallback(IQueueThresholdCallback queueThresholdCallback) {
		this.queueThresholdCallback = queueThresholdCallback;
	}

	public int getQueueLoad() {
		return this.eventQueue.size();
	}

	public void start() {
		logger.debug("Starting...");
		this.dispatcher = new CallbackManagerDispatcher();
		this.dispatcher.start();
	}

	public void stop() {
		logger.debug("Cancelling!");
		this.shouldCancel = true;
		this.dispatcher.interrupt();
		try {
			this.dispatcher.join();
		} catch (InterruptedException e) {
			logger.warn("Interrupted!", e);
		}
		this.dispatcher = null;
	}

	public class CallbackManagerDispatcher extends Thread {
		public CallbackManagerDispatcher() {
			setName("Callback Manager Dispatcher");
			setDaemon(false);
		}

		@Override
		public void run() {
			logger.debug("Started!");
			while (!CallbackManager.this.shouldCancel) {
				try {
					BaseCallbackEvent ev = CallbackManager.this.eventQueue.poll(Settings.callbackDispatcherQueueTimeout,
							TimeUnit.MILLISECONDS);
					if (ev != null) {
						boolean consumed = false;
						boolean handlerFound = false;
						if (ev instanceof ServiceStatusCallbackEvent) {
							if (CallbackManager.this.serviceStatusCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.serviceStatusCallback
											.process((ServiceStatusCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in ServiceStatusCallback!", e);
								}
							}
						} else if (ev instanceof GatewayStatusCallbackEvent) {
							if (CallbackManager.this.gatewayStatusCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.gatewayStatusCallback
											.process((GatewayStatusCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in GatewayStatusCallback!", e);
								}
							}
						} else if (ev instanceof MessageSentCallbackEvent) {
							if (CallbackManager.this.messageSentCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.messageSentCallback
											.process((MessageSentCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in MessageSentCallback!", e);
								}
							}
						} else if (ev instanceof InboundMessageCallbackEvent) {
							if (CallbackManager.this.inboundMessageCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.inboundMessageCallback
											.process((InboundMessageCallbackEvent) ev);
									if (consumed && Settings.deleteMessagesAfterCallback)
										Service.getInstance().delete(((InboundMessageCallbackEvent) ev).getMessage());
								} catch (Exception e) {
									logger.error("Error in InboundMessageCallback!", e);
								}
							}
						} else if (ev instanceof InboundCallCallbackEvent) {
							if (CallbackManager.this.inboundCallCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.inboundCallCallback
											.process((InboundCallCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in InboundCallCallback!", e);
								}
							}
						} else if (ev instanceof DeliveryReportCallbackEvent) {
							if (CallbackManager.this.deliveryReportCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.deliveryReportCallback
											.process((DeliveryReportCallbackEvent) ev);
									if (consumed && Settings.deleteMessagesAfterCallback)
										Service.getInstance().delete(((DeliveryReportCallbackEvent) ev).getMessage());
								} catch (Exception e) {
									logger.error("Error in DeliveryReportCallback!", e);
								}
							}
						} else if (ev instanceof DequeueMessageCallbackEvent) {
							if (CallbackManager.this.dequeueMessageCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.dequeueMessageCallback
											.process((DequeueMessageCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in DequeueMessageCallback!", e);
								}
							}
						} else if (ev instanceof QueueThresholdCallbackEvent) {
							if (CallbackManager.this.queueThresholdCallback != null) {
								try {
									handlerFound = true;
									consumed = CallbackManager.this.queueThresholdCallback
											.process((QueueThresholdCallbackEvent) ev);
								} catch (Exception e) {
									logger.error("Error in QueueThresholdCallback!", e);
								}
							}
						}
						if (handlerFound && !consumed)
							CallbackManager.this.eventQueue.put(ev);
					}
					sleep(Settings.callbackDispatcherYield);
				} catch (InterruptedException e1) {
					if (!CallbackManager.this.shouldCancel)
						logger.error("Interrupted!", e1);
				} catch (Exception e2) {
					logger.error("Unhandled exception!", e2);
				}
			}
			logger.debug("Stopped!");
		}
	}
}
