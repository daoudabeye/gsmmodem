
package ml.ikslib.gateway.helper;

import java.io.Serializable;
import java.util.Comparator;

import ml.ikslib.gateway.message.OutboundMessage;

public class MessagePriorityComparator implements Comparator<OutboundMessage>, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(OutboundMessage x, OutboundMessage y)
	{
		int comp = y.getPriority() - x.getPriority();
		if (comp == 0) comp = x.getCreationDate().compareTo(y.getCreationDate());
		return comp;
	}
}
