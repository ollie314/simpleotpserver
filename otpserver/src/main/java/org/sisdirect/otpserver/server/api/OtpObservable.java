package org.sisdirect.otpserver.server.api;

public interface OtpObservable {

	public enum OtpObserverEvent {
		STARTING,
		STARTED,
		CONNECTING,
		CONNECTED,
		DISCONNECTING,
		DISCONNECTED,
		STOPPING,
		STOPPED,
		MESSAGE,
		ACK
	}

	void addObserver(OtpEventListener observer);
	void removeOserver(OtpEventListener observer);
	void trigger(OtpObserverEvent eventKind, Object event);
}
