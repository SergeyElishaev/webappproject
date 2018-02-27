/**
 * 
 */
package server.model;

/**
 * 
 * User model 
 * @author SeReGa
 *
 */
public class User {

	private String Username, Email, Phone, Password, Nickname, Description, PhotoURL, AddrStreet, AddrCity, AddrZip;
	private int AddrNumber, Role, Id;
	
	

	/**
	 * @param username
	 * @param email
	 * @param phone
	 * @param password
	 * @param nickname
	 * @param description
	 * @param photoURL
	 * @param addrStreet
	 * @param addrCity
	 * @param addrZip
	 * @param addrNumber
	 * @param role
	 * @param id
	 */
	public User(String username, String email, String phone, String password, String nickname, String description,
			String photoURL, String addrStreet, String addrCity, String addrZip, int addrNumber, int role, int id) {
		Username = username;
		Email = email;
		Phone = phone;
		Password = password;
		Nickname = nickname;
		Description = description;
		PhotoURL = photoURL;
		AddrStreet = addrStreet;
		AddrCity = addrCity;
		AddrZip = addrZip;
		AddrNumber = addrNumber;
		Role = role;
		Id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return Phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		Phone = phone;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return Nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * @return the photoURL
	 */
	public String getPhotoURL() {
		return PhotoURL;
	}

	/**
	 * @param photoURL the photoURL to set
	 */
	public void setPhotoURL(String photoURL) {
		PhotoURL = photoURL;
	}

	/**
	 * @return the addrStreet
	 */
	public String getAddrStreet() {
		return AddrStreet;
	}

	/**
	 * @param addrStreet the addrStreet to set
	 */
	public void setAddrStreet(String addrStreet) {
		AddrStreet = addrStreet;
	}

	/**
	 * @return the addrCity
	 */
	public String getAddrCity() {
		return AddrCity;
	}

	/**
	 * @param addrCity the addrCity to set
	 */
	public void setAddrCity(String addrCity) {
		AddrCity = addrCity;
	}

	/**
	 * @return the addrZip
	 */
	public String getAddrZip() {
		return AddrZip;
	}

	/**
	 * @param addrZip the addrZip to set
	 */
	public void setAddrZip(String addrZip) {
		AddrZip = addrZip;
	}

	/**
	 * @return the addrNumber
	 */
	public int getAddrNumber() {
		return AddrNumber;
	}

	/**
	 * @param addrNumber the addrNumber to set
	 */
	public void setAddrNumber(int addrNumber) {
		AddrNumber = addrNumber;
	}

	/**
	 * @return the role
	 */
	public int getRole() {
		return Role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(int role) {
		Role = role;
	}

	/**
	 * @return the role as a String
	 */
	public String getRoleAsString() {
		return new Integer(Role).toString();
	}
	/**
	 * @return the AddrNumber as a String
	 */
	public String getAddrNumberAsString() {
		return new Integer(AddrNumber).toString();
	}
	/**
	 * @return the id as String
	 */
	public String getIdAsString() {
		return new Integer(Id).toString();
	}
	/**
	 * @return the Lite version of User
	 */
	public UserLite getLite() {
		return new UserLite(Username, Email, Phone, Nickname, Description, PhotoURL, AddrStreet, AddrCity, AddrZip, AddrNumber, Role, Id);
	}


}
