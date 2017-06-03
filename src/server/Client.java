package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import protocol.ClientDisconnectMsg;
import protocol.Message;


public class Client implements Runnable{
	private Socket socket;
	private ClientListener clientListener;
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;
	
	
	
	


	public Client(Socket socket, ClientListener clientListener) {
		setSocket(socket);
		setClientListener(clientListener);
		try {
			setStreamToClient(new ObjectOutputStream(socket.getOutputStream()));
			getStreamToClient().flush();
			setStreamFromClient(new ObjectInputStream(socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void run() {
		try {

			Message messageFromClient;
			do {
				//System.out.println("Waiting for client message");
				messageFromClient = (Message) getStreamFromClient().readObject();
				//System.out.println("Server: read message " + messageFromClient);
				new Thread(new HandleMessage(messageFromClient, this)).start();

			} while (!(messageFromClient instanceof ClientDisconnectMsg));

		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
//				 connectToClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public Socket getSocket() {
		return socket;
	}


	public void setSocket(Socket socket) {
		this.socket = socket;
	}


	public ClientListener getClientListener() {
		return clientListener;
	}


	public void setClientListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}


	public ObjectInputStream getStreamFromClient() {
		return streamFromClient;
	}


	public void setStreamFromClient(ObjectInputStream streamFromClient) {
		this.streamFromClient = streamFromClient;
	}


	public ObjectOutputStream getStreamToClient() {
		return streamToClient;
	}


	public void setStreamToClient(ObjectOutputStream streamToClient) {
		this.streamToClient = streamToClient;
	}



}
