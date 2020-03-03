
package ml.ikslib.gateway.callback.events;

import ml.ikslib.gateway.message.InboundMessage;

public class CnmiCallbackEvent extends BaseCallbackEvent {
	String msgMemory;
	String msgIndex;

	public CnmiCallbackEvent(String msgMemory, String msgIndex) {
		this.msgIndex = msgIndex;
		this.msgMemory = msgMemory;
	}

	public String getMsgMemory() {
		return msgMemory;
	}

	public String getMsgIndex() {
		return msgIndex;
	}
}
