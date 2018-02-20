package com.utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;
import com.models.Book;
import com.models.Customer;
import com.models.Message;
import com.models.Review;
import com.models.Transaction;

import javafx.util.Pair;

/**
 * A simple place to hold global application constants
 */
public class AppConstants
{
    
    static public final String CUSTOMERS = "customers";
    
    static public final String MESSAGES = "messages";
    
    static public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
    
    static public final String MESSAGES_FILE = MESSAGES + ".json";
    
    static public final String BOOKS = "books";
    
    static public final String BOOKS_FILE = BOOKS + ".json";
    
    static public final String REVIEWS = "reviews";
    
    static public final String REVIEWS_FILE = REVIEWS + ".json";
    
    static public final String TRANSACTIONS = "transactions";
    
    static public final String TRANSACTIONS_FILE = TRANSACTIONS + ".json";
    
    static public final String NAME = "name";
    
    static public final String MESSAGES_RESTFULL = "messages/";
    
    static public final String CUSTOMERS_RESTFULL = "customers/";
    
    static public final String BOOKS_RESTFULL = "books/";
    
    static public final String REVIEWS_RESTFULL = "reviews/";
    
    static public final String TRANSACTIONS_RESTFULL = "reviews/";
    
    static public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>()
    {
    }.getType();
    
    static public final Type TRANSACTION_COLLECTION = new TypeToken<Collection<Transaction>>()
    {
    }.getType();
    
    static public final Type MESSAGES_COLLECTION = new TypeToken<Collection<Message>>()
    {
    }.getType();
    
    static public final Type BOOKS_COLLECTION = new TypeToken<Collection<Book>>()
    {
    }.getType();
    
    static public final Type REVIEWS_COLLECTION = new TypeToken<Collection<Review>>()
    {
    }.getType();
    
    static public final Type SCROLL_COLLECTION = new TypeToken<ArrayList<Pair<String, String>>>()
    {
    }.getType();
    
    static public final Type ARRAYLISTSTRING_COLLECTION = new TypeToken<ArrayList<String>>()
    {
    }.getType();
    
    // derby constants
    
    static public final String DB_NAME = "DB_NAME";
    
    static public final String DB_DATASOURCE = "DB_DATASOURCE";
    
    static public final String PROTOCOL = "jdbc:derby:";
    
    static public final String OPEN = "Open";
    
    static public final String SHUTDOWN = "Shutdown";
    
    // sql statements
    
    static public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMERS(Username varchar(100),"
	    + "Email varchar(100)," + "Street varchar(100)," + "StreetNum varchar(100)," + "City varchar(100),"
	    + "ZipCode varchar(100)," + "PhoneNum varchar(100)," + "Password varchar(100)," + "NickName varchar(100),"
	    + "Description varchar(100)," + "Photo varchar(100)," + "Affiliation varchar(100)," + "Books varchar(500),"
	    + "BookScroll varchar(1000))";
    
    static public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS(BookName varchar(100)," + "Image varchar(200),"
	    + "Price int," + "Description varchar(1000)," + "Likes varchar(1000)," + "BookPath varchar(200))";
    
    static public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS(ReviewId varchar(100),"
	    + "BookName varchar(100)," + "Review varchar(500)," + "ReviewerUsername varchar(100)," + "IsApproved int,"
	    + "date varchar(100))";
    
    static public final String CREATE_TRANSACTION_TABLE = "CREATE TABLE TRANSACTIONS(Username varchar(100), Date varchar(100), BookList varchar(1000), TotalPrice int)";
    
    static public final String CREATE_MESSAGES_TABLE = "CREATE TABLE MESSAGES(MessageId varchar(100), MessageFrom varchar(100), MessageTo varchar(100), Content varchar(1000), Date varchar(100), isViewed int)";
    
    static public final String INSERT_MESSAGES_STMT = "INSERT INTO MESSAGES VALUES(?,?,?,?,?,?)";
    
    static public final String INSERT_TRANSACTION_STMT = "INSERT INTO TRANSACTIONS VALUES(?,?,?,?)";
    
    static public final String INSERT_BOOKS_STMT = "INSERT INTO BOOKS VALUES(?,?,?,?,?,?)";
    
    static public final String INSERT_REVIEWS_STMT = "INSERT INTO REVIEWS VALUES(?,?,?,?,?,?)";
    
    static public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMERS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    static public final String SELECT_ALL_MESSAGES_STMT = "SELECT * FROM MESSAGES";
    
    static public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMERS";
    
    static public final String SELECT_ALL_TRANSACTIONS_STMT = "SELECT * FROM TRANSACTIONS";
    
    static public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEWS";
    
    static public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
    
    static public final String SELECT_MESSAGE_BY_ID_STMT = "SELECT * FROM MESSAGES " + "WHERE MessageId=?";
    
    static public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMERS " + "WHERE Username=?";
    
    static public final String SELECT_BOOK_BY_NAME_STMT = "SELECT * FROM BOOKS " + "WHERE BookName=?";
    
    static public final String SELECT_REVIEWS_BY_ID_STMT = "SELECT * FROM REVIEWS " + "WHERE ReviewId=?";
}
