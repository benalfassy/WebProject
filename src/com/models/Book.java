package com.models;

/**
 * A simple bean to hold data
 */
public class Book
{
    private String bookName;
    
    private String image;
    
    private int price;
    
    private String description;
    
    private String likes;
    
    private String[] likesList;
    
    private String bookPath;
    
    public Book(String bookName, String image, int price, String description, String likes, String bookPath)
    {
	this.bookName = bookName;
	this.image = image;
	this.price = price;
	this.description = description;
	this.likes = likes;
	this.bookPath = bookPath;
	
	if (likes != null)
	{
	    likesList = likes.split(",");
	}
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
    
    public String getLikes()
    {
	return likes;
    }
    
    public String[] getLikesList()
    {
	return likesList;
    }
    
    public int getPrice()
    {
	return price;
    }
    
}
