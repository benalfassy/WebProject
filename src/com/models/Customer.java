package com.models;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.util.Pair;

// TODO: Auto-generated Javadoc
/**
 * A simple bean to hold data.
 */
public class Customer
{
    
    /** The Constant ADMIN_AFFILIATION. */
    static public final String ADMIN_AFFILIATION = "Admin";
    
    /** The Constant CUSTOMER_AFFILIATION. */
    static public final String CUSTOMER_AFFILIATION = "User";
    
    /** The username. */
    private String username;
    
    /** The email. */
    private String email;
    
    /** The street. */
    private String street;
    
    /** The street num. */
    private String streetNum;
    
    /** The city. */
    private String city;
    
    /** The zip code. */
    private String zipCode;
    
    /** The phone num. */
    private String phoneNum;
    
    /** The password. */
    private String password;
    
    /** The nick name. */
    private String nickName;
    
    /** The description. */
    private String description;
    
    /** The photo. */
    private String photo;
    
    /** The affiliation. */
    private String affiliation;
    
    /** The my books. */
    private String myBooks;
    
    /** The my book list. */
    private ArrayList<String> myBookList;
    
    /** The book scroll. */
    private ArrayList<Pair<String, String>> bookScroll;
    
    /**
     * Instantiates a new customer.
     *
     * @param username the username
     * @param email the email
     * @param street the street
     * @param streetNum the street num
     * @param city the city
     * @param zipCode the zip code
     * @param phoneNum the phone num
     * @param password the password
     * @param nickName the nick name
     * @param description the description
     * @param phote the phote
     * @param affiliation the affiliation
     * @param books the books
     * @param bookScroll the book scroll
     */
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
	
	if (affiliation.equals(ADMIN_AFFILIATION))
	{
	    this.myBookList = new ArrayList<>();
	    this.myBooks = "All";
	}
	else
	{
	    if (books != "")
	    {
		this.myBookList = new ArrayList<>(Arrays.asList(books.split(",")));
	    }
	    else
	    {
		this.myBookList = new ArrayList<>();
	    }
	}
    }
    
    /**
     * Gets the book scroll.
     *
     * @return the book scroll
     */
    public ArrayList<Pair<String, String>> getBookScroll()
    {
	return bookScroll;
    }
    
    /**
     * Gets the my book list.
     *
     * @return the my book list
     */
    public ArrayList<String> getMyBookList()
    {
	return myBookList;
    }
    
    /**
     * Gets the my books.
     *
     * @return the my books
     */
    public String getMyBooks()
    {
	return myBooks;
    }
    
    /**
     * Gets the affiliation.
     *
     * @return the affiliation
     */
    public String getAffiliation()
    {
	return affiliation;
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription()
    {
	return description;
    }
    
    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity()
    {
	return city;
    }
    
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail()
    {
	return email;
    }
    
    /**
     * Gets the nick name.
     *
     * @return the nick name
     */
    public String getNickName()
    {
	return nickName;
    }
    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword()
    {
	return password;
    }
    
    /**
     * Gets the phone num.
     *
     * @return the phone num
     */
    public String getPhoneNum()
    {
	return phoneNum;
    }
    
    /**
     * Gets the photo.
     *
     * @return the photo
     */
    public String getPhoto()
    {
	return photo;
    }
    
    /**
     * Gets the street.
     *
     * @return the street
     */
    public String getStreet()
    {
	return street;
    }
    
    /**
     * Gets the street num.
     *
     * @return the street num
     */
    public String getStreetNum()
    {
	return streetNum;
    }
    
    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername()
    {
	return username;
    }
    
    /**
     * Gets the zip code.
     *
     * @return the zip code
     */
    public String getZipCode()
    {
	return zipCode;
    }
}
