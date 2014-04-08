package org.sisdirect.otpserver.generator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Base otp generator.
 * This class aggregate common behavior used for opt generation process
 * 
 * @author mlefebvre
 *
 */
public abstract class BaseOtpGenerator implements OtpGenerator  {

	/**
	 * These are used to calculate the check-sum digits.
	 * 0 1 2 3 4 5 6 7 8 9
	 */
	private final int[] doubleDigits = { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };

	/**
	 * Default generator
	 */
	public BaseOtpGenerator() {
		super();
	}

	/**
	 * Obtain the current unix timestamp
	 * 
	 * @return
	 */
	public synchronized long getUnixTimestamp() {
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * 
	 */
	public byte[] hmac_sha1(byte[] keyBytes, byte[] text)
			throws NoSuchAlgorithmException, InvalidKeyException {
		// try {
		Mac hmacSha1;
		try {
			hmacSha1 = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException nsae) {
			hmacSha1 = Mac.getInstance("HMAC-SHA-1");
		}
		SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
		hmacSha1.init(macKey);
		return hmacSha1.doFinal(text);
	}

	/**
	 * Calcul the checksum for a given number using specific digits
	 * 
	 * @param num
	 * @param digits
	 * @return
	 */
	public int calcChecksum(long num, int digits) {
		boolean doubleDigit = true;
		int total = 0;
		while (0 < digits--) {
			int digit = (int) (num % 10);
			num /= 10;
			if (doubleDigit) {
				digit = doubleDigits[digit];
			}
			total += digit;
			doubleDigit = !doubleDigit;
		}
		int result = total % 10;
		if (result > 0) {
			result = 10 - result;
		}
		return result;
	}
}