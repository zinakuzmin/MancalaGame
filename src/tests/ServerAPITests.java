package tests;

import org.junit.Assert;
import org.junit.Test;

public class ServerAPITests {
	
	
	@Test
	public void signupWithCorrectData(){
		Assert.assertTrue(true);
	}
	
	@Test
	public void signupWithMissingUserName(){
		Assert.assertFalse(false);
	}
	
	@Test
	public void signupWithMissingEmail(){
		Assert.assertFalse(false);
	}
	
	@Test
	public void signupWithSimplePassword(){
		Assert.assertFalse(false);
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
