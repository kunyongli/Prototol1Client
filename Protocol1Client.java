import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class Protocol1Client {
    
    static int portNo = 11337;
    static String hexKey= NOT TELLING;

    public static void main (String[] args) {
	// Listen for connections, when client connects spin off a 
	// thread to run the protocol over that connection and go 
	// back to listening for new connections
	try { 
	    
	    ServerSocket listening = new ServerSocket(portNo);
	    while (true) {
		// For each connection spin off a new protocol instance.
		Socket connection = listening.accept();
		Thread instance = new Thread(new ProtocolInstance(connection));
		instance.start();
	    }
	} catch (Exception e) {
	    System.out.println("Doh "+e);
	}
    }
    private static class ProtocolInstance implements Runnable {
    	
    	Socket myConnection;
    	boolean debug = true;
    	static Cipher decAEScipher;
    	static Cipher encAEScipher;
    	
    	public ProtocolInstance(Socket myConnection) {
    	    this.myConnection = myConnection;
    	    //Set up the cipher object
    	    Key aesKey = new SecretKeySpec(hexStringToByteArray(hexKey), "AES");
    	    try {
    		decAEScipher = Cipher.getInstance("AES");
    		decAEScipher.init(Cipher.DECRYPT_MODE, aesKey);
    		encAEScipher = Cipher.getInstance("AES");
    		encAEScipher.init(Cipher.ENCRYPT_MODE, aesKey);
    	    } catch (Exception e) {
    		System.out.println("Doh "+e);
    	    }			
    	}
    
    	public void run() {
    	    OutputStream outStream;
    	    InputStream inStream; 
    	    try {
    		outStream = myConnection.getOutputStream();
    		inStream = myConnection.getInputStream();
    	 // Protocol Step 1
    		// We should be sent the ascii for "Connect Protocol 1"
    		byte[] message1 = new byte[18];
    		inStream.read(message1);
    		if (debug) System.out.println("Got M1: "+new String(message1));
    	
    	   
    		
    		
    		
    		
    		
    		
    		
    		
    		
    	    }catch (IOException e) {
    			//Nothing we can do about this one
    			if (debug) System.out.println("See that cable on the back of your computer? Stop pulling it out: "+e);
    			return;
    		    }
    	}
    	    private static byte[] xorBytes (byte[] one, byte[] two) {
    	    	if (one.length!=two.length) {
    	    	    return null;
    	    	} else {
    	    	    byte[] result = new byte[one.length];
    	    	    for(int i=0;i<one.length;i++) {
    	    		result[i] = (byte) (one[i]^two[i]);
    	    	    }
    	    	    return result;
    	    	}
    	        }
    	        
    	        private static String byteArrayToHexString(byte[] data) { 
    	    	StringBuffer buf = new StringBuffer();
    	    	for (int i = 0; i < data.length; i++) { 
    	    	    int halfbyte = (data[i] >>> 4) & 0x0F;
    	    	    int two_halfs = 0;
    	    	    do { 
    	    		if ((0 <= halfbyte) && (halfbyte <= 9)) 
    	    		    buf.append((char) ('0' + halfbyte));
    	    		else 
    	    		    buf.append((char) ('a' + (halfbyte - 10)));
    	    		halfbyte = data[i] & 0x0F;
    	    	    } while(two_halfs++ < 1);
    	    	} 
    	    	return buf.toString();
    	        } 
    	        
    	        private static byte[] hexStringToByteArray(String s) {
    	    	int len = s.length();
    	    	byte[] data = new byte[len / 2];
    	    	for (int i = 0; i < len; i += 2) {
    	    	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
    	    				  + Character.digit(s.charAt(i+1), 16));
    	    	}
    	    	return data;
    	        }
    	        

    	        private static String secretValue() {
    	    	
    	        }
    	}
    }
