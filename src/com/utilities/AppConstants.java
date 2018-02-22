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

// TODO: Auto-generated Javadoc
/**
 * A simple place to hold global application constants.
 */
public class AppConstants
{
    
    /** The Constant CUSTOMERS. */
    static public final String CUSTOMERS = "customers";
    
    /** The Constant MESSAGES. */
    static public final String MESSAGES = "messages";
    
    /** The Constant CUSTOMERS_FILE. */
    static public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
    
    /** The Constant MESSAGES_FILE. */
    static public final String MESSAGES_FILE = MESSAGES + ".json";
    
    /** The Constant BOOKS. */
    static public final String BOOKS = "books";
    
    /** The Constant BOOKS_FILE. */
    static public final String BOOKS_FILE = BOOKS + ".json";
    
    /** The Constant REVIEWS. */
    static public final String REVIEWS = "reviews";
    
    /** The Constant REVIEWS_FILE. */
    static public final String REVIEWS_FILE = REVIEWS + ".json";
    
    /** The Constant TRANSACTIONS. */
    static public final String TRANSACTIONS = "transactions";
    
    /** The Constant TRANSACTIONS_FILE. */
    static public final String TRANSACTIONS_FILE = TRANSACTIONS + ".json";
    
    /** The Constant NAME. */
    static public final String NAME = "name";
    
    /** The Constant MESSAGES_RESTFULL. */
    static public final String MESSAGES_RESTFULL = "messages/";
    
    /** The Constant CUSTOMERS_RESTFULL. */
    static public final String CUSTOMERS_RESTFULL = "customers/";
    
    /** The Constant BOOKS_RESTFULL. */
    static public final String BOOKS_RESTFULL = "books/";
    
    /** The Constant REVIEWS_RESTFULL. */
    static public final String REVIEWS_RESTFULL = "reviews/";
    
    /** The Constant TRANSACTIONS_RESTFULL. */
    static public final String TRANSACTIONS_RESTFULL = "reviews/";
    
    /** The Constant CUSTOMER_COLLECTION. */
    static public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>()
    {
    }.getType();
    
    /** The Constant TRANSACTION_COLLECTION. */
    static public final Type TRANSACTION_COLLECTION = new TypeToken<Collection<Transaction>>()
    {
    }.getType();
    
    /** The Constant MESSAGES_COLLECTION. */
    static public final Type MESSAGES_COLLECTION = new TypeToken<Collection<Message>>()
    {
    }.getType();
    
    /** The Constant BOOKS_COLLECTION. */
    static public final Type BOOKS_COLLECTION = new TypeToken<Collection<Book>>()
    {
    }.getType();
    
    /** The Constant REVIEWS_COLLECTION. */
    static public final Type REVIEWS_COLLECTION = new TypeToken<Collection<Review>>()
    {
    }.getType();
    
    /** The Constant SCROLL_COLLECTION. */
    static public final Type SCROLL_COLLECTION = new TypeToken<ArrayList<Pair<String, String>>>()
    {
    }.getType();
    
    /** The Constant ARRAYLISTSTRING_COLLECTION. */
    static public final Type ARRAYLISTSTRING_COLLECTION = new TypeToken<ArrayList<String>>()
    {
    }.getType();
    
    // derby constants
    
    /** The Constant DB_NAME. */
    static public final String DB_NAME = "DB_NAME";
    
    /** The Constant DB_DATASOURCE. */
    static public final String DB_DATASOURCE = "DB_DATASOURCE";
    
    /** The Constant PROTOCOL. */
    static public final String PROTOCOL = "jdbc:derby:";
    
    /** The Constant OPEN. */
    static public final String OPEN = "Open";
    
    /** The Constant SHUTDOWN. */
    static public final String SHUTDOWN = "Shutdown";
    
    // sql statements
    
