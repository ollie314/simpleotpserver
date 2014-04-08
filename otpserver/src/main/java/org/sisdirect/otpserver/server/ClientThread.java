package org.sisdirect.otpserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

import org.sisdirect.otpserver.server.api.OtpEventListener;
import org.sisdirect.otpserver.server.api.OtpMessageEventListener;

public class ClientThread extends Thread {
	
	private enum ClientThreadCommand {
		QUIT,
		INIT,
		TOTP,
		CACK,
		UNKNOWN
	};
	
	private OtpEventListener listener;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream pstream;
	
	public ClientThread(OtpEventListener listener, Socket socket) {
		super();
		System.out.println("Client starting");
		this.listener = listener;
		this.socket = socket;
	}

	public void init() throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		pstream = new PrintStream(socket.getOutputStream());
	}
	
	private ClientThreadCommand parse(final String command) {
		if(command.length() < 4) {
			return ClientThreadCommand.UNKNOWN;
		}
		final String header = command.substring(0, 4);
		if(header.equals("QUIT")) {
			return ClientThreadCommand.QUIT;
		}
		if(header.equals("INIT")) {
			return ClientThreadCommand.INIT;
		}
		if(header.equals("TOTP")) {
			return ClientThreadCommand.TOTP;
		}
		if(header.equals("CACK")) {
			return ClientThreadCommand.CACK;
		}
		return ClientThreadCommand.UNKNOWN;
	}

	@Override
	public void run() {
		boolean shouldQuit = false;
		try {
			while (true) {
				String readed = reader.readLine();
				long timestamp = System.currentTimeMillis() / 1000L;
				
				switch (parse(readed)) {
				case QUIT:
					pstream.println("300 Bye");
					shouldQuit = true;
					break;
				case INIT:
					pstream.println("201 INIT OK");
					break;
				case TOTP:
					Random rand = new Random();
					int next = rand.nextInt();
					final String resp = (next % 2 == 0) ? "OK" : "KO";
					pstream.println(String.format("202 TOTP %s", resp));
					break;
				case CACK:
					pstream.println("200 OK");
					break;
				case UNKNOWN:
				default:
					pstream.println("100 Command not Supported");
					break;
				}
				if(shouldQuit) {
					break;
				}
			}
			pstream.println("Bye !");
			pstream.close();
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			pstream.println("Bye !");
			pstream.close();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
