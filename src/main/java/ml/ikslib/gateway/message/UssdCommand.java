package ml.ikslib.gateway.message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ml.ikslib.gateway.AbstractGateway;
import ml.ikslib.gateway.message.OutboundMessage.FailureCause;

public class UssdCommand extends AbstractMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ExecStatus {
		Executed("E"), Queued("Q"), Failed("F");
		private final String shortString;

		private ExecStatus(String shortString) {
			this.shortString = shortString;
		}

		public String toShortString() {
			return this.shortString;
		}
	}

	ExecStatus status = ExecStatus.Queued;

	FailureCause failureCause = FailureCause.None;

	String operatorFailureCode = "";

	List<String> operatorMessageIds = new ArrayList<>();

	int priority = 0;
	
	String shortCode;

	LinkedList<AbstractGateway> routingTable = new LinkedList<>();
	
	public UssdCommand() {
	}

	public UssdCommand(String code) {
		super(Type.Ussd, null, null, null);
		this.shortCode = code;
	}

	
	@Override
    public String toString() {
        return "UssdCommand{" +
                "code='" + shortCode + '\'' +
                "port='" + getGatewayId() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


	@Override
	public String getSignature() {
		// TODO Auto-generated method stub
		return hashSignature(String.format("%s-%s", getShortCode(), getId()));
	}

	@Override
	public String toShortString() {
		// TODO Auto-generated method stub
		return "UssdCommand{" +
        "code='" + shortCode + '\'' +
        "port='" + getGatewayId() + '\'' +
        ", status='" + status + '\'' +
        '}';
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	
	public ExecStatus getStatus() {
		return status;
	}

	public void setStatus(ExecStatus status) {
		this.status = status;
	}

	public FailureCause getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(FailureCause failureCause) {
		this.failureCause = failureCause;
	}
	
}
