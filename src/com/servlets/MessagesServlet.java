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
import java.util.Collections;
import java.util.Comparator;
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
import com.models.Message;
import com.utilities.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class MessagesServlet.
 */
public class MessagesServlet extends HttpServlet implements Closeable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The context. */
    private Context context;
    
    /** The connection. */
    private Connection connection;
    
    /**
     * Instantiates a new messages servlet.
     *
     * @throws NamingException the naming exception
     * @see HttpServlet#HttpServlet()
     */
    public MessagesServlet() throws NamingException
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
	    
	    System.out.println(uri);
	    
	    if (uri.indexOf(AppConstants.MESSAGES_RESTFULL) != -1)
	    {
		Message message = null;
		
		String messageId = uri.substring(
			uri.indexOf(AppConstants.MESSAGES_RESTFULL) + AppConstants.MESSAGES_RESTFULL.length());
		
		System.out.println("\n--------------------------");
		System.out.println("trying to get message: " + messageId);
		System.out.println("--------------------------");
		PreparedStatement preparedStatement;
		
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_MESSAGE_BY_ID_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, messageId);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		    return;
		}
		else
		{
		    resultSet.next();
		    
		    message = new Message(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
			    resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		Gson gson = new Gson();
		
		String result = gson.toJson(message, Message.class);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    else
	    {
		System.out.println("\n--------------------------");
		System.out.println("trying to get all messages");
		System.out.println("--------------------------");
		
		ArrayList<Message> messagesResult = new ArrayList<Message>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_MESSAGES_STMT);
		
		while (rs.next())
		{
		    messagesResult.add(new Message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
			    rs.getString(5), rs.getInt(6)));
		}
		
		rs.close();
		
		stmt.close();
		
		messagesResult.sort(Comparator.comparing(msg -> LocalDate.parse(msg.getDate())));
		
		Collections.reverse(messagesResult);
		
		Gson gson = new Gson();
		
		String messagesJsonResult = gson.toJson(messagesResult, AppConstants.MESSAGES_COLLECTION);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(messagesJsonResult);
		
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
	System.out.println("trying to add messages");
	System.out.println("--------------------------");
	
	Gson gson = new GsonBuilder().create();
	
	Message message = gson.fromJson(request.getReader(), Message.class);
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.INSERT_MESSAGES_STMT);
	    
	    pstmt.setString(1, UUID.randomUUID().toString());
	    pstmt.setString(2, message.getFrom());
	    pstmt.setString(3, message.getTo());
	    pstmt.setString(4, message.getContent());
	    pstmt.setString(5, LocalDate.now().toString());
	    pstmt.setInt(6, 0);
	    
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
    
   
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected synchronized void doPut(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
    {
	try
	{
	    System.out.println("\n--------------------------");
	    System.out.println("trying to update messages");
	    System.out.println("--------------------------");
	    
	    openConnection();
	    
	    Gson gson = new GsonBuilder().create();
	    
	    Message message = gson.fromJson(request.getReader(), Message.class);
	    
	    System.out.println("\n--------------------------");
	    System.out.println("updating message: " + message.getMessageId());
	    System.out.println("--------------------------");
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(AppConstants.SELECT_MESSAGE_BY_ID_STMT,
		    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    
	    preparedStatement.setString(1, message.getMessageId());
	    
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    if (!resultSet.isBeforeFirst())
	    {
		response.sendError(404);
		return;
	    }
	    
	    resultSet.next();
	    
	    resultSet.updateString(2, message.getFrom());
	    resultSet.updateString(3, message.getTo());
	    resultSet.updateString(4, message.getContent());
	    resultSet.updateString(5, message.getDate());
	    resultSet.updateInt(6, message.getIsViewed());
	    
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
	System.out.println("trying to delete messages");
	System.out.println("--------------------------");
	
	String uri = request.getRequestURI();
	
	if (uri.indexOf(AppConstants.MESSAGES_RESTFULL) == -1)
	{
	    response.sendError(400);
	    return;
	}
	
	String messageId = uri
		.substring(uri.indexOf(AppConstants.MESSAGES_RESTFULL) + AppConstants.MESSAGES_RESTFULL.length());
	
	PreparedStatement pstmt;
	try
	{
	    openConnection();
	    
	    pstmt = connection.prepareStatement(AppConstants.SELECT_MESSAGE_BY_ID_STMT, ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    pstmt.setString(1, messageId);
	    
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
