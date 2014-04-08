package org.sisdirect.otpserver.server;

import org.sisdirect.otpserver.etc.OtpConfig;

public class ServerApp {

	public static void main(String[] args) {
		OtpConfig config = new OtpConfig() {
			
			public int getTruncationOffset() {
				return 1;
			}
			
			public int getPort() {
				return 37215;
			}
			
			public int getCodeDigits() {
				return 6;
			}
			
			public boolean getCloseOnError() {
				return false;
			}
			
			public boolean getCheckSumPolicy() {
				return false;
			}
		};
		OtpServer server = new OtpServer(config);
		server.start();
	}

}
