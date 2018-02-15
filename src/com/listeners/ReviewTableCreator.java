package com.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.utilities.AppConstants;
import com.models.Book;
import com.models.Customer;
import com.models.Review;

/**
 * An example listener that reads the customer json file and populates the data
 * into a Derby database
 */
@WebListener
public class ReviewTableCreator implements ServletContextListener
{
    
    /**
     * Default constructor.
     */
    public ReviewTableCreator()
    {
	// TODO Auto-generated constructor stub
    }
    
    // utility that checks whether the customer tables already exists
    private boolean tableAlreadyExists(SQLException e)
    {
	boolean exists;
	if (e.getSQLState().equals("X0Y32"))
	{
	    exists = true;
	}
	else
	{
	    exists = false;
	}
	return exists;
    }
    
    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)
    {
	ServletContext cntx = event.getServletContext();
		
	try
	{
	    
	    Context context = new InitialContext();
	    
	    BasicDataSource ds = (BasicDataSource) context
		    .lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
	    Connection conn = ds.getConnection();
	    
	    boolean created = false;
	    
	    try
	    {
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(AppConstants.CREATE_REVIEWS_TABLE);
		
		// commit update
		
		conn.commit();
		
		stmt.close();
	    }
	    catch (SQLException e)
	    {
		created = tableAlreadyExists(e);
		
		if (!created)
		{
		    throw e;
		}
	    }
	    
	    // if no database exist in the past - further populate its records
	    // in the table
	    if (!created)
	    {
		// populate customers table with customer data from json file
		Collection<Review> reviews = loadReviews(
			cntx.getResourceAsStream(File.separator + AppConstants.REVIEWS_FILE));
		
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_REVIEWS_STMT);
		
		for (Review review : reviews)
		{
		    pstmt.setString(1, review.getReviewId());
		    pstmt.setString(2, review.getBookName());
		    pstmt.setString(3, review.getReview());
		    pstmt.setString(4, review.getReviewerUsername());
		    pstmt.setInt(5, review.getIsApproved());
		    pstmt.setString(6, review.getDate());
		    pstmt.executeUpdate();
		}
		
		// commit update
		conn.commit();
		// close statements
		pstmt.close();
	    }
	    
	    // close connection
	    conn.close();
	    
	}
	catch (IOException | SQLException | NamingException e)
	{
	    System.out.println(e);
	    cntx.log("Error during database initialization", e);
	}
    }
    
    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)
    {
	ServletContext cntx = event.getServletContext();
	
	// shut down database
	try
	{
	    // obtain CustomerDB data source from Tomcat's context and shutdown
	    Context context = new InitialContext();
	    BasicDataSource ds = (BasicDataSource) context
		    .lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.SHUTDOWN);
	    ds.getConnection();
	    ds = null;
	}
	catch (SQLException | NamingException e)
	{
	    cntx.log("Error shutting down database", e);
	}
	
    }
    
    /**
     * Loads customers data from json file that is read from the input stream
     * into a collection of Customer objects
     * 
     * @param is
     *            input stream to json file
     * @return collection of customers
     * @throws IOException
     */
    private Collection<Review> loadReviews(InputStream is) throws IOException
    {
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	StringBuilder jsonFileContent = new StringBuilder();
		
	String nextLine = null;
	
	while ((nextLine = br.readLine()) != null)
	{
	    jsonFileContent.append(nextLine);
	}
	
	Gson gson = new Gson();
	
	Type type = new TypeToken<Collection<Review>>()
	{
	}.getType();
	
	//System.out.println("trying to parse: " + jsonFileContent.toString());
	
	Collection<Review> reviews = gson.fromJson(jsonFileContent.toString(), type);
	
	br.close();
	
	return reviews;
	
    }
}
