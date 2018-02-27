package server;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import server.model.User;

/**
* <h1>Web Application Constants</h1>
* This class is simply a String interface use to interact with the user upon wrong input, and other invalid actions
* <p>
* It serves no function, but makes the functional classes shorter and easier on the eyes
* </p>
*
* @author  Sergey
* @author  Amit
* @version 1.0
*/
public interface AppConstants {

	//app constants
	public final Type USER_COLLECTION = new TypeToken<Collection<User>>() {}.getType();
	public final String NAME = "name";
	public final String USERNAME = "username";
	public final String BOOKS = "books";
	public final String ALLBOOKS = "allBooks";
	public final String MYBOOKS = "myBooks";
	public final String NOTMYBOOKS = "notMyBooks";
	public final String CLOSEBOOK = "closeBook";
	public final String LIKEBOOK = "likeBook";
	public final String UNLIKEBOOK = "unlikeBook";
	public final String SENDREVIEW = "sendReview";
	public final String APPROVE_REVIEW = "approveReview";
	public final String REJECT_REVIEW = "rejectReview";
	public final String REMOVE_USER = "removeUser";
	public final String BUYNEWBOOK = "buyNewBook";
	public final String VALIDATECREDIT = "validateCredit";
	public final String ALLUSERS = "allUsers";
	public final String RECENT_TRANSACTIONS = "recentTransactions";
	public final String REVIEW_REQUSTS = "reviewRequsts";
	public final String SHOW_USER_INFO = "showUserInfo";
	
	
	public final String USERS_FILE = "users.json";
	public final String BOOKS_FILE = "books.json";
	public final String REVIEWS_FILE = "reviews.json";
	public final String BOOKS_OF_USER_FILE = "booksOfUser.json";
	public final String BOOKS_LIKED_BY_USER_FILE = "booksLikedByUser.json";
	
	//Statuses
	public final String PENDING = "pending";
	public final String APPROVED = "approved";
	public final String REJECTED = "rejected";
	
	//Validations
	public final String WARNING_EMPTY_USERNAME = "Please enter a username";
	public final String WARNING_USERNAME_EXIST = "Username already exists";
	public final String WARNING_WRONG_USERNAME = "Username can be up to 10 characters";
	public final String WARNING_EMPTY_PASSWORD = "Please enter a password";
	public final String WARNING_EMPTY_EMAIL = "Please insert an email";
	public final String WARNING_EMPTY_PHONE = "Please insert a phone number";
	public final String WARNING_LONG_PASSWORD = "Password can be up to 8 characters";
	public final String WARNING_WRONG_EMAIL = "Please insert a valid email";
	public final String WARNING_WRONG_PHONE = "Please insert a valid phone number";
	public final String WARNING_EMPTY_NICKNAME = "Please choose a username";
	public final String WARNING_LONG_DESCRIPTION = "Max description length is 50";
	
	public final String WARNING_EMPTY_STREET = "Street is mandatory";
	public final String WARNING_WRONG_STREET = "Please insert a valid street";
	public final String WARNING_WRONG_NUMBER = "Please insert a valid House Number";
	public final String WARNING_EMPTY_CITY = "City is mandatory";
	public final String WARNING_WRONG_CITY = "Please insert a valid City";
	public final String WARNING_EMPTY_ZIP = "ZIP code is mandatory";
	public final String WARNING_WRONG_ZIP= "Please insert a valid ZIP code";
	
	public final String REGISTRATION_FAIL = "Failed to register a new user";
	public final String REGISTRATION_SUCCESS= "User registered successfully";
	
	public final String WARNING_WRONG_LOGIN = "Wrong Username or Password";
	public final String LOGIN_WELCOME = "Welcome!";
	
	public final String NO_USERS_IN_DB = "The are no user registered to the site.";
	public final String NO_BOOKS_IN_DB = "There are no available books right now, try again later.";
	public final String NO_NEW_BOOKS_IN_DB = "You have all the available books.";
	public final String NO_BOOKS_FOR_USER = "You don't have books. But you can buy in the store.";
	public final String NO_TRANSACTIONS_IN_DB = "The are no transactions registered in the database.";
	public final String NO_REQUESTS_IN_DB = "The are no requests for review registered in the database.";
	
	//derby constants
	public final String DB_NAME = "DB_NAME";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";

