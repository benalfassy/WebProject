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
import com.models.Book;
import com.models.Customer;
import com.models.Review;
import com.sun.jmx.snmp.Timestamp;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/reviews", "/reviews/*" })
public class ReviewsServlet extends HttpServlet implements Closeable
{
    private static final long serialVersionUID = 1L;
    
    private Context context;
    
    private Connection connection;
    
    /**
     * @throws NamingException
     * @throws SQLException
     * @see HttpServlet#HttpServlet()
     */
    public ReviewsServlet() throws NamingException
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
	    String uri = request.getRequestURI();
	    
	    System.out.println(uri);
	    
	    if (uri.indexOf(AppConstants.REVIEWS_RESTFULL) != -1)
	    {
		Review review = null;
		
		String reviewId = uri
			.substring(uri.indexOf(AppConstants.REVIEWS_RESTFULL) + AppConstants.REVIEWS_RESTFULL.length());
		
		System.out.println("trying to get review: " + reviewId);
		
		System.out.println(reviewId);
		
		PreparedStatement preparedStatement;
		
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_REVIEWS_BY_ID_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, reviewId);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		}
		else
		{
		    resultSet.next();
		    
		    review = new Review(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
			    resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		Gson gson = new Gson();
		
		String result = gson.toJson(review, Review.class);
		
		System.out.println("trying to return review: " + reviewId);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    else
	    {
		
		Collection<Review> reviewsResult = new ArrayList<Review>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_REVIEWS_STMT);
		
		while (rs.next())
		{
		    reviewsResult.add(new Review(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
			    rs.getInt(5), rs.getString(6)));
		}
		
		rs.close();
		
		stmt.close();
		
		Gson gson = new Gson();
		
		// convert from customers collection to json
		
		String reviewsJsonResult = gson.toJson(reviewsResult, AppConstants.REVIEWS_COLLECTION);
		
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
	Gson gson = new GsonBuilder().create();
	
	Review review = gson.fromJson(request.getReader(), Review.class);
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.INSERT_REVIEWS_STMT);
	    
	    pstmt.setString(1, UUID.randomUUID().toString());
	    pstmt.setString(2, review.getBookName());
	    pstmt.setString(3, review.getReview());
	    pstmt.setString(4, review.getReviewerUsername());
	    pstmt.setInt(5, 0);
	    pstmt.setString(6, LocalDate.now().toString());
	    
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
