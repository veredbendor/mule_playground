package com.oreilly.test1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.beans.factory.InitializingBean;

public class DBInitialization implements InitializingBean {
	public void afterPropertiesSet() throws Exception {
		String dbName="productDB";
		Connection conn = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			try{
				DriverManager.getConnection(String.format("jdbc:derby:memory:%s;drop=true",dbName));
			}catch(Exception e){
				//e.printStackTrace();
			}
			// Get a connection
			conn = DriverManager.getConnection(String.format("jdbc:derby:memory:%s;create=true",dbName));
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("CREATE TABLE products (id VARCHAR(255), title VARCHAR(255), publisher VARCHAR(255),format VARCHAR(255),created TIMESTAMP,numPages NUMERIC)");
			stmt.execute("INSERT INTO products (id,title,publisher,format,created,numPages) values ('a1','Title For Book With ID a1','Publisher1','format1','2011-01-01 01:20:13.0',153)");
			stmt.execute("INSERT INTO products (id,title,publisher,format,created,numPages) values ('b1','Title For Book With ID b1','Publisher2','format2','2010-02-03 02:25:17.0',234)");
		} catch (java.sql.SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		}
	}
}
