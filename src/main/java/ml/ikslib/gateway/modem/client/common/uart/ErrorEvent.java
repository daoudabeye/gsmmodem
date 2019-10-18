package ml.ikslib.gateway.modem.client.common.uart;

public class ErrorEvent extends GsmEvent {
	private final String error;

	public ErrorEvent(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}
}
