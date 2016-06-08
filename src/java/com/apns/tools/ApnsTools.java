package com.apns.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import com.apns.model.Command;
import com.apns.model.FrameItem;
public class ApnsTools {
	
	private static String[] hexArr = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	
	public static byte[] generateData(List<FrameItem> list) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(bos);
		int frameLength = 0;
		for (FrameItem item : list) {
			frameLength += 1 + 2 + item.getItemLength();
		}
		try {
			os.writeByte(Command.SEND_V2);
			os.writeInt(frameLength);
			for (FrameItem item : list) {
				os.writeByte(item.getItemId());
				os.writeShort(item.getItemLength());
				os.write(item.getItemData());
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException();
	}

	public static String encodeHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(hexArr[(b >> 4) & 0x0F]);
			sb.append(hexArr[b & 0x0F]);
		}
		return sb.toString();
	}
	
	public static byte[] decodeHex(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte)((hexCharIndex(hex.charAt(2 * i)) << 4) | hexCharIndex(hex.charAt(2 * i + 1)));
		}
		return bytes;
	}
	
	/**
	 * 十六进制转换到十进制
	 * @param hex
	 * @return 0-15
	 */
	private static int hexCharIndex(char hex) {
		int index = 0;
		if (hex >= '0' && hex <= '9') {
			index = hex - '0';
		} else if (hex >= 'a' && hex <= 'f') {
			index = hex - 'a' + 10;
		} else if (hex >= 'A' && hex <= 'F') {
			index = hex - 'A' + 10;
		} else {
			throw new IllegalArgumentException("Invalid hex char. " + hex);
		}
		return index;
	}
	public static int parse4ByteInt(byte b1, byte b2, byte b3, byte b4) {
		return ((b1 << 24) & 0xFF000000) | ((b2 << 16) & 0x00FF0000) | ((b3 << 8) & 0x0000FF00) | (b4 & 0x000000FF);
	}
	public static SocketFactory createSocketFactory(InputStream keyStore, String password, 
			String keystoreType, String algorithm, String protocol) {
		try {
			char[] pwdChars = password.toCharArray();
			KeyStore ks = KeyStore.getInstance(keystoreType);
			ks.load(keyStore, pwdChars);
			KeyManagerFactory kf = KeyManagerFactory.getInstance(algorithm);
			kf.init(ks, pwdChars);
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
	        tmf.init((KeyStore)null);
	        SSLContext context = SSLContext.getInstance(protocol);
			context.init(kf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			return context.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Can't create socketFactory.");
	}
	public static byte[] intToBytes(int num, int resultBytesCount) {
		byte[] ret = new byte[resultBytesCount];
		for (int i = 0; i < resultBytesCount; i++) {
			ret[i] = (byte)((num >> ((resultBytesCount - 1 - i) * 8)) & 0xFF);
		}
		return ret;
	}
}