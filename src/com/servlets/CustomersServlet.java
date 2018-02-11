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
import com.google.gson.GsonBuilder;
import com.utilities.AppConstants;
import com.models.Customer;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/customers", "/customers/*" })
public class CustomersServlet extends HttpServlet implements Closeable
{
    private static final long serialVersionUID = 1L;
    
    private Context context;
    
    private Connection connection;
    
    /**
     * @throws NamingException
     * @throws SQLException
     * @see HttpServlet#HttpServlet()
     */
    public CustomersServlet() throws NamingException
    {
	super();
	
	context = new InitialContext();
    }
    
    private void openConnection() throws SQLException, NamingException
    {
	BasicDataSource ds = (BasicDataSource) context
		.lookup(getServletContext().getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
	
	connection = ds.getConnection();
    }
    
    @Override
    public void close()
    {
	try
	{
	    connection.close();
	}
	catch (Exception e)
	{
	}
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	String uri = request.getRequestURI();
	
	System.out.println(uri);
	
	if (uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) != -1)
	{
	    Customer customer = null;
	    
	    String username = uri
		    .substring(uri.indexOf(AppConstants.CUSTOMERS_RESTFULL) + AppConstants.CUSTOMERS_RESTFULL.length());
	    
	    System.out.println(username);
	    
	    PreparedStatement preparedStatement;
	    
	    try
	    {
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, username);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		}
		else
		{
		    resultSet.next();
		    
		    customer = new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
			    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
			    resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
			    resultSet.getString(10), resultSet.getString(11), resultSet.getString(12), resultSet.getString(13));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		Gson gson = new Gson();
		
		String result = gson.toJson(customer, Customer.class);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    catch (Exception e)
	    {
		e.printStackTrace();
		System.out.println(e);
		response.sendError(500);
	    }
	}
	else
	{
	    
	    try
	    {
		Collection<Customer> customersResult = new ArrayList<Customer>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
		
		while (rs.next())
		{
		    customersResult.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
			    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
			    rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));
		}
		
		rs.close();
		
		stmt.close();
		
		Gson gson = new Gson();
		
		// convert from customers collection to json
		
		String customerJsonResult = gson.toJson(customersResult, AppConstants.CUSTOMER_COLLECTION);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(customerJsonResult);
		
		writer.close();
	    }
	    catch (SQLException | NamingException e)
	    {
		System.out.println(e);
		getServletContext().log("Error while querying for customers", e);
		response.sendError(500);// internal server error
	    }
	}
	
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	Gson gson = new GsonBuilder().create();
	
	Customer customer = gson.fromJson(request.getReader(), Customer.class);
	
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
	
    }
    
}
