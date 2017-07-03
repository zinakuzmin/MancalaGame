package tests;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import model.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import server.ActionResult;
import server.Client;
import server.ClientListener;
import server.GameController;
import server.GameStatusEnum;
import server.MancalaServerController;

public class GameTests {
	private static MancalaServerController serverController;
	private static GameController gameController;
	private static Client client1;
	private static Client client2;
	
	
	@BeforeClass
	public static void prepareToTest(){
		serverController = new MancalaServerController();
		Socket socket;
		try {
			socket = new Socket("localhost", 8000);
			ClientListener clientListener = new ClientListener(serverController);
			client1 = new Client(socket, clientListener);
			client1.setUserID(1);
			client2 = new Client(socket, clientListener);
			client2.setUserID(2);
			serverController.addOnlineClient(client1);
			serverController.addOnlineClient(client2);
			serverController.startGame(client1.getClientId(), client2.getClientId());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gameController = serverController.getRunningGames().get(0);
		gameController.setGameStatus(GameStatusEnum.inProgress);
		gameController.setPlayer1SessionID(client1.getClientId());
		gameController.setPlayer2SessionID(client2.getClientId());
		gameController.setPlayerTurn(client1.getClientId());
		
		
		
	}
	
	
	@Test
	public void makeValidMove(){
		serverController.makeMove(client1.getClientId(), client2.getClientId(), client1.getClientId(), 0);
		int[] pits = gameController.getPits();
		
		Assert.assertTrue(pits[0] == 0);
	}
	
	
	@Test
	public void makeInvalidMove(){

		Assert.assertTrue(true);
	}
	
	
	public void makeMoveLastStoneMancala(){

		Assert.assertTrue(true);
	}
	
	
	public void makeLastMoveInGame(){

		Assert.assertTrue(true);
	}
	
	public void makeMoveLastStoneOnEmptyPlace(){

		Assert.assertTrue(true);
	}

}
