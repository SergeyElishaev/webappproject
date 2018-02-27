/**
 * 
 */
package server.model;

/**
 * Credit Card model
 * 
 * @author SeReGa
 *
 */
public class CreditCard {
	private String FullName, CardType, CardNumber;
	private int ExpiryMonth, ExpiryYear, CVV;
	
	/**
	 * @param fullName
	 * @param cardType
	 * @param cardNumber
	 * @param expiryMonth
	 * @param expiryYear
	 * @param cVV
	 */
	public CreditCard(String fullName, String cardType, String cardNumber, int expiryMonth, int expiryYear, int cVV) {
		FullName = fullName;
		CardType = cardType;
		CardNumber = cardNumber;
		ExpiryMonth = expiryMonth;
		ExpiryYear = expiryYear;
		CVV = cVV;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return FullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		FullName = fullName;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return CardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		CardType = cardType;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return CardNumber;
	}

	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	/**
	 * @return the expiryMonth
	 */
	public int getExpiryMonth() {
		return ExpiryMonth;
	}

	/**
	 * @param expiryMonth the expiryMonth to set
	 */
	public void setExpiryMonth(int expiryMonth) {
		ExpiryMonth = expiryMonth;
	}

	/**
	 * @return the expiryYear
	 */
	public int getExpiryYear() {
		return ExpiryYear;
	}

	/**
	 * @param expiryYear the expiryYear to set
	 */
	public void setExpiryYear(int expiryYear) {
		ExpiryYear = expiryYear;
	}

	/**
	 * @return the cVV
	 */
	public int getCVV() {
		return CVV;
	}

	/**
	 * @param cVV the cVV to set
	 */
	public void setCVV(int cVV) {
		CVV = cVV;
	}
	
	
}
