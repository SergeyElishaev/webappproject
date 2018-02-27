/**
 * 
 */
package server.model;

/**
 * @author SeReGa
 *
 */
public class Like {
	private int UserId;
	private String UserNickName;
	/**
	 * @param userId
	 * @param userNickName
	 */
	public Like(int userId, String userNickName) {
		UserId = userId;
		UserNickName = userNickName;
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
	 * @return the userNickName
	 */
	public String getUserNickName() {
		return UserNickName;
	}
	/**
	 * @param userNickName the userNickName to set
	 */
	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}
	
	
}
