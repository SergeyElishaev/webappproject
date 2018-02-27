/**
 * 
 */
package server.model;

/**
 * @author SeReGa
 *
 */
public class BookLikedByUser {
	int BookId, UserId;

	/**
	 * @param bookId
	 * @param userId
	 */
	public BookLikedByUser(int bookId, int userId) {
		BookId = bookId;
		UserId = userId;
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
	
	
}
