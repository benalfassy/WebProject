package com.servlets;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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

import javafx.util.Pair;

import com.models.Book;
import com.models.Customer;
import com.models.Review;
import com.models.Transaction;
import com.sun.jmx.snmp.Timestamp;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/transactions", })
public class TransactionsServlet extends HttpServlet implements Closeable
{
    private static final long serialVersionUID = 1L;
    
    private Context context;
    
    private Connection connection;
    
    /**
     * @throws NamingException
     * @throws SQLException
     * @see HttpServlet#HttpServlet()
     */
    public TransactionsServlet() throws NamingException
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
	    e.printStackTrace();
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
	    Gson gson = new Gson();
	    
	    String uri = request.getRequestURI();
	    
	    System.out.println(uri);
	    
	    if (uri.indexOf(AppConstants.TRANSACTIONS_RESTFULL) != -1)
	    {
		response.sendError(400);
		return;
	    }
	    else
	    {
		
		Collection<Transaction> transactionsResult = new ArrayList<Transaction>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_TRANSACTIONS_STMT);
		
		while (rs.next())
		{
		    
		    ArrayList<String> bookList = gson.fromJson(rs.getString(3),
			    AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    transactionsResult.add(
			    new Transaction(rs.getString(1), LocalDate.parse(rs.getString(2)), bookList, rs.getInt(4)));
		}
		
		rs.close();
		
		stmt.close();
		
		// convert from customers collection to json
		
		String reviewsJsonResult = gson.toJson(transactionsResult, AppConstants.TRANSACTION_COLLECTION);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(reviewsJsonResult);
		
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	try
	{
	    openConnection();
	    
	    System.out.println("adding transaction");
	    
	    Gson gson = new GsonBuilder().create();
	    
	    Transaction transaction = gson.fromJson(request.getReader(), Transaction.class);
	    
	    PreparedStatement pstmt;
	    
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.INSERT_TRANSACTION_STMT);
	    
	    pstmt.setString(1, transaction.getUsername());
	    pstmt.setString(2, transaction.getDate().toString());
	    pstmt.setString(3, gson.toJson(transaction.getBookList()));
	    pstmt.setInt(4, transaction.getTotalPrice());
	   
	    pstmt.executeUpdate();
	    
	    connection.commit();
	    	    
	    pstmt.close();
	    
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
    
}
