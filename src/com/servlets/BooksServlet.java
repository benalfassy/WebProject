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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.models.Book;
import com.models.LikeRequest;
import com.utilities.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class BooksServlet.
 */
public class BooksServlet extends HttpServlet implements Closeable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The context. */
    private Context context;
    
    /** The connection. */
    private Connection connection;
    
    /**
     * Instantiates a new books servlet.
     *
     * @throws NamingException the naming exception
     * @see HttpServlet#HttpServlet()
     */
    public BooksServlet() throws NamingException
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
     * Open DB connection.
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
	    Gson gson = new Gson();
	    
	    String uri = request.getRequestURI();
	    	    
	    if (uri.indexOf(AppConstants.BOOKS_RESTFULL) != -1)
	    {
		Book book = null;
		
		String bookName = uri
			.substring(uri.indexOf(AppConstants.BOOKS_RESTFULL) + AppConstants.BOOKS_RESTFULL.length());
		
		System.out.println("\n--------------------------");
		System.out.println("trying to get book: " + bookName);
		System.out.println("--------------------------");
		
		System.out.println(bookName);
		
		PreparedStatement preparedStatement;
		
		openConnection();
		
		preparedStatement = connection.prepareStatement(AppConstants.SELECT_BOOK_BY_NAME_STMT,
			ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		
		preparedStatement.setString(1, bookName);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		if (!resultSet.isBeforeFirst())
		{
		    response.sendError(404);
		    return;
		}
		else
		{
		    resultSet.next();
		    
		    ArrayList<String> likes = gson.fromJson(resultSet.getString(5),
			    AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    book = new Book(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3),
			    resultSet.getString(4), likes, resultSet.getString(6));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		String result = gson.toJson(book, Book.class);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    
	    else
	    {
		System.out.println("\n--------------------------");
		System.out.println("trying to get all books");
		System.out.println("--------------------------");
		
		Collection<Book> booksResult = new ArrayList<Book>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
		
		while (rs.next())
		{
		    ArrayList<String> likes = gson.fromJson(rs.getString(5), AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    booksResult.add(new Book(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), likes,
			    rs.getString(6)));
		}
		
		rs.close();
		
		stmt.close();
				
		String booksJsonResult = gson.toJson(booksResult, AppConstants.BOOKS_COLLECTION);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(booksJsonResult);
		
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
	response.sendError(400);
    }
    
    /**
     * Do put.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	try
	{
	    System.out.println("\n--------------------------");
	    System.out.println("trying to update book");
	    System.out.println("--------------------------");
	    
	    openConnection();
	    
	    Gson gson = new GsonBuilder().create();
	    
	    LikeRequest likeRequest = gson.fromJson(request.getReader(), LikeRequest.class);
	    
	    if (!ValidateLikeRequest(likeRequest))
	    {
		response.sendError(400);
		return;
	    }
	    
	    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet resultSet = statement.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
	    
	    while (resultSet.next())
	    {
		if (resultSet.getString(1).equals(likeRequest.getBookName()))
		{
		    
		    ArrayList<String> likesArray = gson.fromJson(resultSet.getString(5),
			    AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    if (likeRequest.getIsLike())
		    {
			likesArray.add(likeRequest.getNickName());
		    }
		    else
		    {
			likesArray.removeIf(username -> username.equals(likeRequest.getNickName()));
		    }
		    
		    resultSet.updateString(5, gson.toJson(likesArray));
		    
		    resultSet.updateRow();
		    
		}
	    }
	    
	    resultSet.close();
	    
	    statement.close();
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
     * Validate like request.
     *
     * @param likeRequest the like request
     * @return true, if successful
     */
    private boolean ValidateLikeRequest(LikeRequest likeRequest)
    {
	if (likeRequest == null || likeRequest.getBookName() == null || likeRequest.getNickName().length() > 20)
	{
	    System.out.println("Failed validating like request ");
	    
	    return false;
	}
	
	return true;
    }
    
}
