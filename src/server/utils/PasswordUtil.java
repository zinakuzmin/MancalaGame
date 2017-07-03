package server.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PasswordUtil {
	Map<String, String> DB = new HashMap<String, String>();
	public static final String SALT = "Agile2017";
	
	
	public static void main(String args[]) {
		
		System.out.println("Encrypted password " + encryptPassword("password"));

		// login should succeed.
//		if (demo.login("john", "dummy123"))
//			System.out.println("user login successfull.");
//
//		// login should fail because of wrong password.
//		if (demo.login("john", "blahblah"))
//			System.out.println("User login successfull.");
//		else
//			System.out.println("user login failed.");
	}
	
	
	public static String encryptPassword(String password) {
		String saltedPassword = SALT + password;
		String hashedPassword = generateHash(saltedPassword);
		System.out.println("salted password " + saltedPassword);
		System.out.println("hashed password " + hashedPassword);
		return hashedPassword;
//		DB.put(username, hashedPassword);
	}

	public static Boolean isPasswordsEqual(String clearPassword, String passwordFromDB) {
		Boolean isPasswordsEqual = false;

		// remember to use the same SALT value use used while storing password
		// for the first time.
		clearPassword = SALT + clearPassword;
		String hashedPassword = generateHash(clearPassword);
		if(hashedPassword.equals(passwordFromDB))
			isPasswordsEqual = true;
		return isPasswordsEqual;
		
	}
	
	public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}

		return hash.toString();
	}
}




	

