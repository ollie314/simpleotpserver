package org.sisdirect.otpserver.etc;

public interface OtpConfig {
	
	int getCodeDigits();
	boolean getCheckSumPolicy();
	int getTruncationOffset();
	int getPort();
	boolean getCloseOnError();
}
