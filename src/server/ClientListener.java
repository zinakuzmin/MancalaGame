package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ClientListener implements Runnable{
	/**
	 * Server socket accepting clients
	 */
	private ServerSocket serverSocket;

	/**
	 * Client socket
	 */
	private Socket socket;
	
	private boolean threadShouldRun;;
	
	
	private MancalaServerController serverController;
	
	public ClientListener(MancalaServerController serverController) {
		threadShouldRun = true;
		this.setServerController(serverController);
		try {
			serverSocket = new ServerSocket(8000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		new Thread(() -> {
			try {
				while (threadShouldRun) {
					socket = serverSocket.accept();
					Client client = new Client(socket, this);
					serverController.addOnlineClient(client);
					new Thread(client).start();
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}).start();

		
		
	}


	public MancalaServerController getServerController() {
		return serverController;
	}


	public void setServerController(MancalaServerController serverController) {
		this.serverController = serverController;
	}


	public boolean isThreadShouldRun() {
		return threadShouldRun;
	}


	public void setThreadShouldRun(boolean threadShouldRun) {
		this.threadShouldRun = threadShouldRun;
	}

}
