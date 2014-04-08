package org.sisdirect.otpserver.etc;

/**
 * 
 * @author mlefebvre
 *
 */
public interface OtpConfig {
	
	int getCodeDigits();
	boolean getCheckSumPolicy();
	int getTruncationOffset();
	int getPort();
	boolean getCloseOnError();
}
