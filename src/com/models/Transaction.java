package com.models;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple bean to hold data
 */
public class Transaction
{
    private String username;
    
    private ArrayList<String> bookList;
    
    private int totalPrice;
    
    private LocalDate date;
    
    public Transaction(String Username, LocalDate date, ArrayList<String> bookList, int totalPrice)
    {
	this.username = Username;
	this.date = date;
	this.bookList = bookList;
	this.totalPrice = totalPrice;
    }
    
    public ArrayList<String> getBookList()
    {
	return bookList;
    }
    
    public LocalDate getDate()
    {
	return date;
    }
    
    public int getTotalPrice()
    {
	return totalPrice;
    }
    
    public String getUsername()
    {
	return username;
    }
    
}
