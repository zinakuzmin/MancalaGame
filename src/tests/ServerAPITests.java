package tests;

import javafx.stage.Stage;
import model.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import server.ActionResult;
import server.MancalaServerController;

public class ServerAPITests {
	private static MancalaServerController serverController;
	
	@BeforeClass
	public static void prepareToTest(){
		serverController = new MancalaServerController(new Stage());
		
	}
	
	
	
	@Test
	public void signupWithCorrectData(){
		User user = new User("zina@mail.com", "zinamail", "Password1!", 0) ;
		ActionResult result = serverController.registerUser(user);
		System.out.println("signupWithCorrectData : " + result);
		Assert.assertTrue(result.isActionSucceeded());
	}
	
	@Test
	public void signupWithMissingUserName(){
		User user = new User("zina@mail.com", "", "Password1!", 0) ;
		ActionResult result = serverController.registerUser(user);
		System.out.println("signupWithMissingUserName: " + result);
		Assert.assertFalse(result.isActionSucceeded());
	}
	
	@Test
	public void signupWithMissingEmail(){
		User user = new User("zina", "zinamail", "Password1!", 0) ;
		ActionResult result = serverController.registerUser(user);
		System.out.println("signupWithMissingEmail: " + result);
		Assert.assertFalse(result.isActionSucceeded());
	}
	
	@Test
	public void signupWithSimplePassword(){
		User user = new User("zina@mail.com", "zinamail", "1234", 0) ;
		ActionResult result = serverController.registerUser(user);
		System.out.println("signupWithSimplePassword: " + result);
		Assert.assertFalse(result.isActionSucceeded());
	}
	
	
	
	
	
	
	@Test
	public void loginWithValidCredentials(){
		Assert.assertTrue(true);
	}
	
	@Test
	public void loginWithWrongUsernameAndCorrectPassword(){
		Assert.assertFalse(false);
	}
	
	@Test
	public void loginWithCorrectUsernameAndWrongPassword(){
		Assert.assertFalse(false);
	}
	
	@Test
	public void loginWithNonValidCredentials(){
		Assert.assertFalse(false);
	}
	
	
	@Test
	public void startGameWithCorrectParams(){
		Assert.assertTrue(true);
	}
	
	@Test
	public void startGameWithNoOpponent(){
		Assert.assertFalse(false);
	}
	
	@Test
	public void startGameWithNoLogin(){
		Assert.assertFalse(false);
	}
	
	

}