	//Table names
	public final String USERS_TABLE = "USERS";
	public final String BOOKS_TABLE = "BOOKS";
	public final String REVIEWS_TABLE = "REVIEWS";
	public final String BOOKS_OF_USER_TABLE = "BOOKSOFUSER";
	public final String BOOKS_LIKED_BY_USER_TABLE = "BOOKSLIKEDBYUSER";
	
	//sql SELECT statements
	public final String SELECT_ALL_USERS = "SELECT * FROM USERS";
	public final String SELECT_ALL_BOOKS = "SELECT * FROM BOOKS";
	public final String SELECT_ALL_REVIEWS = "SELECT * FROM REVIEWS ORDER BY date DESC";
	public final String SELECT_ALL_BOOKSOFUSER = "SELECT * FROM BOOKSOFUSER ORDER BY dateOfPurchase DESC";
	public final String SELECT_USERS_BY_USERNAME = "SELECT * FROM USERS WHERE username=?";
	public final String SELECT_A_USER_BY_USERID = "SELECT * FROM USERS WHERE id=?";
	public final String SELECT_USER_BY_LOGIN_STMT = "SELECT * FROM USERS WHERE username=? AND password=?";
	public final String SELECT_USER_NICKNAME_BY_ID = "SELECT nickname FROM USERS WHERE id=?";
	public final String SELECT_BOOK_NAME_AND_PRICE_BY_BOOKID = "SELECT name,price FROM BOOKS WHERE id=?";
	public final String SELECT_BOOKSOFUSER_BY_USERID = "SELECT * FROM BOOKSOFUSER WHERE userId=?";
	public final String SELECT_BOOKS_BY_BOOKID = "SELECT * FROM BOOKS WHERE id=?";
	public final String SELECT_APPROVED_REVIEWS_BY_BOOKID = "SELECT * FROM REVIEWS WHERE bookId=? AND status='approved'";
	public final String SELECT_APPROVED_REVIEWS_BY_BOOKID_AND_USERID = "SELECT * FROM REVIEWS WHERE bookId=? AND userId=? AND status='approved'";
	public final String SELECT_LIKES_BY_BOOKID = "SELECT * FROM BOOKSLIKEDBYUSER WHERE bookId=?";
	
