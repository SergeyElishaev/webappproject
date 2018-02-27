package server.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;

import server.AppConstants;
import server.AppFunctions;
import server.Message;
import server.Status;
import server.model.User;

/**
* <h1>Register Servlet</h1>
* <p>
* This class contains functions that handle registration process
* </p>
*
* @author  Sergey
* @author  Amit
* @version 1.0
*/
@WebServlet("/registration")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.sendError(401);
		} catch (Exception e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);// internal server error
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// set encoding to UTF-8
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
        	
			//obtain projectDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		    PrintWriter writer = response.getWriter();
			
			// read the data sent from the client
			String gsonData = "";
			if (br != null) {
				gsonData = br.readLine();
			}

			// parse the data
			Gson gson = new Gson();
			User user = gson.fromJson(gsonData, User.class);
			ArrayList<Message> warnings = new ArrayList<Message>();
			
			// Validations
    		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_USERNAME));
			} 
    		else if (AppFunctions.isUsernameExist(conn, user.getUsername())) {
				warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_USERNAME_EXIST));
			}
    		else if (user.getUsername().length() > 10) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_USERNAME));
			}
    		if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_PASSWORD));
			}
    		else if (user.getPassword().length() > 8) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_LONG_PASSWORD));
			}
    		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_EMAIL));
			}
    		else if(!isValidEmailAddress(user.getEmail())){
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_EMAIL));
    		}
    		if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_PHONE));
			}
    		else if(!isValidPhone(user.getPhone())) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_PHONE));
			}    		
    		if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_NICKNAME));
			}
    		if (user.getDescription() != null && user.getDescription().length() > 50) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_LONG_DESCRIPTION));
			}
    		//user.getAddrCity() == null || user.getAddrZip() == null) {
    		if (user.getAddrStreet() == null || user.getAddrStreet().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_STREET));
    		}
    		else if(user.getAddrStreet().length() < 3) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_STREET));
    		}
    		if (user.getAddrNumber() < 0) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_NUMBER));
    		}
    		if (user.getAddrCity() == null || user.getAddrCity().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_CITY));
    		}
    		else if(user.getAddrCity().length() < 3) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_CITY));
    		}
    		if (user.getAddrZip() == null || user.getAddrZip().trim().isEmpty()) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_EMPTY_ZIP));
    		}
    		else if(user.getAddrZip().length() != 7) {
    			warnings.add(new Message(Status.ValidationError, AppConstants.WARNING_WRONG_ZIP));
    		}
    		
			// Finally:
    		if(warnings.isEmpty()) {
    			// prepare response to client				
				if(AppFunctions.registerNewUser(conn, user)) {
					writer.write(gson.toJson(new Message(Status.RegistrationSuccess, AppConstants.REGISTRATION_SUCCESS)));
				}
				else {
					writer.write(gson.toJson(new Message(Status.RegistrationFailure, AppConstants.REGISTRATION_FAIL)));
				}
    		}
			else {
				writer.write(gson.toJson(warnings));
    			return;
			}
        	writer.close();
			conn.close();
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);//internal server error
		}
	}

	public static int numOfDigits(String s) {
		int n=0;
		for (int i=0; i<s.length(); i++) {
			if (AppFunctions.isDigit(s.charAt(i)))
				n++;
		}
		return n;
	}
	
	public static boolean isLetter (char c) {
		return ((c<='z' && c>='a') || (c<='Z' && c>='A'));
	}
	
	public static boolean isSymbol (char c) {
		boolean result=false;
		//(ASCII: 33, 35-39, 42, 43, 45, 47, 61, 63, 94-96, 123-126)
		if (c==33 || c==42 || c==43 || c==45 || c==47 || c==61 || c==63)
			result = true;
		else if (c<=39 && c>=35)
			result = true;
		else if (c<=96 && c>=94)
			result = true;
		else if (c<=126 && c>=123)
			result = true;
		
		return result;
	}
	
	
	public static boolean isValidEmailAddress(String email) {
		   int n = email.length();
		   if (n==0 || n>256)
				return false;
		   
		   int d = 0; //number of @'s
		   int at=0;  //index of @
		   for (int i=0; i<n; i++) {
			   if (email.charAt(i)=='@') {
				   at=i;
				   d++;
			   }
		   }
		   
		   if (d!=1 || at==0 || at==n-1) //more than one @, @ at beginning or @ at end
			   return false;
		   
		   String local = email.substring(0, at);
		   String domain = email.substring(at+1, n);
		   	  	   
		   return validLocal(local) && validDomain(domain);
		}
	
	public static boolean validDomain(String domain) {
		boolean result = true;
		int n = domain.length();
		if (n==0 || n>253)
			return false;
		/*
		 * The domain name part of an email address has to conform to strict guidelines:
		 *  it must match the requirements for a hostname, consisting of letters, digits, hyphens(-) and dots.
		 */
		
		//check dot not appears twice in a row
				for (int i=0; i<n; i++) {
					if (domain.charAt(i)=='.' && i<n-1)
						if (domain.charAt(i+1)=='.')
							return false;
				}
		
		for (int i=0; i<n && result; i++) {
			char c = domain.charAt(i);
			result = (AppFunctions.isDigit(c) || isLetter(c) || (c=='-') || (c=='.'));
		}
		
		return result;
	}

	public static boolean validLocal(String local) {
		boolean result = true;
		int n = local.length();
		if (n==0 || n>64)
			return false;
	   /* Allowed characters :
	   The local-part of the email address may use any of these ASCII characters RFC 5322 Section 3.2.3:
		   ·Uppercase and lowercase English letters (a–z, A–Z) (ASCII: 65-90, 97-122)
		   ·Digits 0 to 9 (ASCII: 48-57)
		   ·Characters !#$%&'*+-/=?^_`{|}~ (ASCII: 33, 35-39, 42, 43, 45, 47, 61, 63, 94-96, 123-126)
		   ·Character . (dot, period, full stop) (ASCII: 46) provided that it is not the first or last character, and provided also that it does not appear two or more times consecutively (e.g. John..Doe@server.com is not allowed.).
	   */
		   
		//check dot not in beginning or end
		if (local.charAt(0)=='.' || local.charAt(n-1)=='.')
			return false;
		
		//check dot not appears twice in a row
		for (int i=0; i<n; i++) {
			if (local.charAt(i)=='.')
				// dot can't be in the end (already checked)
				if (local.charAt(i+1)=='.')
					return false;
		}
		
		for (int i=0; i<n && result; i++) {
			char c = local.charAt(i);
			result = (AppFunctions.isDigit(c) || isLetter(c) || isSymbol(c) || (c=='.') );
		}
		
		return result;
	}
	
	public static boolean isValidPhone(String num) {
		boolean result = true;
		
		//remove all spaces and dashes (other characters are not allowed)
		num=num.replaceAll("\\s+","");
		num=num.replaceAll("-","");
		
		int n = num.length();
		int total_digits = numOfDigits(num);
		
		//check all characters are digits
		for (int i=0; i<n; i++) {
			char c=num.charAt(i);
			if (!AppFunctions.isDigit(c))
				return false;
		}
		
		//cellphone
		if (total_digits==10) {
			if (num.charAt(0)!='0' || num.charAt(1)!='5')
				//illegal cell phone prefix
				return false;
		}
		
		//landline
		else if (total_digits==9) {
			char c1 = num.charAt(0);
			char c2 = num.charAt(1);
			if (c1!='0')
				//illegal land line prefix
				return false;
			else if (c2!='2' || c2!='3' || c2!='4' || c2!='8' || c2!='9')
				//illegal land line prefix
				return false;
		}
		
		else {
			//unknown number of digits
			result = false;
		}
		
		return result;
	}

}
