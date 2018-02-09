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
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/customers",
	"/customers/name/*" })
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
	
	try
	{
	    
	    Collection<Customer> customersResult = new ArrayList<Customer>();
	    
	    // String uri = request.getRequestURI();
	    
	    /*
	     * if (uri.indexOf(AppConstants.NAME) != -1) {// filter customer by
	     * specific name String name =
	     * uri.substring(uri.indexOf(AppConstants.NAME) +
	     * AppConstants.NAME.length() + 1); PreparedStatement stmt; try {
	     * stmt =
	     * conn.prepareStatement(AppConstants.SELECT_CUSTOMER_BY_NAME_STMT);
	     * name = name.replaceAll("\\%20", " "); stmt.setString(1, name);
	     * ResultSet rs = stmt.executeQuery(); while (rs.next()) {
	     * customersResult.add(new Customer(rs.getString(1),
	     * rs.getString(2), rs.getString(3))); } rs.close(); stmt.close(); }
	     * catch (SQLException e) {
	     * getServletContext().log("Error while querying for customers", e);
	     * response.sendError(500);// internal server error } }
	     */
	    
	    Statement stmt;
	    
	    try
	    {
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_CUSTOMERS_STMT);
		
		while (rs.next())
		{
		    customersResult.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
			    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
			    rs.getString(10), rs.getString(11), rs.getString(12)));
		}
		
		rs.close();
		
		stmt.close();
	    }
	    catch (SQLException | NamingException e)
	    {
		getServletContext().log("Error while querying for customers", e);
		response.sendError(500);// internal server error
	    }
	    
	    connection.close();
	    
	    Gson gson = new Gson();
	    
	    // convert from customers collection to json
	    
	    String customerJsonResult = gson.toJson(customersResult, AppConstants.CUSTOMER_COLLECTION);
	    
	    response.addHeader("Content-Type", "application/json");
	    
	    PrintWriter writer = response.getWriter();
	    
	    writer.println(customerJsonResult);
	    
	    writer.close();
	}
	catch (SQLException e)
	{
	    getServletContext().log("Error on Customer get", e);
	    response.sendError(500);
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
	    pstmt.executeUpdate();
	    
	    connection.commit();
	    pstmt.close();
	}
	catch (SQLException | NamingException e)
	{
	    getServletContext().log("Error on Customer post", e);
	    response.sendError(500);
	}
	
    }
    
}
