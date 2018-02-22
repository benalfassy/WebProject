package com.models;

import java.time.LocalDate;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * A simple bean to hold data.
 */
public class Transaction
{
    
    /** The username. */
    private String username;
    
    /** The book list. */
    private ArrayList<String> bookList;
    
    /** The total price. */
    private int totalPrice;
    
    /** The date. */
    private LocalDate date;
    
    /**
     * Instantiates a new transaction.
     *
     * @param Username the username
     * @param date the date
     * @param bookList the book list
     * @param totalPrice the total price
     */
    public Transaction(String Username, LocalDate date, ArrayList<String> bookList, int totalPrice)
    {
	this.username = Username;
	this.date = date;
	this.bookList = bookList;
	this.totalPrice = totalPrice;
    }
    
    /**
     * Gets the book list.
     *
     * @return the book list
     */
    public ArrayList<String> getBookList()
    {
	return bookList;
    }
    
    /**
     * Gets the date.
     *
     * @return the date
     */
    public LocalDate getDate()
    {
	return date;
    }
    
    /**
     * Gets the total price.
     *
     * @return the total price
     */
    public int getTotalPrice()
    {
	return totalPrice;
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
    
}
