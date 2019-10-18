
package ml.ikslib.gateway.message;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ml.ikslib.gateway.Service;

public class OutboundEncryptedMessage extends OutboundBinaryMessage
{
	private static final long serialVersionUID = 1L;

	public OutboundEncryptedMessage(MsIsdn recipientAddress, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException
	{
		super(recipientAddress, data);
		setPayload(new Payload(Service.getInstance().getKeyManager().encrypt(recipientAddress, data)));
	}

	public OutboundEncryptedMessage(String recipientAddress, byte[] data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException
	{
		this(new MsIsdn(recipientAddress), data);
	}
}
