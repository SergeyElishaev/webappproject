package server;

/**
* <h1>Message Class</h1>
* <p>
* This class serves as a message template for convenience.
* </p>
* @author  Sergey
* @author  Amit
* @version 1.0
*/
public class Message {
	private Status status;
	private String details;
	private Object object;

	/**
	 * @param status
	 * @param details
	 */
	public Message(Status status, String details) {
		this.status = status;
		this.details = details;
	}
	
	
	/**
	 * @param status
	 * @param details
	 * @param object
	 */
	public Message(Status status, String details, Object object) {
		this.status = status;
		this.details = details;
		this.setObject(object);
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
}
