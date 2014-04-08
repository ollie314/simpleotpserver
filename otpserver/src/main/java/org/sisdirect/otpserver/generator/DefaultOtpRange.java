package org.sisdirect.otpserver.generator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.sisdirect.otpserver.etc.OtpConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class define a generator
 * 
 * @author mlefebvre
 * @version 0.0.1
 */
public class DefaultOtpRange {
	
	/**
	 * Otp generator
	 */
	@Autowired
	private OtpGenerator otp;
	
	/**
	 * Configuration of the opt
	 */
	@Autowired
	private OtpConfig config;

	/**
	 * delta of the range in seconds
	 */
	private int delta;

	/**
	 * List of valid tokens according to delta.
	 */
	private List<String> validTokens;
	/**
	 * Secret key for the token to
	 */
	private String secret;

	/**
	 * Default constructor
	 */
	public DefaultOtpRange() {
		super();
		validTokens = new ArrayList<String>();
	}

	/**
	 * Default constructor
	 * 
	 * @param config : configuration to use within the current range generator
	 */
	public DefaultOtpRange(final OtpConfig config) {
		this();
		this.config = config;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getValidTokens() {
		return validTokens;
	}

	/**
	 * Return the list of current valid tokens.
	 * 
	 * @param validTokens
	 */
	public void setValidTokens(List<String> validTokens) {
		this.validTokens = validTokens;
	}

	/**
	 * 
	 * @return
	 */
	public int getDelta() {
		return delta;
	}

	/**
	 * 
	 * @param delta
	 */
	public void setDelta(int delta) {
		this.delta = delta;
	}

	/**
	 * 
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * 
	 * @param secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * 
	 * @return
	 */
	public OtpConfig getConfig() {
		return config;
	}

	/**
	 * 
	 * @param config
	 */
	public void setConfig(OtpConfig config) {
		this.config = config;
	}

	/**
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * 
	 */
	public void generateDelta() throws InvalidKeyException,
			NoSuchAlgorithmException {
		int i = -delta;
		int codeDigits = config.getCodeDigits();
		boolean addChecksum = config.getCheckSumPolicy();
		int truncationOffset = config.getTruncationOffset();
		for (; i < delta; i++) {
			long movingFactor = otp.getUnixTimestamp() + i;
			final String tok = otp.generateOTP(secret.getBytes(), movingFactor,
					codeDigits, addChecksum, truncationOffset);
			validTokens.add(tok);
		}
	}

	/**
	 * 
	 * @param delta
	 */
	public void generateDelta(int delta) throws InvalidKeyException,
			NoSuchAlgorithmException {
		generateDelta(delta, false);
	}

	/**
	 * 
	 * @param delta
	 * @param preserveLast
	 */
	public void generateDelta(int delta, Boolean preserveLast)
			throws InvalidKeyException, NoSuchAlgorithmException {
		int lastDelta = this.delta;
		this.delta = delta;
		generateDelta();
		if (preserveLast) {
			this.delta = lastDelta;
		}
	}

	/**
	 * 
	 */
	public void resetResults() {
		validTokens.clear();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getResults() {
		return validTokens;
	}

	/**
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public Boolean isValid(final String token) throws Exception {
		if (validTokens.size() == 0) {
			try {
				generateDelta();
			} catch (Exception e) {
				throw e;
			}
		}
		return validTokens.contains(token);
	}
}
