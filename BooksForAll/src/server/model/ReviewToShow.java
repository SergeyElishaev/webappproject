/**
 * 
 */
package server.model;

import java.sql.Timestamp;


/**
 * @author SeReGa
 *
 */
public class ReviewToShow {
	private int BookId, UserId, Score;
	private String Content, ReviewerNickName;
	private Timestamp Date;

	/**
	 * @param bookId
	 * @param userId
	 * @param score
	 * @param content
	 * @param reviewerNickName
	 * @param date
	 */
	public ReviewToShow(int bookId, int userId, int score, String content, String reviewerNickName, Timestamp date) {
		BookId = bookId;
		UserId = userId;
		Score = score;
		Content = content;
		ReviewerNickName = reviewerNickName;
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
}
