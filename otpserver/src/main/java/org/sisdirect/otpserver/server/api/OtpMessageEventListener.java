package org.sisdirect.otpserver.server.api;

public interface OtpMessageEventListener extends OtpEventListener {

	void onMessage(Object event);
	void onAck(Object event);
}
