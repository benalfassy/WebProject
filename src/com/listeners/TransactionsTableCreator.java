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
import com.models.Transaction;
import com.utilities.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Listener that reads the transactions json file and populates the data into a
 * Derby database.
 */
@WebListener
public class TransactionsTableCreator implements ServletContextListener
{
    
    /**
     * Default constructor.
     */
    public TransactionsTableCreator()
    {
	// TODO Auto-generated constructor stub
    }
    
    /**
     * utility that checks whether the customer tables already exists
     *
     * @param e
     *            the exception
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
	    Gson gson = new Gson();
	    
	    Context context = new InitialContext();
	    
	    BasicDataSource ds = (BasicDataSource) context
		    .lookup(cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
	    Connection conn = ds.getConnection();
	    
	    boolean created = false;
	    
	    try
	    {
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(AppConstants.CREATE_TRANSACTION_TABLE);
		
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
		Collection<Transaction> transactions = loadTransactions(
			cntx.getResourceAsStream(File.separator + AppConstants.TRANSACTIONS_FILE));
		
		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_TRANSACTION_STMT);
		
		for (Transaction transaction : transactions)
		{
		    pstmt.setString(1, transaction.getUsername());
		    pstmt.setString(2, transaction.getDate().toString());
		    pstmt.setString(3, gson.toJson(transaction.getBookList()));
		    pstmt.setInt(4, transaction.getTotalPrice());
		    
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
     * Loads transactions data from json file that is read from the input stream
     * into a collection of Transaction objects.
     *
     * @param is
     *            input stream to json file
     * @return collection of Transaction
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private Collection<Transaction> loadTransactions(InputStream is) throws IOException
    {
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	StringBuilder jsonFileContent = new StringBuilder();
	
	String nextLine = null;
	
	while ((nextLine = br.readLine()) != null)
	{
	    jsonFileContent.append(nextLine);
	}
	
	Gson gson = new Gson();
	
	Type type = new TypeToken<Collection<Transaction>>()
	{
	}.getType();
	
	Collection<Transaction> transactions = gson.fromJson(jsonFileContent.toString(), type);
	
	br.close();
	
	return transactions;
	
    }
}
