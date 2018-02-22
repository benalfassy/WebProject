package com.models;

import java.util.ArrayList;//book.image

/**
 * A simple bean to hold data
 */
public class Book
{
    private String bookName;
    
    private String image;
    
    private int price;
    
    private String description;
    
    private ArrayList<String> likes;
    
    private String bookPath;
    
    public Book(String bookName, String image, int price, String description, ArrayList<String> likes, String bookPath)
    {
	this.bookName = bookName;
	this.image = image;
	this.price = price;
	this.description = description;
	this.likes = likes;
	this.bookPath = bookPath;
    }
    
    public String getBookPath()
    {
	return bookPath;
    }
    
    public String getBookName()
    {
	return bookName;
    }
    
    public String getDescription()
    {
	return description;
    }
    
    public String getImage()
    {
	return image;
    }
    
    public ArrayList<String> getLikes()
    {
	return likes;
    }
    
    public int getPrice()
    {
	return price;
    }
    
}
