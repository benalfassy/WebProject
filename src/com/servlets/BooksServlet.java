package com.servlets;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.utilities.AppConstants;

import javafx.util.Pair;

import com.models.Book;
import com.models.Customer;
import com.models.LikeRequest;

/**
 * Servlet implementation class CustomersServlet1
 */
@WebServlet(description = "Servlet to provide details about customers", urlPatterns = { "/books", "/books/*" })
public class BooksServlet extends HttpServlet implements Closeable
{
    private static final long serialVersionUID = 1L;
    
    private Context context;
    
    private Connection connection;
    
    /**
     * @throws NamingException
     * @throws SQLException
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
	    
	    if (uri.indexOf(AppConstants.BOOKS_RESTFULL) != -1)
	    {
		Book book = null;
		
		String bookName = uri
			.substring(uri.indexOf(AppConstants.BOOKS_RESTFULL) + AppConstants.BOOKS_RESTFULL.length());
		
		System.out.println("trying to get book: " + bookName);
		
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
		    
		    ArrayList<String> likes = gson.fromJson(resultSet.getString(5), AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    book = new Book(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3),
			    resultSet.getString(4), likes, resultSet.getString(6));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		String result = gson.toJson(book, Book.class);
		
		System.out.println("trying to return book: " + bookName);
		
		response.addHeader("Content-Type", "application/json");
		
		PrintWriter writer = response.getWriter();
		
		writer.println(result);
		
		writer.close();
		
	    }
	    
	    else
	    {
		
		Collection<Book> booksResult = new ArrayList<Book>();
		
		Statement stmt;
		
		openConnection();
		
		stmt = connection.createStatement();
		
		ResultSet rs = stmt.executeQuery(AppConstants.SELECT_ALL_BOOKS_STMT);
		
		while (rs.next())
		{
		    ArrayList<String> likes = gson.fromJson(rs.getString(5), AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
		    booksResult.add(new Book(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
			    likes, rs.getString(6)));
		}
		
		rs.close();
		
		stmt.close();
				
		// convert from customers collection to json
		
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	response.sendError(400);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	try
	{
	    openConnection();
	    
	    Gson gson = new GsonBuilder().create();
	    
	    LikeRequest likeRequest = gson.fromJson(request.getReader(), LikeRequest.class);
	    
	    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    
	    ResultSet resultSet = statement.executeQuery("SELECT * FROM BOOKS");
	    
	    while (resultSet.next())
	    {
		if (resultSet.getString(1).equals(likeRequest.getBookName()))
		{
		    
		    ArrayList<String> likesArray = gson.fromJson(resultSet.getString(5), AppConstants.ARRAYLISTSTRING_COLLECTION);
		    
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
    
}
