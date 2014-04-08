package org.sisdirect.otpserver.generator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author mlefebvre
 * @version 0.0.1
 * 
 */
public final class DefaultOtpGenerator extends BaseOtpGenerator {

	/**
	 * Default constructor
	 */
	public DefaultOtpGenerator() {

	}

	private final int[] DIGITS_POWER
	// 0 1 2 3 4 5 6 7 8
	= { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

	/* (non-Javadoc)
	 * @see org.sisdirect.otpserver.generator.OtpGenerator#generateOTP(byte[], long, int, boolean, int)
	 */
	@Override
	public String generateOTP(byte[] secret, long movingFactor, int codeDigits,
			boolean addChecksum, int truncationOffset)
			throws NoSuchAlgorithmException, InvalidKeyException {
		// put movingFactor value into text byte array
		String result = null;
		int digits = addChecksum ? (codeDigits + 1) : codeDigits;
		byte[] text = new byte[8];
		for (int i = text.length - 1; i >= 0; i--) {
			text[i] = (byte) (movingFactor & 0xff);
			movingFactor >>= 8;
		}

		// compute hmac hash
		byte[] hash = hmac_sha1(secret, text);

		// put selected bytes into result int
		int offset = hash[hash.length - 1] & 0xf;
		if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4))) {
			offset = truncationOffset;
		}
		int binary = ((hash[offset] & 0x7f) << 24)
				| ((hash[offset + 1] & 0xff) << 16)
				| ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

		int otp = binary % DIGITS_POWER[codeDigits];
		if (addChecksum) {
			otp = (otp * 10) + calcChecksum(otp, codeDigits);
		}
		result = Integer.toString(otp);
		while (result.length() < digits) {
			result = "0" + result;
		}
		return result;
	}
}
