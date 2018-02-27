/**
 * 
 */
package server.model;

import java.sql.Timestamp;


/**
 * Review model
 * 
 * @author SeReGa
 *
 */
public class Review {
	private int BookId, UserId, Score;
	private String Content, Status, ReviewerNickName, BookName;
	private Timestamp Date;

	/**
	 * @param bookId
	 * @param userId
	 * @param score
	 * @param content
	 * @param status
	 * @param date
	 */
	public Review(int bookId, int userId, int score, String content, String status, Timestamp date) {
		BookId = bookId;
		UserId = userId;
		Score = score;
		Content = content;
		Status = status;
		Date = date;
	}
	
	/**
	 * @override constructor without a date
	 * @param bookId
	 * @param userId
	 * @param score
	 * @param content
	 * @param status
	 */
	public Review(int bookId, int userId, int score, String content, String status) {
		BookId = bookId;
		UserId = userId;
		Score = score;
		Content = content;
		Status = status;
		Date = new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * @param bookId
	 * @param userId
	 * @param score
	 * @param content
	 * @param status
	 * @param reviewerNickName
	 * @param bookName
	 * @param date
	 */
	public Review(int bookId, int userId, int score, String content, String status, String reviewerNickName,
			String bookName, Timestamp date) {
		BookId = bookId;
		UserId = userId;
		Score = score;
		Content = content;
		Status = status;
		ReviewerNickName = reviewerNickName;
		setBookName(bookName);
		Date = date;
	}

	/**
	 * @return the bookId
	 */
	public int getBookId() {
		return BookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(int bookId) {
		BookId = bookId;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return UserId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		UserId = userId;
	}
	/**
	 * @return the title
	 */
	public int getScore() {
		return Score;
	}
	/**
	 * @param title the title to set
	 */
	public void setScore(int score) {
		Score = score;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return Content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		Content = content;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return Date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		Date = date;
	}

	/**
	 * @return the reviewerNickName
	 */
	public String getReviewerNickName() {
		return ReviewerNickName;
	}

	/**
	 * @param reviewerNickName the reviewerNickName to set
	 */
	public void setReviewerNickName(String reviewerNickName) {
		ReviewerNickName = reviewerNickName;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return BookName;
	}

	/**
	 * @param bookName the bookName to set
	 */
	public void setBookName(String bookName) {
		BookName = bookName;
	}
}
