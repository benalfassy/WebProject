package com.servlets;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.models.Customer;
import com.utilities.AppConstants;

import javafx.util.Pair;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class CustomersServlet.
 */
public class CustomersServlet extends HttpServlet implements Closeable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The context. */
    private Context context;
    
    /** The connection. */
    private Connection connection;
    
    /**
     * Instantiates a new customers servlet.
     *
     * @throws NamingException the naming exception
     * @see HttpServlet#HttpServlet()
     */
    public CustomersServlet() throws NamingException
    {
	super();
	
	try
	{
	    context = new InitialContext();
	    
	}
	catch (NamingException e)
	{
	    e.printStackTrace();
	    throw e;
	}
    }
    
    /**
     * Open connection.
     *
     * @throws SQLException the SQL exception
     * @throws NamingException the naming exception
     */
    private void openConnection() throws SQLException, NamingException
    {
	BasicDataSource ds = (BasicDataSource) context
		.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
	
	connection = ds.getConnection();
    }
    
    /* (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close()
    {
	try
	{
	    connection.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    /**
     * Do get.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	try
	{
	    
	    Gson gson = new Gson();
	    
	    String uri = request.getRequestURI();
	    
	    System.out.println(uri);
	    
	    if (uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) != -1)
	    {
		Customer customer = null;
		
		String username = uri.substring(
			uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) + AppConstants.CUSTOMERS_RESTFULL.length());
		
		System.out.println("\n--------------------------");
		System.out.println("trying to get customer: " + username);
		System.out.println("--------------------------");
		
		PreparedStatement preparedStatement;
		
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, username);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		    return;
		}
		else
		{
		    resultSet.next();
		    
		    ArrayList<Pair<String, String>> bookScroll = gson.fromJson(resultSet.getString(14),
			    AppConstants.SCROLL_COLLECTION);
		    
		    customer = new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
			    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
			    resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
			    resultSet.getString(10), resultSet.getString(11), resultSet.getString(12),
			    resultSet.getString(13), bookScroll);
		}
		
		resultSet.close();
		preparedStatement.close();
		
		String result = gson.toJson(customer, Customer.class);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    else
	    {
		System.out.println("\n--------------------------");
		System.out.println("trying to get all customers");
		System.out.println("--------------------------");
		
		Collection<Customer> customersResult = new ArrayList<Customer>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
		
		while (rs.next())
		{
		    ArrayList<Pair<String, String>> bookScroll = gson.fromJson(rs.getString(14),
			    AppConstants.SCROLL_COLLECTION);
		    
		    customersResult.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
			    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
			    rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), bookScroll));
		}
		
		rs.close();
		
		stmt.close();
				
		String customerJsonResult = gson.toJson(customersResult, AppConstants.CUSTOMER_COLLECTION);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(customerJsonResult);
		
		writer.close();
		
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    response.sendError(500);
	}
	finally
	{
	    close();
	}
    }
    
    /**
     * Do post.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	
	System.out.println("\n--------------------------");
	System.out.println("trying add customer");
	System.out.println("--------------------------");
	
	Gson gson = new GsonBuilder().create();
	
	Customer customer = gson.fromJson(request.getReader(), Customer.class);
	
	if (!ValidateCustomer(customer))
	{
	    response.sendError(400);
	    return;
	}
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT,
		    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    pstmt.setString(1, customer.getUsername());
	    
	    ResultSet resultSet = pstmt.executeQuery();
	    
	    if (resultSet.isBeforeFirst())
	    {
		resultSet.close();
		
		pstmt.close();
		
		response.sendError(409);
		return;
	    }
	    else
	    {
		pstmt = connection.prepareStatement(AppConstants.INSERT_CUSTOMER_STMT);
		
		pstmt.setString(1, customer.getUsername());
		pstmt.setString(2, customer.getEmail());
		pstmt.setString(3, customer.getStreet());
		pstmt.setString(4, customer.getStreetNum());
		pstmt.setString(5, customer.getCity());
		pstmt.setString(6, customer.getZipCode());
		pstmt.setString(7, customer.getPhoneNum());
		pstmt.setString(8, customer.getPassword());
		pstmt.setString(9, customer.getNickName());
		pstmt.setString(10, customer.getDescription());
		pstmt.setString(11, customer.getPhoto());
		pstmt.setString(12, customer.getAffiliation());
		pstmt.setString(13, customer.getMyBooks());
		pstmt.setString(14, gson.toJson(new ArrayList<Pair<String, String>>()));
		pstmt.executeUpdate();
		
		connection.commit();
		
		resultSet.close();
		
		pstmt.close();
	    }
	    
	}
	catch (SQLException | NamingException e)
	{
	    getServletContext().log("Error on Customer post", e);
	    response.sendError(500);
	}
	finally
	{
	    close();
	}
	
    }
    
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	try
	{
	    System.out.println("\n--------------------------");
	    System.out.println("trying to update customer");
	    System.out.println("--------------------------");
	    
	    openConnection();
	    
	    Gson gson = new GsonBuilder().create();
	    
	    Customer customer = gson.fromJson(request.getReader(), Customer.class);
	    
	    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet resultSet = statement.executeQuery(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
	    
	    while (resultSet.next())
	    {
		if (resultSet.getString(1).equals(customer.getUsername()))
		{
		    resultSet.updateString(1, customer.getUsername());
		    resultSet.updateString(2, customer.getEmail());
		    resultSet.updateString(3, customer.getStreet());
		    resultSet.updateString(4, customer.getStreetNum());
		    resultSet.updateString(5, customer.getCity());
		    resultSet.updateString(6, customer.getZipCode());
		    resultSet.updateString(7, customer.getPhoneNum());
		    resultSet.updateString(8, customer.getPassword());
		    resultSet.updateString(9, customer.getNickName());
		    resultSet.updateString(10, customer.getDescription());
		    resultSet.updateString(11, customer.getPhoto());
		    resultSet.updateString(12, customer.getAffiliation());
		    resultSet.updateString(13, customer.getMyBooks());
		    resultSet.updateString(14, gson.toJson(customer.getBookScroll()));
		    resultSet.updateRow();
		}
	    }
	    
	    resultSet.close();
	    
	    statement.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    response.sendError(500);
	}
	finally
	{
	    close();
	}
    }
    
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
    {
	System.out.println("\n--------------------------");
	System.out.println("trying to delete customer");
	System.out.println("--------------------------");
	
	Gson gson = new GsonBuilder().create();
	
	String uri = request.getRequestURI();
	
	if (uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) == -1)
	{
	    response.sendError(400);
	    return;
	}
	
	String username = uri
		.substring(uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) + AppConstants.CUSTOMERS_RESTFULL.length());
	
	String nickName;
	
	System.out.println("\n--------------------------------------");
	System.out.println("trying to delete username: " + username);
	System.out.println("--------------------------------------");
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT,
		    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    pstmt.setString(1, username);
	    
	    ResultSet resultSet = pstmt.executeQuery();
	    
	    if (!resultSet.isBeforeFirst())
	    {
		resultSet.close();
		
		pstmt.close();
		
		response.sendError(404);
		return;
	    }
	    else
	    {
		resultSet.next();
		
		nickName = resultSet.getString(9);
		
		resultSet.deleteRow();
		
		resultSet.close();
		
		pstmt.close();
	    }
	    
	    System.out.println("\n--------------------------------------");
	    System.out.println("trying to delete " + username + "'s likes");
	    System.out.println("--------------------------------------");
	    
	    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet likesResultSet = statement.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
	    
	    while (likesResultSet.next())
	    {
		ArrayList<String> likesArray = gson.fromJson(likesResultSet.getString(5),
			AppConstants.ARRAYLISTSTRING_COLLECTION);
		
		likesArray.removeIf(nick -> nick.equals(nickName));
		
		likesResultSet.updateString(5, gson.toJson(likesArray));
		
		likesResultSet.updateRow();
	    }
	    
	    likesResultSet.close();
	    
	    statement.close();
	    
	    System.out.println("\n--------------------------------------");
	    System.out.println("trying to delete " + username + "'s messages");
	    System.out.println("--------------------------------------");
	    
	    statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet messagesResultSet = statement.executeQuery(AppConstants.SELECT_ALL_MESSAGES_STMT);
	    
	    while (messagesResultSet.next())
	    {
		if (messagesResultSet.getString(2).equals(username) || messagesResultSet.getString(3).equals(username))
		{
		    messagesResultSet.deleteRow();
		}
	    }
	    
	    messagesResultSet.close();
	    
	    statement.close();
	    
	    System.out.println("\n--------------------------------------");
	    System.out.println("trying to delete " + username + "'s reviews");
	    System.out.println("--------------------------------------");
	    
	    statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet reviewsResultSet = statement.executeQuery(AppConstants.SELECT_ALL_REVIEWS_STMT);
	    
	    while (reviewsResultSet.next())
	    {
		if (reviewsResultSet.getString(4).equals(username))
		{
		    reviewsResultSet.deleteRow();
		}
	    }
	    
	    reviewsResultSet.close();
	    
	    statement.close();
	    
	    System.out.println("finished delete " + username);
	}
	catch (SQLException | NamingException e)
	{
	    getServletContext().log("Error on Customer post", e);
	    response.sendError(500);
	}
	finally
	{
	    close();
	}
    }
    
    /**
     * Validate customer.
     *
     * @param customer the customer
     * @return true, if successful
     */
    private boolean ValidateCustomer(Customer customer)
    {
	if (customer == null || customer.getUsername() == null || customer.getEmail() == null
		|| customer.getStreetNum() == null || customer.getStreet() == null || customer.getCity() == null
		|| customer.getZipCode() == null || customer.getNickName() == null || customer.getPhoneNum() == null
		|| customer.getPassword() == null || !Pattern.matches("^[a-zA-Z1-9]{1,10}$", customer.getUsername())
		|| !Pattern.matches("^.+@.+\\..+$", customer.getEmail())
		|| !Pattern.matches("^[a-zA-Z_ ]{1,100}$", customer.getStreet())
		|| !Pattern.matches("^[1-9]\\d{0,10}$", customer.getStreetNum())
		|| !Pattern.matches("^[a-zA-Z_ ]{1,100}$", customer.getCity())
		|| !Pattern.matches("^[1-9]{5,7}$", customer.getZipCode())
		|| !Pattern.matches("^([0][5]\\d{1}[- ]?\\d{3}[- ]?\\d{4}|[0][2,3,4,8,9][- ]?\\d{7})$",
			customer.getPhoneNum())
		|| customer.getPassword().length() > 8 || customer.getNickName().length() == 0)
	{
	    System.out.println("\nfailed validate customer");
	    
	    return false;
	}
	
	return true;
    }
    
}
