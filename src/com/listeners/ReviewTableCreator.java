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
import java.util.Collection;

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
import com.models.Review;
import com.utilities.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Listener that reads the reviews json file and populates the data into a Derby
 * database.
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
    
    /**
     * utility that checks whether the customer tables already exists
     *
     * @param e
     *            the e
     * @return true, if successful
     */
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
     * Context initialized.
     *
     * @param event
     *            the event
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
		
		conn.commit();
		pstmt.close();
	    }
	    
	    conn.close();
	    
	}
	catch (IOException | SQLException | NamingException e)
	{
	    System.out.println(e);
	    cntx.log("Error during database initialization", e);
	}
    }
    
    /**
     * Context destroyed.
     *
     * @param event
     *            the event
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)
    {
	ServletContext cntx = event.getServletContext();
	
	// shut down database
	try
	{
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
     * Loads reviews data from json file that is read from the input stream into
     * a collection of Review objects.
     *
     * @param is
     *            input stream to json file
     * @return collection of reviews
     * @throws IOException
     *             Signals that an I/O exception has occurred.
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
	
	Collection<Review> reviews = gson.fromJson(jsonFileContent.toString(), type);
	
	br.close();
	
	return reviews;
	
    }
}
