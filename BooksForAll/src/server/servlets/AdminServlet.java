package server.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import server.model.BookTransaction;
import server.model.Id;
import server.model.Review;
import server.model.UserLite;



/**
* <h1>Admin Servlet</h1>
* <p>
* This class contains functions that handle an Admin type user login
* </p>
*
* @author  Sergey
* @author  Amit
* @version 1.0
*/
@WebServlet(
		description = "Servlet to provide details about books", 
		urlPatterns = { 
				"/admin", 
				"/admin/allUsers",
				"/admin/recentTransactions",
				"/admin/reviewRequsts",
				"/admin/approveReview",
				"/admin/rejectReview",
				"/admin/removeUser",
				"/admin/showUserInfo"
				}
		)
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.sendError(401);
		} 
		catch (Exception  e) {
			getServletContext().log("Error while closing connection", e);
			response.sendError(500);//internal server error
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			
        	//obtain ExampleDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		String uri = request.getRequestURI();
    		PrintWriter writer = response.getWriter();
    		
    		Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    		// Data from client
			String gsonData = "";
			if (br != null) {
				gsonData = br.readLine();
			}
			String jsonData = gsonData;
			
    		if (uri.indexOf(AppConstants.ALLUSERS) != -1){
				ArrayList<UserLite> users = AppFunctions.getAllUsers(conn);
	    		
	    		if (users == null || users.isEmpty()) {
					writer.write(gson.toJson(new Message(Status.NoUsersInDB, AppConstants.NO_USERS_IN_DB)));
				} 
				else {
					writer.write(gson.toJson(new Message(Status.Success, "", users)));
				}
    		}
    		else if (uri.indexOf(AppConstants.RECENT_TRANSACTIONS) != -1){
				ArrayList<BookTransaction> trans = AppFunctions.getRecentTransactions(conn);
	    		
	    		if (trans == null || trans.isEmpty()) {
					writer.write(gson.toJson(new Message(Status.Failure, AppConstants.NO_TRANSACTIONS_IN_DB)));
				} 
				else {
					writer.write(gson.toJson(new Message(Status.Success, "", trans)));
				}
    		}
    		else if (uri.indexOf(AppConstants.REVIEW_REQUSTS) != -1){
				ArrayList<Review> reviews = AppFunctions.getReviewRequests(conn);
	    		
	    		if (reviews == null || reviews.isEmpty()) {
					writer.write(gson.toJson(new Message(Status.Failure, AppConstants.NO_REQUESTS_IN_DB)));
				} 
				else {
					writer.write(gson.toJson(new Message(Status.Success, "", reviews)));
				}
    		}
    		else if (uri.indexOf(AppConstants.APPROVE_REVIEW) != -1){ 
    			Review review = gson.fromJson(jsonData, Review.class);
    			
    			if(AppFunctions.performApproveReview(conn, review)){
    				writer.write(gson.toJson(new Message(Status.Success, "Review approved")));
    			}
    			else {
    				writer.write(gson.toJson(new Message(Status.Failure, "Review could not be approved")));
    			}
    			
    		}
    		else if (uri.indexOf(AppConstants.REJECT_REVIEW) != -1){ 
    			Review review = gson.fromJson(jsonData, Review.class);
    			
    			if(AppFunctions.performRejectReview(conn, review)){
    				writer.write(gson.toJson(new Message(Status.Success, "Review rejected")));
    			}
    			else {
    				writer.write(gson.toJson(new Message(Status.Failure, "Review could not be rejected")));
    			}
    			
    		}
    		else if (uri.indexOf(AppConstants.REMOVE_USER) != -1){ 
    			Id id = gson.fromJson(jsonData, Id.class);
    			
    			if(AppFunctions.performRemoveUser(conn, id.getValue())){
    				writer.write(gson.toJson(new Message(Status.Success, "User removed")));
    			}
    			else {
    				writer.write(gson.toJson(new Message(Status.Failure, "User could not be removed")));
    			}
    			
    		}
    		else if (uri.indexOf(AppConstants.SHOW_USER_INFO) != -1){ 
    			Id id = gson.fromJson(jsonData, Id.class);
    			UserLite userInfo = AppFunctions.performGetUserInfo(conn, id.getValue());
    			
    			if(userInfo != null){
    				writer.write(gson.toJson(new Message(Status.Success, "User info", userInfo)));
    			}
    			else {
    				writer.write(gson.toJson(new Message(Status.Failure, "User could not be removed")));
    			}
    			
    		}
			writer.close();
			conn.close();
    	} 
		catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
