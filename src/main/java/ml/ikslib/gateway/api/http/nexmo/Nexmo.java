
package ml.ikslib.gateway.api.http.nexmo;

import java.net.URLConnection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import ml.ikslib.gateway.ussd.USSDRequest;
import ml.ikslib.gateway.ussd.USSDResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.api.http.AbstractHttpGateway;
import ml.ikslib.gateway.core.Capabilities;
import ml.ikslib.gateway.core.Capabilities.Caps;
import ml.ikslib.gateway.message.AbstractMessage.Encoding;
import ml.ikslib.gateway.message.OutboundMessage;
import ml.ikslib.gateway.message.OutboundMessage.FailureCause;
import ml.ikslib.gateway.message.OutboundMessage.SentStatus;

public class Nexmo extends AbstractHttpGateway
{
	String key;

	String secret;

	public Nexmo(String gatewayId, String key, String secret)
	{
		super(gatewayId, "Nexmo (http://www.nexmo.com)");
		Capabilities caps = new Capabilities();
		caps.set(Caps.CanSendMessage);
		caps.set(Caps.CanSetSenderId);
		caps.set(Caps.CanSplitMessages);
		caps.set(Caps.CanQueryCreditBalance);
		caps.set(Caps.CanRequestDeliveryStatus);
		caps.set(Caps.CanSendUnicodeMessage);
		setCapabilities(caps);
		setBaseUrl("https://rest.nexmo.com/sms/xml");
		setSubmitMessageUrl("");
		setQueryMessageUrl("");
		setQueryBalanceUrl("");
		setHttpMethod(HttpMethod.POST);
		setHttpEncoding("UTF-8");
		setMaxMessageParts(1);
		this.key = key;
		this.secret = secret;
	}

	public Nexmo(String gatewayId, String... parms)
	{
		this(gatewayId, parms[0], parms[1]);
	}

	@Override
	protected void prepareParameters(Operation operation, Object o, Hashtable<String, String> parameters, Object... args)
	{
		OutboundMessage message;
		switch (operation)
		{
			case SendMessage:
				message = (OutboundMessage) o;
				parameters.put("username", this.key);
				parameters.put("password", this.secret);
				if (!message.getOriginatorAddress().isVoid()) parameters.put("from", message.getOriginatorAddress().getAddress());
				else if (!getSenderAddress().isVoid()) parameters.put("from", getSenderAddress().getAddress());
				parameters.put("to", message.getRecipientAddress().getAddress());
				parameters.put("text", translateText(message.getPayload().getText()));
				parameters.put("client-ref", message.getId());
				if (message.getEncoding() == Encoding.EncUcs2) parameters.put("type", "unicode");
				if (message.getRequestDeliveryReport() || getRequestDeliveryReport()) parameters.put("status-report-req", "1");
				break;
			case QueryBalance:
				break;
			default:
				throw new RuntimeException("Not implemented!");
		}
	}

	@Override
	protected void prepareUrlConnection(URLConnection con)
	{
		con.setRequestProperty("Accept", "application/xml");
	}

	@Override
	protected void parseResponse(Operation operation, Object o, List<String> responseList) throws Exception
	{
		OutboundMessage message;
		Document xml;
		switch (operation)
		{
			case SendMessage:
				message = (OutboundMessage) o;
				xml = loadXMLFromString(responseList);
				if (getXMLTextValue((Element) xml.getElementsByTagName("message").item(0), "status").equalsIgnoreCase("0"))
				{
					double totalCost = 0;
					for (int i = 0; i < xml.getElementsByTagName("message").getLength(); i++)
					{
						message.getOperatorMessageIds().add(getXMLTextValue((Element) xml.getElementsByTagName("message").item(i), "messageId"));
						totalCost += getXMLDoubleValue((Element) xml.getElementsByTagName("message").item(0), "messagePrice");
					}
					message.setCreditsUsed(totalCost);
					message.setGatewayId(getGatewayId());
					message.setSentDate(new Date());
					message.setSentStatus(SentStatus.Sent);
					message.setFailureCause(FailureCause.None);
				}
				else
				{
					message.setSentStatus(SentStatus.Failed);
					message.setOperatorFailureCode(getXMLTextValue((Element) xml.getElementsByTagName("message").item(0), "status"));
					switch (getXMLIntegerValue((Element) xml.getElementsByTagName("message").item(0), "status"))
					{
						case 1:
							message.setFailureCause(FailureCause.Unavailable);
							break;
						case 2:
							message.setFailureCause(FailureCause.MissingParms);
							break;
						case 4:
							message.setFailureCause(FailureCause.AuthFailure);
							break;
						case 6:
							message.setFailureCause(FailureCause.BadFormat);
							break;
						case 9:
							message.setFailureCause(FailureCause.OverQuota);
							break;
						default:
							message.setFailureCause(FailureCause.UnknownFailure);
							break;
					}
				}
				break;
			case QueryBalance:
				if (responseList.size() == 1)
				{
					xml = loadXMLFromString(responseList.get(0));
					((AbstractGateway) o).getCreditBalance().setCredits(getXMLDoubleValue((Element) xml.getElementsByTagName("accountBalance").item(0), "value"));
				}
				break;
			default:
				throw new RuntimeException("Not implemented!");
		}
	}

	@Override
	public String getQueryBalanceUrl()
	{
		return String.format("https://rest.nexmo.com/account/get-balance/%s/%s", this.key, this.secret);
	}

	@Override
	public HttpMethod getOperationHttpMethod(Operation op)
	{
		if (op == Operation.QueryBalance) return HttpMethod.GET;
		return super.getOperationHttpMethod(op);
	}

	@Override
	protected String translateText(String text)
	{
		return text;
	}

	@Override
	protected USSDResponse _sendUSSDCommand(USSDRequest request, boolean interactive) throws Exception {
		return null;
	}

	@Override
	public void cleanMemory() throws Exception {

	}
}
