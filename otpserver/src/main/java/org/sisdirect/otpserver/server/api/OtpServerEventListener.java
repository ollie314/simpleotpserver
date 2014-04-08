package org.sisdirect.otpserver.server.api;

public interface OtpServerEventListener extends OtpEventListener {

	void onConnectionStarting(Object event);
	void onConnectionStarted(Object event);
	void onConnectionStopping(Object event);
	void onConnectionStopped(Object event);
}
