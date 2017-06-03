package server.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;

/**
 * run a sql script for building database tables and inserting initial data
 * 
 * @author Zina K
 *
 */

public class DBBuilder {

	/**
	 * run the sql script MancalaSchema.txt
	 * 
	 * 
	 * CREATE User 'scott' IDENTIFIED BY 'tiger'; 
	 * GRANT ALL PRIVILEGES ON * . * TO 'scott' WITH GRANT OPTION;
	 * 
	 */
	public static void runScript() {
		String sqlScript = "MancalaSchema.txt";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 new
			 ScriptRunner(DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false",
			 "scott", "tiger"))
			 .runScript(new BufferedReader(new FileReader(sqlScript)));
		
		} catch (Exception e) {
			System.err.println(e);
		}

	}

}
