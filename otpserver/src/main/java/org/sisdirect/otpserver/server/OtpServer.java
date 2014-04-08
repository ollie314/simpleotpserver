package org.sisdirect.otpserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.sisdirect.otpserver.etc.OtpConfig;
import org.sisdirect.otpserver.server.api.OtpConnectionEventListener;
import org.sisdirect.otpserver.server.api.OtpEventListener;
import org.sisdirect.otpserver.server.api.OtpMessageEventListener;
import org.sisdirect.otpserver.server.api.OtpObservable;
import org.sisdirect.otpserver.server.api.OtpServerEventListener;

public class OtpServer implements OtpObservable, OtpEventListener {

	private OtpConfig config;
	private List<OtpEventListener> listeners;

	public OtpServer(final OtpConfig config) {
		super();
		this.config = config;
		listeners = new ArrayList<OtpEventListener>();
	}

	public void addObserver(OtpEventListener observer) {
		if (!listeners.contains(observer)) {
			listeners.add(observer);
		}

	}

	public void removeOserver(OtpEventListener observer) {
		if (listeners.contains(observer)) {
			listeners.remove(observer);
		}
	}

	public void trigger(OtpObserverEvent eventKind, Object event) {

		for (OtpEventListener l : listeners) {
			if ((l.getClass().equals(OtpMessageEventListener.class))
					&& (eventKind == OtpObserverEvent.ACK || eventKind == OtpObserverEvent.MESSAGE)) {
				if (eventKind == OtpObserverEvent.ACK) {
					((OtpMessageEventListener) l).onAck(event);
				} else {
					((OtpMessageEventListener) l).onMessage(event);
				}
				break;
			}
			if (l.getClass().equals(OtpServerEventListener.class)
					&& (eventKind == OtpObserverEvent.STARTING
							|| eventKind == OtpObserverEvent.STARTED
							|| eventKind == OtpObserverEvent.STOPPED || eventKind == OtpObserverEvent.STOPPING)) {
				if (eventKind == OtpObserverEvent.STARTING) {
					((OtpServerEventListener) l).onConnectionStarting(event);
				}
				if (eventKind == OtpObserverEvent.STARTED) {
					((OtpServerEventListener) l).onConnectionStarted(event);
				}
				if (eventKind == OtpObserverEvent.STOPPED) {
					((OtpServerEventListener) l).onConnectionStopped(event);
				}
				if (eventKind == OtpObserverEvent.STOPPING) {
					((OtpServerEventListener) l).onConnectionStopping(event);
				}
				break;
			}
			if (l.getClass().equals(OtpConnectionEventListener.class)
					&& (eventKind == OtpObserverEvent.CONNECTED
							|| eventKind == OtpObserverEvent.CONNECTING
							|| eventKind == OtpObserverEvent.DISCONNECTED || eventKind == OtpObserverEvent.DISCONNECTING)) {
				if (eventKind == OtpObserverEvent.CONNECTED) {
					((OtpConnectionEventListener) l).onConnected(event);
				}
				if (eventKind == OtpObserverEvent.CONNECTING) {
					((OtpConnectionEventListener) l).onConnecting(event);
				}
				if (eventKind == OtpObserverEvent.DISCONNECTED) {
					((OtpConnectionEventListener) l).onDisconnected(event);
				}
				if (eventKind == OtpObserverEvent.DISCONNECTING) {
					((OtpConnectionEventListener) l).onDisconnecting(event);
				}
				break;
			}
		}
	}

	public void start() {
		boolean closeOnError = config.getCloseOnError();
		try {
			ServerSocket serverSocket = new ServerSocket(config.getPort());
			System.out.println("Server start listening");
			try {
				while (true) {
					Socket socket = serverSocket.accept();
					System.out.println("Connected");
					ClientThread thread = new ClientThread(this, socket);
					thread.init();
					thread.start();
				}
			} catch (Exception e) {
				System.out.println(String.format(
						"Error occured on the server main loop with message [%s]",
						e.getMessage()));
			} finally {
				if(closeOnError) {
					System.out.println("Closing socket");
					serverSocket.close();
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
