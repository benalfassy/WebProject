package com.servlets;

import java.io.Closeable;
import java.io.IOException;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.models.Review;
import com.utilities.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class ReviewsServlet.
 */
public class ReviewsServlet extends HttpServlet implements Closeable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The context. */
    private Context context;
    
    /** The connection. */
    private Connection connection;
    
    /**
     * Instantiates a new reviews servlet.
     *
     * @throws NamingException the naming exception
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
	    String uri = request.getRequestURI();
	    	    
	    if (uri.indexOf(AppConstants.REVIEWS_RESTFULL) != -1)
	    {
		Review review = null;
		
		String reviewId = uri
			.substring(uri.indexOf(AppConstants.REVIEWS_RESTFULL) + AppConstants.REVIEWS_RESTFULL.length());
		
		System.out.println("\n--------------------------");
		System.out.println("trying to get review " + reviewId);
		System.out.println("--------------------------");
		
		PreparedStatement preparedStatement;
		
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_REVIEWS_BY_ID_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, reviewId);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		    return;
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
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    else
	    {
		System.out.println("\n--------------------------");
		System.out.println("trying to get all reviews ");
		System.out.println("--------------------------");
		
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
	try
	{
	    System.out.println("\n--------------------------");
	    System.out.println("trying to add review ");
	    System.out.println("--------------------------");
	    
	    Gson gson = new GsonBuilder().create();
	    
	    Review review = gson.fromJson(request.getReader(), Review.class);
	    
	    PreparedStatement pstmt;
	    
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
	    e.printStackTrace();
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
	    System.out.println("trying to update review ");
	    System.out.println("--------------------------");
	    
	    openConnection();
	    
	    Gson gson = new GsonBuilder().create();
	    
	    Review review = gson.fromJson(request.getReader(), Review.class);
	    
	    System.out.println("--------------------------");
	    System.out.println("updating review: " + review.getReviewId());
	    System.out.println("--------------------------");
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(AppConstants.SELECT_REVIEWS_BY_ID_STMT,
		    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    preparedStatement.setString(1, review.getReviewId());
	    
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    if (!resultSet.isBeforeFirst())
	    {
		response.sendError(404);
		return;
	    }
	    
	    resultSet.next();
	    
	    resultSet.updateString(2, review.getBookName());
	    resultSet.updateString(3, review.getReview());
	    resultSet.updateString(4, review.getReviewerUsername());
	    resultSet.updateInt(5, review.getIsApproved());
	    resultSet.updateString(6, review.getDate());
	    
	    resultSet.updateRow();
	    
	    resultSet.close();
	    
	    preparedStatement.close();
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
	System.out.println("trying to delete review ");
	System.out.println("--------------------------");
	
	String uri = request.getRequestURI();
	
	if (uri.indexOf(AppConstants.REVIEWS_RESTFULL) == -1)
	{
	    response.sendError(400);
	    return;
	}
	
	String reviewId = uri
		.substring(uri.indexOf(AppConstants.REVIEWS_RESTFULL) + AppConstants.REVIEWS_RESTFULL.length());
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.SELECT_REVIEWS_BY_ID_STMT, ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    pstmt.setString(1, reviewId);
	    
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
		
		resultSet.deleteRow();
		
		resultSet.close();
		
		pstmt.close();
	    }
	    
	}
	catch (SQLException | NamingException e)
	{
	    e.printStackTrace();
	    response.sendError(500);
	}
	finally
	{
	    close();
	}
	
    }
    
}
