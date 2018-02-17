package com.models;

import java.util.ArrayList;

import javafx.util.Pair;


/**
 * A simple bean to hold data
 */
public class Customer
{
    static public final String ADMIN_AFFILIATION = "Admin";
    
    static public final String CUSTOMER_AFFILIATION = "User";
    
    private String username;
    
    private String email;
    
    private String street;
    
    private String streetNum;
    
    private String city;
    
    private String zipCode;
    
    private String phoneNum;
    
    private String password;
    
    private String nickName;
    
    private String description;
    
    private String photo;
    
    private String affiliation;
    
    private String myBooks;
    
    private String[] myBookList;
    
    private ArrayList<Pair<String, String>> bookScroll;
    
    public Customer(String username, String email, String street, String streetNum, String city, String zipCode,
	    String phoneNum, String password, String nickName, String description, String phote, String affiliation,
	    String books, ArrayList<Pair<String, String>> bookScroll)
    {
	this.username = username;
	this.email = email;
	this.street = street;
	this.streetNum = streetNum;
	this.city = city;
	this.zipCode = zipCode;
	this.phoneNum = phoneNum;
	this.password = password;
	this.nickName = nickName;
	this.description = description;
	this.photo = phote;
	this.affiliation = affiliation;
	this.myBooks = books;
	this.bookScroll = bookScroll;
	
	if (books != "")
	{
	    this.myBookList = books.split(",");
	}
	else {
	    this.myBookList=new String[1];
	}
    }
    
    public ArrayList<Pair<String, String>> getBookScroll()
    {
	return bookScroll;
    }
    
    public String[] getMyBookList()
    {
	return myBookList;
    }
    
    public String getMyBooks()
    {
	return myBooks;
    }
    
    public String getAffiliation()
    {
	return affiliation;
    }
    
    public String getDescription()
    {
	return description;
    }
    
    public String getCity()
    {
	return city;
    }
    
    public String getEmail()
    {
	return email;
    }
    
    public String getNickName()
    {
	return nickName;
    }
    
    public String getPassword()
    {
	return password;
    }
    
    public String getPhoneNum()
    {
	return phoneNum;
    }
    
    public String getPhoto()
    {
	return photo;
    }
    
    public String getStreet()
    {
	return street;
    }
    
    public String getStreetNum()
    {
	return streetNum;
    }
    
    public String getUsername()
    {
	return username;
    }
    
    public String getZipCode()
    {
	return zipCode;
    }
}
