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
import com.models.Message;
import com.utilities.AppConstants;

/**
 * An example listener that reads the customer json file and populates the data
 * into a Derby database
 */
@WebListener
public class MessagesTableCreator implements ServletContextListener
{
    
    /**
     * Default constructor.
     */
    public MessagesTableCreator()
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
		
		stmt.executeUpdate(AppConstants.CREATE_MESSAGES_TABLE);
		
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
		Collection<Message> messages = loadMessages(
			cntx.getResourceAsStream(File.separator + AppConstants.MESSAGES_FILE));
		
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_MESSAGES_STMT);
		
		for (Message message : messages)
		{
		    pstmt.setString(1, message.getMessageId());
		    pstmt.setString(2, message.getFrom());
		    pstmt.setString(3, message.getTo());
		    pstmt.setString(4, message.getContent());
		    pstmt.setString(5, message.getDate());
		    pstmt.setInt(6, message.getIsViewed());
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
    private Collection<Message> loadMessages(InputStream is) throws IOException
    {
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	StringBuilder jsonFileContent = new StringBuilder();
		
	String nextLine = null;
	
	while ((nextLine = br.readLine()) != null)
	{
	    jsonFileContent.append(nextLine);
	}
	
	Gson gson = new Gson();
	
	Type type = new TypeToken<Collection<Message>>()
	{
	}.getType();
	
	//System.out.println("trying to parse: " + jsonFileContent.toString());
	
	Collection<Message> messages = gson.fromJson(jsonFileContent.toString(), type);
	
	br.close();
	
	return messages;
	
    }
}
