package org.sisdirect.otpserver.server.api;

public interface OtpConnectionEventListener extends OtpEventListener {

	void onConnecting(Object event);
	void onConnected(Object event);
	void onDisconnecting(Object event);
	void onDisconnected(Object event);
}
