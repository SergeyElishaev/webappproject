package server.listeners;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import server.AppConstants;
import server.model.Book;
import server.model.BookLikedByUser;
import server.model.BookOfUser;
import server.model.Review;
import server.model.User;

/**
 * Application Lifecycle Listener implementation class Initializer
 *
 */
@WebListener
public class Initializer implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public Initializer() { }

    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
    
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
   	 
        //shut down database
	   	try {
	    	//obtain CustomerDB data source from Tomcat's context and shutdown
	    	Context context = new InitialContext();
	    	BasicDataSource ds = (BasicDataSource)context.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.SHUTDOWN);
	    	ds.getConnection();
	    	ds = null;
		} 
	   	catch (SQLException | NamingException e) {
			cntx.log("Error shutting down database",e);
	   	}
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
    	
    	try {
    		//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		
    		// Reset DataBase
    		// uncomment to drop all tables
			try {
    			Statement stmt = conn.createStatement();
    			DatabaseMetaData dbmd = conn.getMetaData();
    			
    			ResultSet rs = dbmd.getTables(null, null, "BOOKSOFUSER", null);
    			if(rs.next()) {
    				stmt.execute("DROP TABLE BOOKSOFUSER");
        			conn.commit();
    			}
    			rs = dbmd.getTables(null, null, "BOOKSLIKEDBYUSER", null);
    			if(rs.next()) {
        			stmt.execute("DROP TABLE BOOKSLIKEDBYUSER");
    				conn.commit();
    			}
    			rs = dbmd.getTables(null, null, "REVIEWS", null);
    			if(rs.next()) {
        			stmt.execute("DROP TABLE REVIEWS");    				
    				conn.commit();
    			}rs = dbmd.getTables(null, null, "USERS", null);
    			if(rs.next()) {
    				stmt.execute("DROP TABLE USERS");
    				conn.commit();
    			}rs = dbmd.getTables(null, null, "BOOKS", null);
    			if(rs.next()) {
    				stmt.execute("DROP TABLE BOOKS");
    				conn.commit();
    			}
    			
    			stmt.close();
			} catch (SQLException e) { }
    		// EO Reset DataBase
    		
			// Create all the needed tables and populate them
			createUsersTable(cntx, conn);
			createBooksTable(cntx, conn);
			createReviewsTable(cntx, conn);
			createBooksOfUserTable(cntx, conn);
			createBooksLikedByUserTable(cntx, conn);
			
    		//close connection
    		conn.close();

    	} 
    	catch (SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    }
    
    private void createReviewsTable(ServletContext cntx, Connection conn) throws SQLException {
    	boolean tableExists = false;
    	try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(AppConstants.CREATE_REVIEWS_TABLE);
    		conn.commit();
    		stmt.close();
		} catch (SQLException e){
			if (!(tableExists = tableAlreadyExists(e))){ // if not a 'table already exists' exception, rethrow 
				throw e;
			}
		}
		
		//if no database exist in the past - further populate its records in the table
		if (!tableExists){
            // Create users table with user data from json file
			try {
				ArrayList<Review> reviews = loadReviews(cntx.getResourceAsStream(File.separator + AppConstants.REVIEWS_FILE));
                PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_REVIEW);
                for (Review review : reviews) {
                    pstmt.setString(1, new Integer(review.getBookId()).toString());
                    pstmt.setString(2, new Integer(review.getUserId()).toString());
                    pstmt.setString(3, new Integer(review.getScore()).toString());
                    pstmt.setString(4, review.getContent());
                    pstmt.setString(5, review.getStatus());
                    
                    pstmt.executeUpdate();
                }
                conn.commit();
                pstmt.close();
			} catch (IOException | NullPointerException e) { }
		}
	}

	private void createUsersTable(ServletContext cntx, Connection conn) throws SQLException {
    	boolean tableExists = false;
    	try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(AppConstants.CREATE_USERS_TABLE);
    		conn.commit();
    		stmt.close();
		} catch (SQLException e){
			if (!(tableExists = tableAlreadyExists(e))){ // if not a 'table already exists' exception, rethrow 
				throw e;
			}
		}
		
		//if no database exist in the past - further populate its records in the table
		if (!tableExists){
            // Create users table with user data from json file
			try {
				ArrayList<User> users = loadUsers(cntx.getResourceAsStream(File.separator + AppConstants.USERS_FILE));
                PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_USER_WITH_ID);
                for (User user : users) {
                    pstmt.setString(1, user.getUsername());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getPhone());
                    pstmt.setString(4, user.getPassword());
                    pstmt.setString(5, user.getNickname());
                    pstmt.setString(6, user.getDescription());
                    pstmt.setString(7, user.getPhotoURL());
                    pstmt.setString(8, user.getRoleAsString());
                    pstmt.setString(9, user.getAddrStreet());
                    pstmt.setString(10, user.getAddrNumberAsString());
                    pstmt.setString(11, user.getAddrCity());
                    pstmt.setString(12, user.getAddrZip());
                    pstmt.setString(13, user.getIdAsString());
                    
                    pstmt.executeUpdate();
                }
                conn.commit();
                pstmt.close();
			} catch (IOException | NullPointerException e) { }
		}
    }
    
    private void createBooksTable(ServletContext cntx, Connection conn) throws SQLException {
    	boolean tableExists = false;
    	try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(AppConstants.CREATE_BOOKS_TABLE);
    		conn.commit();
    		stmt.close();
		} catch (SQLException e){
			if (!(tableExists = tableAlreadyExists(e))){ // if not a 'table already exists' exception, rethrow 
				throw e;
			}
		}
		
		//if no database exist in the past - further populate its records in the table
		if (!tableExists){
            // Create users table with user data from json file
			try {
				ArrayList<Book> books = loadBooks(cntx.getResourceAsStream(File.separator + AppConstants.BOOKS_FILE));
                PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_BOOK_WITH_ID);
                for (Book book : books) {
                    pstmt.setString(1, book.getName());
                    pstmt.setString(2, book.getPath());
                    pstmt.setString(3, book.getImageUrl());
                    pstmt.setString(4, book.getDescription());
                    pstmt.setString(5, book.getPriceAsString());
                    pstmt.setString(6, book.getLikesAsString());
                    pstmt.setString(7, book.getReviewsAsString());
                    pstmt.setString(8, book.getIdAsString());
                    
                    pstmt.executeUpdate();
                }
                conn.commit();
                pstmt.close();
			} catch (IOException | NullPointerException e) { }
		}
    }
    
    private void createBooksOfUserTable(ServletContext cntx, Connection conn) throws SQLException {
    	boolean tableExists = false;
    	try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(AppConstants.CREATE_BOOKS_OF_USER_TABLE);
    		conn.commit();
    		stmt.close();
		} catch (SQLException e){
			if (!(tableExists = tableAlreadyExists(e))){ // if not a 'table already exists' exception, rethrow 
				throw e;
			}
		}
		
		//if no database exist in the past - further populate its records in the table
		if (!tableExists){
            // Create users table with user data from json file
			try {
				ArrayList<BookOfUser> booksOfUser = loadBooksOfUser(cntx.getResourceAsStream(File.separator + AppConstants.BOOKS_OF_USER_FILE));
                PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_BOOK_OF_USER);
                for (BookOfUser bookOfUser : booksOfUser) {
                    pstmt.setInt(1, bookOfUser.getBookId());
                    pstmt.setInt(2, bookOfUser.getUserId());
                    pstmt.setBoolean(3, bookOfUser.getIsOpen());
                    pstmt.setBoolean(4, bookOfUser.getIsLiked());
                    pstmt.setBoolean(5, bookOfUser.getIsReviewed());
                    pstmt.setInt(6, bookOfUser.getScrollLocation());
                    
                    pstmt.executeUpdate();
                }
                conn.commit();
                pstmt.close();
			} catch (IOException | NullPointerException e) { }
		}
	}
    private void createBooksLikedByUserTable(ServletContext cntx, Connection conn) throws SQLException {
    	boolean tableExists = false;
    	try{
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(AppConstants.CREATE_BOOKS_LIKED_BY_USER_TABLE);
    		conn.commit();
    		stmt.close();
		} catch (SQLException e){
			if (!(tableExists = tableAlreadyExists(e))){ // if not a 'table already exists' exception, rethrow 
				throw e;
			}
		}
		
		//if no database exist in the past - further populate its records in the table
		if (!tableExists){
            // Create users table with user data from json file
			try {
				ArrayList<BookLikedByUser> booksLikedByUser = loadBooksLikedByUser(cntx.getResourceAsStream(File.separator + AppConstants.BOOKS_LIKED_BY_USER_FILE));
                PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_BOOK_LIKED_BY_USER);
                for (BookLikedByUser bookLikedByUser : booksLikedByUser) {
                    pstmt.setString(1, new Integer(bookLikedByUser.getBookId()).toString());
                    pstmt.setString(2, new Integer(bookLikedByUser.getUserId()).toString());
                    
                    pstmt.executeUpdate();
                }
                conn.commit();
                pstmt.close();
			} catch (IOException | NullPointerException e) { }
		}
	}
    
	private ArrayList<User> loadUsers(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<ArrayList<User>>(){}.getType();
		ArrayList<User> users = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return users;
	}
	
	private ArrayList<Book> loadBooks(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<ArrayList<Book>>(){}.getType();
		ArrayList<Book> books = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return books;
	}
	
	private ArrayList<Review> loadReviews(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<ArrayList<Review>>(){}.getType();
		ArrayList<Review> reviews = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return reviews;
	}
	
	private ArrayList<BookOfUser> loadBooksOfUser(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<ArrayList<BookOfUser>>(){}.getType();
		ArrayList<BookOfUser> bookOfUser = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return bookOfUser;
	}
	
private ArrayList<BookLikedByUser> loadBooksLikedByUser(InputStream is) throws IOException{
		
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		Gson gson = new Gson();
		//this is a require type definition by the Gson utility so Gson will 
		//understand what kind of object representation should the json file match
		Type type = new TypeToken<ArrayList<BookLikedByUser>>(){}.getType();
		ArrayList<BookLikedByUser> bookLikedByUser = gson.fromJson(jsonFileContent.toString(), type);
		//close
		br.close();	
		return bookLikedByUser;
	}
}
