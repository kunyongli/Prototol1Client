import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Protocol1Client
{

	static int			portNo	= 11337;

	static OutputStream	outStream;
	static InputStream	inStream;

	public static void main(String[] args)
	{
		try
		{
			InetAddress ipAddress = InetAddress.getLocalHost();
			Socket socket = new Socket(ipAddress, portNo);
			outStream = socket.getOutputStream();
			inStream = socket.getInputStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			// Protocol Step 1
			byte[] message1 = new byte[18];
			outStream.write(message1);
			System.out.println("I have sent a message" + "(message1)");

			if (!(new String(message1)).equals("Connect Protocol 1"))
			{
				outStream.write(("Protocol Error. Unregonised command: ").getBytes());
				outStream.write(message1);

				return;
			}
			// Protocol Step 2
			
			byte[] serverNonce = new byte[16];
			inStream.read(serverNonce);
			System.out.println("Server Nonce: " + byteArrayToHexString(serverNonce));

		}
		catch (IOException e)
		{
			//Nothing we can do about this one
			System.out.println("See that cable on the back of your computer? Stop pulling it out: " + e);
			return;
		}
	}

	private static byte[] xorBytes(byte[] one, byte[] two)
	{
		if (one.length != two.length)
		{
			return null;
		}
		else
		{
			byte[] result = new byte[one.length];
			for (int i = 0; i < one.length; i++)
			{
				result[i] = (byte) (one[i] ^ two[i]);
			}
			return result;
		}
	}

	private static String byteArrayToHexString(byte[] data)
	{
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++)
		{
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do
			{
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	private static byte[] hexStringToByteArray(String s)
	{
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}