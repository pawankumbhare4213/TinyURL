package com.tinyurl.util;
import java.math.BigInteger;
import java.util.HashMap;

public class Codec {

	private static final char[] charMap = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
	
	private static final HashMap<Character, Integer> revCharMap = new HashMap<>(62);
	
	static {
		for(int i = 0; i < charMap.length; i++) revCharMap.put(charMap[i], i);
	}

	// Encodes long to Base62
	public static String encode(long decoded) {
		StringBuilder sb = new StringBuilder();
		int rem = 0;
		while(true) {
			rem = (int) (decoded % charMap.length);
			sb.append(charMap[rem]);
			if(decoded == 0) break;
			decoded = decoded / charMap.length;
		}
		return sb.toString();
	}
	
	public static String encode(BigInteger decoded) {
		StringBuilder sb = new StringBuilder();
		int rem = 0;
		while(true) {
			rem = (decoded.mod(BigInteger.valueOf(charMap.length))).intValue();
			sb.append(charMap[rem]);
			if(decoded.equals(BigInteger.valueOf(0))) break;
			decoded = decoded.divide(BigInteger.valueOf(charMap.length));
		}
		return sb.toString();
	}

	// Decodes Base62 to long
	public static long decodeToLong(String endoded) {
		long decoded = 0;
		for(int i = endoded.length() - 1; i >= 0; i--) {
			char curr = endoded.charAt(i);
			decoded += (62 ^ endoded.length() - 1 - i) * revCharMap.get(curr);
		}
		return decoded;
	}
	
	public static BigInteger decodeToBigInteger(String endoded) {
		BigInteger decoded = BigInteger.ZERO;
		for(int i = endoded.length() - 1; i >= 0; i--) {
			char curr = endoded.charAt(i);
			decoded = decoded.add(BigInteger.valueOf((62 ^ endoded.length() - 1 - i) * revCharMap.get(curr)));
		}
		return decoded;
	}
}