	//sql INSERT statements
	public final String INSERT_NEW_USER = "INSERT INTO USERS(Username, Email, Phone, Password, Nickname, Description, PhotoURL, Role, AddrStreet, AddrNumber, AddrCity, AddrZip) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	public final String INSERT_NEW_USER_WITH_ID = "INSERT INTO USERS(Username, Email, Phone, Password, Nickname, Description, PhotoURL, Role, AddrStreet, AddrNumber, AddrCity, AddrZip, Id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public final String INSERT_NEW_BOOK = "INSERT INTO BOOKS(Name, Path, ImageUrl, Description, Price, Likes, Reviews) VALUES(?,?,?,?,?,?,?)";
	public final String INSERT_NEW_BOOK_WITH_ID = "INSERT INTO BOOKS(Name, Path, ImageUrl, Description, Price, Likes, Reviews, Id) VALUES(?,?,?,?,?,?,?,?)";
	public final String INSERT_NEW_REVIEW = "INSERT INTO REVIEWS(bookId, userId, score, content, date, status) VALUES(?,?,?,?,CURRENT_TIMESTAMP,?)";
	public final String INSERT_NEW_BOOK_OF_USER = "INSERT INTO BOOKSOFUSER(bookId, userId, isOpen, isLiked, isReviewed, scrollLocation, dateOfPurchase) VALUES(?,?,?,?,?,?,CURRENT_TIMESTAMP)";
	public final String INSERT_NEW_BOOK_LIKED_BY_USER = "INSERT INTO BOOKSLIKEDBYUSER(bookId, userId) VALUES(?,?)";
	
	
	//sql UPDATE statements
	public final String SET_REVIEW_AS_APPROVED = "UPDATE REVIEWS SET status='approved' WHERE bookId=? AND userId=?";
	public final String SET_REVIEW_AS_REJECTED = "UPDATE REVIEWS SET status='rejected' WHERE bookId=? AND userId=?";
	public final String UPDATE_SCROLL_LOCATION = "UPDATE BOOKSOFUSER SET scrollLocation=? WHERE bookId=? AND userId=?";
	public final String UPDATE_BOOK_OF_USER_ISOPEN = "UPDATE BOOKSOFUSER SET isOpen=true WHERE bookId=? AND userId=?";
	public final String LIKE_A_BOOK = "UPDATE BOOKS SET likes = likes + 1 WHERE id=?";
	public final String UNLIKE_A_BOOK = "UPDATE BOOKS SET likes = likes - 1 WHERE id=?";
	public final String SET_BOOK_OF_USER_AS_LIKED = "UPDATE BOOKSOFUSER SET isLiked = true WHERE bookId=? AND userId=?";
	public final String SET_BOOK_OF_USER_AS_UNLIKED = "UPDATE BOOKSOFUSER SET isLiked = false WHERE bookId=? AND userId=?";
	public final String SET_BOOK_OF_USER_AS_REVIEWED = "UPDATE BOOKSOFUSER SET isReviewed = true WHERE bookId=? AND userId=?";
	public final String REVIEW_A_BOOK = "UPDATE BOOKS SET reviews = reviews + 1 WHERE id=?";
	public final String UNREVIEW_A_BOOK = "UPDATE BOOKS SET reviews = reviews - 1 WHERE id=?";
	
	//sql DELETE statements
	public final String DELETE_USER_FROM_USERS = "DELETE FROM USERS WHERE id=?";
	public final String DELETE_USER_FROM_BOOKSOFUSER = "DELETE FROM BOOKSOFUSER WHERE userId=?";
	public final String DELETE_USER_FROM_BOOKSLIKEDBYUSER = "DELETE FROM BOOKSLIKEDBYUSER USERS WHERE userId=?";
	public final String DELETE_USER_FROM_REVIEWS = "DELETE FROM REVIEWS WHERE userId=?";
	
	//sql CREATE statements	
	public final String CREATE_USERS_TABLE = "CREATE TABLE USERS(" + 
			"id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 21, INCREMENT BY 1)," +
			"username varchar(10) NOT NULL," + 
			"email varchar(80) NOT NULL," + 
			"phone varchar(20) NOT NULL," + 
			"password varchar(32) NOT NULL," + 
			"nickname varchar(20) UNIQUE," + 
			"description varchar(50)," + 
			"photoUrl varchar(500)," + 
			"role integer NOT NULL default 0," +
			"addrStreet varchar(200) NOT NULL," + 
			"addrNumber integer NOT NULL," + 
			"addrCity varchar(200) NOT NULL," + 
			"addrZip varchar(200) NOT NULL," + 
			"PRIMARY KEY(id))";
	public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS(" + 
			"id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 11, INCREMENT BY 1)," +
			"name varchar(200) NOT NULL," + 
			"path varchar(500) NOT NULL," + 
			"imageUrl varchar(500)," +
			"price real NOT NULL," + 
			"description varchar(50)," + 
			"likes integer default 0," +
			"reviews integer default 0," + 
			"PRIMARY KEY(id))";
	public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS(" + 
			"bookId integer NOT NULL," + 
			"userId integer NOT NULL," + 
			"score integer NOT NULL," + 
			"content varchar(2000) NOT NULL," + 
			"date timestamp," + 
			"status varchar(20) default 'pending'," + 
			"FOREIGN KEY(userId) REFERENCES USERS (id)," + 
			"FOREIGN KEY(bookId) REFERENCES BOOKS (id))";
	public final String CREATE_BOOKS_OF_USER_TABLE = "CREATE TABLE BOOKSOFUSER(" + 
			"bookId integer NOT NULL," + 
			"userId integer NOT NULL," + 
			"dateOfPurchase timestamp," +
			"isOpen boolean default false," + 
			"isLiked boolean default false," +
			"isReviewed boolean default false," +
			"scrollLocation integer," +
			"FOREIGN KEY(userId) REFERENCES USERS (id)," + 
			"FOREIGN KEY(bookId) REFERENCES BOOKS (id))";
	public final String CREATE_BOOKS_LIKED_BY_USER_TABLE = "CREATE TABLE BOOKSLIKEDBYUSER(" + 
			"bookId integer NOT NULL," + 
			"userId integer NOT NULL," + 
			"FOREIGN KEY(userId) REFERENCES USERS (id)," + 
			"FOREIGN KEY(bookId) REFERENCES BOOKS (id))";
}
