package server.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

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
import server.model.LoginData;
import server.model.User;

/**
* <h1>Login Servlet</h1>
* <p>
* This class contains functions that handle Login process
* </p>
*
* @author  Sergey
* @author  Amit
* @version 1.0
*/
@WebServlet({ "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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

    		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			PrintWriter writer = response.getWriter();
    		
			// Data from client
			String gsonData = "";
			if (br != null) {
				gsonData = br.readLine();
			}
			String jsonData = gsonData;
			
			Gson gson = new Gson();
			LoginData login = gson.fromJson(jsonData, LoginData.class);
			
			String username = login.getUsername();
			String password = login.getPassword();
    		
    		if (username == null || username.trim().isEmpty()) {
				writer.write(gson.toJson(new Message(Status.LoginFailure, AppConstants.WARNING_EMPTY_USERNAME)));
				return;
			} 
    		if (password == null || password.trim().isEmpty()) {
    			writer.write(gson.toJson(new Message(Status.LoginFailure, AppConstants.WARNING_EMPTY_PASSWORD)));
				return;
			}
			
    		User user = null;
			if ((user = AppFunctions.getUserByLogin(conn, username, password)) != null) {
				writer.write(gson.toJson(new Message(Status.LoginSuccess, AppConstants.LOGIN_WELCOME, user.getLite())));
			} 
			else {
				writer.write(gson.toJson(new Message(Status.LoginFailure, AppConstants.WARNING_WRONG_LOGIN)));
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
