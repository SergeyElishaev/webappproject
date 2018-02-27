package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import server.model.Book;
import server.model.BookLikedByUser;
import server.model.BookOfUser;
import server.model.BookTransaction;
import server.model.CreditCard;
import server.model.Id;
import server.model.Like;
import server.model.Review;
import server.model.ReviewToShow;
import server.model.User;
import server.model.UserLite;

/**
* <h1>Application Functions</h1>
* This class contains all the methods and functions operated by the server
* <p>
* Most of the algorithms and behind-the-scenes server evaluations are made in this class
* </p>
*
* @author  Sergey
* @author  Amit
* @version 1.0
*/


public class AppFunctions {

	private AppFunctions() { }
	
	public static User getUserByLogin(Connection conn, String username, String password) {
		User user = null;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_LOGIN_STMT);
			prepStmt.setString(1, username);
			prepStmt.setString(2, password);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) { // user was found
				user = new User(rs.getString("Username"), 
						rs.getString("Email"), 
						rs.getString("Phone"), 
						rs.getString("Password"), 
						rs.getString("Nickname"), 
						rs.getString("Description"), 
						rs.getString("PhotoUrl"), 
						rs.getString("AddrStreet"), 
						rs.getString("AddrCity"), 
						rs.getString("AddrZip"),
						rs.getInt("AddrNumber"), 
						rs.getInt("Role"),
						rs.getInt("Id")); 
			}
			rs.close();
			prepStmt.close();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static ArrayList<UserLite> getAllUsers(Connection conn) {
		ArrayList<UserLite> users = new ArrayList<UserLite>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_USERS);
	        while (rs.next()) {
	        	users.add(new UserLite(rs.getString("Username"), 
						rs.getString("Email"), 
						rs.getString("Phone"), 
						rs.getString("Nickname"), 
						rs.getString("Description"), 
						rs.getString("PhotoUrl"), 
						rs.getString("AddrStreet"), 
						rs.getString("AddrCity"), 
						rs.getString("AddrZip"),
						rs.getInt("AddrNumber"), 
						rs.getInt("Role"),
						rs.getInt("Id")));
	        }
	        rs.close();
	        stmt.close();
	        return users;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<BookTransaction> getRecentTransactions(Connection conn) {
		ArrayList<BookTransaction> trans = new ArrayList<BookTransaction>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKSOFUSER);
	        while (rs.next()) {
	        	int bookId = rs.getInt("bookId");
	        	float price = 0;
	        	String bookName = null;
	        	//get book name and price
	        	PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.SELECT_BOOK_NAME_AND_PRICE_BY_BOOKID);
				prepStmt2.setInt(1, bookId);
				ResultSet rs2 = prepStmt2.executeQuery();
				if (rs2.next()) {
					bookName = rs2.getString("name");
					price = rs2.getFloat("price");
				}
				rs2.close();
				prepStmt2.close();
	        	//get user nickname
	        	String userNickname = getUserNicknameById(conn, rs.getInt("userId"));
	        	trans.add(new BookTransaction(userNickname, 
	        			bookName, 
	        			price, 
						rs.getTimestamp("dateOfPurchase")));
	        }
	        rs.close();
	        stmt.close();
	        return trans;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Review> getReviewRequests(Connection conn){
		ArrayList<Review> reviews = new ArrayList<Review>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_REVIEWS);
			while (rs.next()) {
				int reviewerId = rs.getInt("UserId");
				int bookId = rs.getInt("BookId");
				String bookName = null;
				String reviewerNickName = null;
				
				//get book name
	        	PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.SELECT_BOOK_NAME_AND_PRICE_BY_BOOKID);
				prepStmt2.setInt(1, bookId);
				ResultSet rs2 = prepStmt2.executeQuery();
				if (rs2.next()) {
					bookName = rs2.getString("name");
				}
				rs2.close();
				prepStmt2.close();
				
				PreparedStatement prepStmt3 = conn.prepareStatement(AppConstants.SELECT_USER_NICKNAME_BY_ID);
				prepStmt3.setInt(1, reviewerId);
				ResultSet rs3 = prepStmt3.executeQuery();
				if (rs3.next()) {
					reviewerNickName = rs3.getString("nickname");
				}
				rs3.close();
				prepStmt3.close();
				
				reviews.add(new Review(bookId,
						reviewerId,
						rs.getInt("Score"),
						rs.getString("Content"),
						rs.getString("Status"),
						reviewerNickName,
						bookName,
						rs.getTimestamp("Date")));
	        }
	        rs.close();
	        stmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return reviews;
	}
	
	public static ArrayList<Book> getAllBooks(Connection conn) {
		ArrayList<Book> books = new ArrayList<Book>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS);
	        while (rs.next()) {
	        	int bookId = rs.getInt("Id");
	        	ArrayList<ReviewToShow> reviews = getReviewsListByBookId(conn, bookId);
        		ArrayList<Like> likes = getLikesListByBookId(conn, bookId);
	        	books.add(new Book(rs.getString("Name"), 
						rs.getString("Path"), 
						rs.getString("ImageUrl"), 
						rs.getString("Description"), 
						rs.getFloat("Price"), 
						rs.getInt("Likes"), 
						rs.getInt("Reviews"),
						bookId,
						reviews,
						likes
						));
	        }
	        rs.close();
	        stmt.close();
	        return books;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<BookOfUser> getBooksOfUser(Connection conn, int userId) {
		ArrayList<BookOfUser> booksOfUser = new ArrayList<BookOfUser>();
		
		String UID = new Integer(userId).toString();
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_BOOKSOFUSER_BY_USERID);
			prepStmt.setString(1, UID);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) { // book of user was found
				int bookId = rs.getInt("bookId");
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.SELECT_BOOKS_BY_BOOKID);
				prepStmt2.setInt(1, bookId);
				ResultSet rs2 = prepStmt2.executeQuery();
				while (rs2.next()) { // books found
					ArrayList<ReviewToShow> reviews = getReviewsListByBookId(conn, bookId);
	        		ArrayList<Like> likes = getLikesListByBookId(conn, bookId);
					booksOfUser.add(new BookOfUser(bookId,
							userId,
							rs2.getInt("Likes"),
							rs2.getInt("Reviews"),
							rs.getBoolean("IsOpen"),
							rs.getBoolean("IsLiked"),
							rs.getBoolean("IsReviewed"),
							rs2.getString("Name"), 
							rs2.getString("Path"), 
							rs2.getString("ImageUrl"), 
							rs2.getString("Description"),
							rs.getInt("ScrollLocation"),
							rs.getTimestamp("DateOfPurchase"),
							reviews,
							likes));
				}
				rs2.close();
				prepStmt2.close();
			}
			rs.close();
			prepStmt.close();
			
			return booksOfUser;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getUserNicknameById(Connection conn, int userId) {
		String nickname = null;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_USER_NICKNAME_BY_ID);
			prepStmt.setInt(1, userId);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				nickname = rs.getString("nickname");
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return nickname;
	}
	
	public static ArrayList<ReviewToShow> getReviewsListByBookId(Connection conn, int bookId){
		ArrayList<ReviewToShow> reviews = new ArrayList<ReviewToShow>();
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_APPROVED_REVIEWS_BY_BOOKID);
			prepStmt.setInt(1, bookId);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) { // reviews found
				int reviewerId = rs.getInt("UserId");
				String reviewerNickName = null;
				
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.SELECT_USER_NICKNAME_BY_ID);
				prepStmt2.setInt(1, reviewerId);
				ResultSet rs2 = prepStmt2.executeQuery();
				if (rs2.next()) {
					reviewerNickName = rs2.getString("nickname");
				}
				rs2.close();
				prepStmt2.close();
				reviews.add(new ReviewToShow(rs.getInt("BookId"),
						reviewerId,
						rs.getInt("Score"),
						rs.getString("Content"),
						reviewerNickName,
						rs.getTimestamp("Date")));
				
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return reviews;
	}
	
	public static ArrayList<Like> getLikesListByBookId(Connection conn, int bookId){
		ArrayList<Like> likes = new ArrayList<Like>();
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_LIKES_BY_BOOKID);
			prepStmt.setInt(1, bookId);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) { // likes found
				int userId = rs.getInt("UserId");
				String userNickName = null;
				
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.SELECT_USER_NICKNAME_BY_ID);
				prepStmt2.setInt(1, userId);
				ResultSet rs2 = prepStmt2.executeQuery();
				if (rs2.next()) {
					userNickName = rs2.getString("nickname");
				}
				likes.add(new Like(userId, userNickName));
				rs2.close();
				prepStmt2.close();
			}
			rs.close();
			prepStmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return likes;
	}
	
	public static ArrayList<Book> getNotMyBooks(Connection conn, int userId) {
		ArrayList<BookOfUser> bofList = getBooksOfUser(conn, userId);
		ArrayList<Book> books = new ArrayList<Book>();
		ArrayList<Id> BIdList = new ArrayList<Id>();
		
		//create list of bookIds that user have
		for (BookOfUser bof : bofList) {
			BIdList.add(new Id(bof.getBookId()));
		}

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS);
	        while (rs.next()) {
	        	int bookId = rs.getInt("Id");
	        	boolean have = false;
	        	
	        	for(Id i : BIdList) {
	        		if(i.getValue() == bookId) {
	        			have = true;
	        			break;
	        		}
	        	}
	        	// if user doesn't have this book, add to list
	        	if(!have) {
	        		ArrayList<ReviewToShow> reviews = getReviewsListByBookId(conn, bookId);
	        		ArrayList<Like> likes = getLikesListByBookId(conn, bookId);
		        	books.add(new Book(rs.getString("Name"), 
							rs.getString("Path"), 
							rs.getString("ImageUrl"), 
							rs.getString("Description"), 
							rs.getFloat("Price"), 
							rs.getInt("Likes"), 
							rs.getInt("Reviews"),
							rs.getInt("Id"),
							reviews,
							likes));
	        	}
	        }
	        rs.close();
	        stmt.close();
	        return books;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static void performCloseBook(Connection conn, BookOfUser bof) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.UPDATE_SCROLL_LOCATION);
			prepStmt.setInt(1, bof.getScrollLocation());
			prepStmt.setInt(2, bof.getBookId());
			prepStmt.setInt(3, bof.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
			
			PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.UPDATE_BOOK_OF_USER_ISOPEN);
			prepStmt2.setInt(1, bof.getBookId());
			prepStmt2.setInt(2, bof.getUserId());
			prepStmt2.executeUpdate();
			prepStmt2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void performUnlikeBook(Connection conn, BookLikedByUser blbu) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SET_BOOK_OF_USER_AS_UNLIKED);
			prepStmt.setInt(1, blbu.getBookId());
			prepStmt.setInt(2, blbu.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.UNLIKE_A_BOOK);
			prepStmt.setInt(1, blbu.getBookId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void performLikeBook(Connection conn, BookLikedByUser blbu) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SET_BOOK_OF_USER_AS_LIKED);
			prepStmt.setInt(1, blbu.getBookId());
			prepStmt.setInt(2, blbu.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.LIKE_A_BOOK);
			prepStmt.setInt(1, blbu.getBookId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean performReviewBook(Connection conn, Review review) {
		boolean result = true;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SET_BOOK_OF_USER_AS_REVIEWED);
			prepStmt.setInt(1, review.getBookId());
			prepStmt.setInt(2, review.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.INSERT_NEW_REVIEW);
			prepStmt.setInt(1, review.getBookId());
			prepStmt.setInt(2, review.getUserId());
			prepStmt.setInt(3, review.getScore());
			prepStmt.setString(4, review.getContent());
			prepStmt.setString(5, AppConstants.PENDING);
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static boolean performApproveReview(Connection conn, Review review) {
		boolean result = true;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SET_REVIEW_AS_APPROVED);
			prepStmt.setInt(1, review.getBookId());
			prepStmt.setInt(2, review.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.REVIEW_A_BOOK);
			prepStmt.setInt(1, review.getBookId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static boolean performRejectReview(Connection conn, Review review) {
		boolean result = true;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SET_REVIEW_AS_REJECTED);
			prepStmt.setInt(1, review.getBookId());
			prepStmt.setInt(2, review.getUserId());
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	public static boolean performRemoveUser(Connection conn, int userId) {
		boolean result = true;
		
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_BOOKSOFUSER_BY_USERID);
			prepStmt.setInt(1, userId);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				int bookId = rs.getInt("bookId");
				
				if(rs.getBoolean("isLiked")) {
					try {
						PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.UNLIKE_A_BOOK);
						prepStmt2.setInt(1, bookId);
						prepStmt2.executeUpdate();
						prepStmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
						result = false;
					}
				}
				
				if(rs.getBoolean("isReviewed")) {
					PreparedStatement prepStmt3 = conn.prepareStatement(AppConstants.SELECT_APPROVED_REVIEWS_BY_BOOKID_AND_USERID);
					prepStmt3.setInt(1, bookId);
					prepStmt3.setInt(2, userId);
					ResultSet rs3 = prepStmt3.executeQuery();
					if (rs3.next()) {
						try {
							PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.UNREVIEW_A_BOOK);
							prepStmt2.setInt(1, bookId);
							prepStmt2.executeUpdate();
							prepStmt2.close();
						} catch (SQLException e) {
							e.printStackTrace();
							result = false;
						}
					prepStmt3.close();
					}
				}
			}
			
			try {
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.DELETE_USER_FROM_BOOKSOFUSER);
				prepStmt2.setInt(1, userId);
				prepStmt2.executeUpdate();
				prepStmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
			
			try {
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.DELETE_USER_FROM_BOOKSLIKEDBYUSER);
				prepStmt2.setInt(1, userId);
				prepStmt2.executeUpdate();
				prepStmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
			
			try {
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.DELETE_USER_FROM_REVIEWS);
				prepStmt2.setInt(1, userId);
				prepStmt2.executeUpdate();
				prepStmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
			
			try {
				PreparedStatement prepStmt2 = conn.prepareStatement(AppConstants.DELETE_USER_FROM_USERS);
				prepStmt2.setInt(1, userId);
				prepStmt2.executeUpdate();
				prepStmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				result = false;
			}
			
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	public static boolean performBuyNewBook(Connection conn, BookLikedByUser blbu) {
		boolean result = true;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.INSERT_NEW_BOOK_OF_USER);
			prepStmt.setInt(1, blbu.getBookId());
			prepStmt.setInt(2, blbu.getUserId());
			prepStmt.setBoolean(3, false);
			prepStmt.setBoolean(4, false);
			prepStmt.setBoolean(5, false);
			prepStmt.setInt(6, 0);
			prepStmt.executeUpdate();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	public static ArrayList<Message> validateCreditCard(Connection conn, CreditCard card) {
        ArrayList<Message> errorList = new ArrayList<Message>();
        String errorMsg = null;
        
        String type = card.getCardType();
        String num = card.getCardNumber();
        int year = card.getExpiryYear();
        int month = card.getExpiryMonth();
        int cvv = card.getCVV();
         
        //remove spaces and hyphens from card number
        num=num.replaceAll("\\s+","");
        num=num.replaceAll("-","");
        //get length, first and second digit of card
        int l=num.length();
        if (l==0) {
        	errorMsg = "Card number can't be empty";
        	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
        int first = num.charAt(0)-'0';
        int second = num.charAt(1)-'0';
        //make sure number is a string of digits
        for (int i=0; i<l; i++) {
            char c = num.charAt(i);
            if (!isDigit(c)) {
            	errorMsg = "Card number can contain only numbers"; 
            	errorList.add(new Message(Status.ValidationError, errorMsg));
            }
        }
         
        String type_1 = "AMEX";
        String type_2 = "VISA";
         
        /** Type & Number **/
        if (!(type.equals(type_1)||type.equals(type_2))) {
        	errorMsg = "card type is neither VISA nor AMEX";
        	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
        if (type.equals(type_2)) {
            errorMsg = "Valid length of VISA: 16 digits. First digit must be a 4.";
            if (l!=16 || first!=4)
            	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
        else {
            errorMsg = "Valid length of AMEX: 15 digits. First digit must be a 3 and second digit must be a 4";
            if (l!=15 || first!=3 || second!=4 )
            	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
         
        /** Date **/
        if ((year < 2018) && (month < 3)) {
        	errorMsg = "Past exp. date";
        	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
        	
         
        if ((year > 2099) || (month < 1) ||(month > 12)) {
        	errorMsg = "Wrong exp. date";
        	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
         
        /** CVV **/
        if (cvv<100 || cvv>999) {
        	errorMsg = "cvv should be 3 digits";
        	errorList.add(new Message(Status.ValidationError, errorMsg));
        }
     
         
        return errorList;
	}
	
	public static boolean isUsernameExist(Connection conn, String username) {
		boolean result = false;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_USERS_BY_USERNAME);
			prepStmt.setString(1, username);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) { // user exist
				result = true;
			}
			rs.close();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static UserLite performGetUserInfo(Connection conn, int userId){
		UserLite userInfo = null;
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.SELECT_A_USER_BY_USERID);
			prepStmt.setInt(1, userId);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) { // user exist
				userInfo = new UserLite(
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getString("nickname"),
						rs.getString("description"),
						rs.getString("photoURL"),
						rs.getString("addrStreet"),
						rs.getString("addrCity"),
						rs.getString("addrZip"),
						rs.getInt("addrNumber"),
						rs.getInt("role"),
						rs.getInt("id"));
			}
			rs.close();
			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	
	public static boolean registerNewUser(Connection conn, User user) {
		try {
			PreparedStatement prepStmt = conn.prepareStatement(AppConstants.INSERT_NEW_USER);
			prepStmt.setString(1, user.getUsername());
			prepStmt.setString(2, user.getEmail());
			prepStmt.setString(3, user.getPhone());
			prepStmt.setString(4, user.getPassword());
			prepStmt.setString(5, user.getNickname());
			prepStmt.setString(6, user.getDescription());
			prepStmt.setString(7, user.getPhotoURL());
			prepStmt.setString(8, user.getRoleAsString());
			prepStmt.setString(9, user.getAddrStreet());
			prepStmt.setString(10, user.getAddrNumberAsString());
			prepStmt.setString(11, user.getAddrCity());
			prepStmt.setString(12, user.getAddrZip());
			
			prepStmt.executeUpdate();
			prepStmt.close();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isDigit (char c) {
		return (c<='9' && c>='0');
	}
}
