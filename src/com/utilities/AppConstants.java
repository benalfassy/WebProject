package com.utilities;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import com.models.Customer;

/**
 * A simple place to hold global application constants
 */
public class AppConstants
{
    
    static public final String CUSTOMERS = "customers";
    
    static public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
    
    static public final String NAME = "name";
    
    static public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>()
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
	    + "Description varchar(100)," + "Photo varchar(100),"+"Affiliation varchar(100))";
        
    static public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMERS VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        
    static public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMERS";
    
    static public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMERS " + "WHERE Username=?";
}
