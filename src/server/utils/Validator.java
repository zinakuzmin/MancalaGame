package server.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
	

	private Pattern emailPattern;
	private Pattern usernamePattern;
	private Pattern passwordPattern;
	private Matcher matcher;

	
	
	/**email address must start with “_A-Za-z0-9-\\+” , optional follow by “.[_A-Za-z0-9-]”, 
	 * and end with a “@” symbol. The email’s domain name must start with “A-Za-z0-9-“, 
	 * follow by first level Tld (.com, .net) “.[A-Za-z0-9]” and optional follow by a 
	 * second level Tld (.com.au, .com.my) “\\.[A-Za-z]{2,}”, where second level Tld must 
	 * start with a dot “.” and length must equal or more than 2 characters.**/
	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	
	/**3 to 15 characters with any lower case character, digit or special symbol “_-” only.*/
	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
	
	
	/**Whole combination is means, 6 to 20 characters string with at least one digit, 
	 * one upper case letter, one lower case letter and one special symbol (“@#$%”). 
	 * This regular expression pattern is very useful to implement a strong and complex password.*/
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

	public Validator() {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		usernamePattern = Pattern.compile(USERNAME_PATTERN);
		passwordPattern = Pattern.compile(PASSWORD_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validateEmail(final String hex) {

		matcher = emailPattern.matcher(hex);
		return matcher.matches();

	}
	
	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validateUsername(final String hex) {

		matcher = usernamePattern.matcher(hex);
		return matcher.matches();

	}
	
	/**
	 * Validate hex with regular expression
	 *
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validatePasswordComplexity(final String hex) {

		matcher = passwordPattern.matcher(hex);
		return matcher.matches();

	}
}
