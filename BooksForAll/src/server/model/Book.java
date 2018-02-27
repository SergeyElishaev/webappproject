/**
 * 
 */
package server.model;

import java.util.ArrayList;

/**
 * Book model
 * 
 * @author SeReGa
 * 
 */
public class Book {

	private String Name, Path, ImageUrl, Description;
	private float Price;
	private int Likes, Reviews, Id;
	private ArrayList<ReviewToShow> ReviewsList;
	private ArrayList<Like> LikesList;
	/**
	 * @param name
	 * @param path
	 * @param imageUrl
	 * @param description
	 * @param price
	 * @param likes
	 * @param reviews
	 * @param id
	 */
	public Book(String name, String path, String imageUrl, String description, float price, int likes, int reviews, int id) {
		Name = name;
		Path = path;
		ImageUrl = imageUrl;
		Description = description;
		Price = price;
		Likes = likes;
		Reviews = reviews;
		Id = id;
	}
	
	
	
	/**
	 * @param name
	 * @param path
	 * @param imageUrl
	 * @param description
	 * @param price
	 * @param likes
	 * @param reviews
	 * @param id
	 * @param reviewsList
	 * @param likesList
	 */
	public Book(String name, String path, String imageUrl, String description, float price, int likes, int reviews,
			int id, ArrayList<ReviewToShow> reviewsList, ArrayList<Like> likesList) {
		Name = name;
		Path = path;
		ImageUrl = imageUrl;
		Description = description;
		Price = price;
		Likes = likes;
		Reviews = reviews;
		Id = id;
		ReviewsList = reviewsList;
		LikesList = likesList;
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
	 * @return the reviewsList
	 */
	public ArrayList<ReviewToShow> getReviewsList() {
		return ReviewsList;
	}


	/**
	 * @param reviewsList the reviewsList to set
	 */
	public void setReviewsList(ArrayList<ReviewToShow> reviewsList) {
		ReviewsList = reviewsList;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return Path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		Path= path;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return ImageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
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
	 * @return the price
	 */
	public float getPrice() {
		return Price;
	}
	/**
	 * @return the price
	 */
	public String getPriceAsString() {
		return String.valueOf(Price);
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		Price = price;
	}
	/**
	 * @return the likes
	 */
	public int getLikes() {
		return Likes;
	}
	/**
	 * @return the likes as String
	 */
	public String getLikesAsString() {
		return new Integer(Likes).toString();
	}
	/**
	 * @param likes the likes to set
	 */
	public void setLikes(int likes) {
		Likes = likes;
	}
	/**
	 * @return the reviews
	 */
	public int getReviews() {
		return Reviews;
	}
	/**
	 * @return the reviews as String
	 */
	public String getReviewsAsString() {
		return new Integer(Reviews).toString();
	}
	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(int reviews) {
		Reviews = reviews;
	}
	/**
	 * @return the id as String
	 */
	public String getIdAsString() {
		return new Integer(Id).toString();
	}



	/**
	 * @return the likesList
	 */
	public ArrayList<Like> getLikesList() {
		return LikesList;
	}



	/**
	 * @param likesList the likesList to set
	 */
	public void setLikesList(ArrayList<Like> likesList) {
		LikesList = likesList;
	}
}