    /** The Constant CREATE_CUSTOMERS_TABLE. */
    static public final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE CUSTOMERS(Username varchar(100),"
	    + "Email varchar(100)," + "Street varchar(100)," + "StreetNum varchar(100)," + "City varchar(100),"
	    + "ZipCode varchar(100)," + "PhoneNum varchar(100)," + "Password varchar(100)," + "NickName varchar(100),"
	    + "Description varchar(100)," + "Photo varchar(100)," + "Affiliation varchar(100)," + "Books varchar(500),"
	    + "BookScroll varchar(1000))";
    
    /** The Constant CREATE_BOOKS_TABLE. */
    static public final String CREATE_BOOKS_TABLE = "CREATE TABLE BOOKS(BookName varchar(100)," + "Image varchar(200),"
	    + "Price int," + "Description varchar(1000)," + "Likes varchar(1000)," + "BookPath varchar(200))";
    
    /** The Constant CREATE_REVIEWS_TABLE. */
    static public final String CREATE_REVIEWS_TABLE = "CREATE TABLE REVIEWS(ReviewId varchar(100),"
	    + "BookName varchar(100)," + "Review varchar(500)," + "ReviewerUsername varchar(100)," + "IsApproved int,"
	    + "date varchar(100))";
    
    /** The Constant CREATE_TRANSACTION_TABLE. */
    static public final String CREATE_TRANSACTION_TABLE = "CREATE TABLE TRANSACTIONS(Username varchar(100), Date varchar(100), BookList varchar(1000), TotalPrice int)";
    
    /** The Constant CREATE_MESSAGES_TABLE. */
    static public final String CREATE_MESSAGES_TABLE = "CREATE TABLE MESSAGES(MessageId varchar(100), MessageFrom varchar(100), MessageTo varchar(100), Content varchar(1000), Date varchar(100), isViewed int)";
    
    /** The Constant INSERT_MESSAGES_STMT. */
    static public final String INSERT_MESSAGES_STMT = "INSERT INTO MESSAGES VALUES(?,?,?,?,?,?)";
    
    /** The Constant INSERT_TRANSACTION_STMT. */
    static public final String INSERT_TRANSACTION_STMT = "INSERT INTO TRANSACTIONS VALUES(?,?,?,?)";
    
    /** The Constant INSERT_BOOKS_STMT. */
    static public final String INSERT_BOOKS_STMT = "INSERT INTO BOOKS VALUES(?,?,?,?,?,?)";
    
    /** The Constant INSERT_REVIEWS_STMT. */
    static public final String INSERT_REVIEWS_STMT = "INSERT INTO REVIEWS VALUES(?,?,?,?,?,?)";
    
    /** The Constant INSERT_CUSTOMER_STMT. */
    static public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMERS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    /** The Constant SELECT_ALL_MESSAGES_STMT. */
    static public final String SELECT_ALL_MESSAGES_STMT = "SELECT * FROM MESSAGES";
    
    /** The Constant SELECT_ALL_CUSTOMERS_STMT. */
    static public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMERS";
    
    /** The Constant SELECT_ALL_TRANSACTIONS_STMT. */
    static public final String SELECT_ALL_TRANSACTIONS_STMT = "SELECT * FROM TRANSACTIONS";
    
    /** The Constant SELECT_ALL_REVIEWS_STMT. */
    static public final String SELECT_ALL_REVIEWS_STMT = "SELECT * FROM REVIEWS";
    
    /** The Constant SELECT_ALL_BOOKS_STMT. */
    static public final String SELECT_ALL_BOOKS_STMT = "SELECT * FROM BOOKS";
    
    /** The Constant SELECT_MESSAGE_BY_ID_STMT. */
    static public final String SELECT_MESSAGE_BY_ID_STMT = "SELECT * FROM MESSAGES " + "WHERE MessageId=?";
    
    /** The Constant SELECT_CUSTOMER_BY_NAME_STMT. */
    static public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMERS " + "WHERE Username=?";
    
    /** The Constant SELECT_BOOK_BY_NAME_STMT. */
    static public final String SELECT_BOOK_BY_NAME_STMT = "SELECT * FROM BOOKS " + "WHERE BookName=?";
    
    /** The Constant SELECT_REVIEWS_BY_ID_STMT. */
    static public final String SELECT_REVIEWS_BY_ID_STMT = "SELECT * FROM REVIEWS " + "WHERE ReviewId=?";
}
