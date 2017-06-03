package tests;

import model.Game;
import model.User;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import server.db.DBHandler;

public class TestExamples {
	private static DBHandler db;
	private static User user1;
	private static User user2;
	private static Game game;
	
	@BeforeClass
	public static void prepareToTest(){
		db = new DBHandler();
		user1 = new User("dima@ct.com", "kakulya", "password", 100);
		user2 = new User("zina@ct.com", "kakulya-toje", "password", 50);
		
		
		
	}
	
	
	@Test
	public void myTest(){
		Assert.assertTrue(true);
	}
	
	@Test
	public void insertUserTest(){
		
		System.out.println(user1);
		User resultUser = db.insertUser(user1);
		System.out.println(resultUser);
		Assert.assertTrue(user1.getUserEmail().equals(resultUser.getUserEmail()) && user1.getUserID() == resultUser.getUserID());
	}
	
	

}
