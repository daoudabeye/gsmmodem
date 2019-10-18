package ml.ikslib.gateway.modem.client.api;

import java.io.Closeable;
import java.io.IOException;

public interface GsmModemClient extends Closeable {
	
	void sendCommand(String command) throws IOException, GsmModemException;
}
