/**
 * 
 */
package server.model;

import java.sql.Timestamp;

/**
 * @author SeReGa
 *
 */
public class BookTransaction {
	String UserNickname, BookName;
	float Price;
	private Timestamp DateOfPurchase;
	/**
	 * @param userNickname
	 * @param bookName
	 * @param price
	 * @param dateOfPurchase
	 */
	public BookTransaction(String userNickname, String bookName, float price, Timestamp dateOfPurchase) {
		UserNickname = userNickname;
		BookName = bookName;
		Price = price;
		DateOfPurchase = dateOfPurchase;
	}
	/**
	 * @return the userNickname
	 */
	public String getUserNickname() {
		return UserNickname;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname) {
		UserNickname = userNickname;
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
	/**
	 * @return the price
	 */
	public float getPrice() {
		return Price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		Price = price;
	}
	/**
	 * @return the dateOfPurchase
	 */
	public Timestamp getDateOfPurchase() {
		return DateOfPurchase;
	}
	/**
	 * @param dateOfPurchase the dateOfPurchase to set
	 */
	public void setDateOfPurchase(Timestamp dateOfPurchase) {
		DateOfPurchase = dateOfPurchase;
	}
	
	
	
}
