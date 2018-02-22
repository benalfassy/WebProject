package com.models;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * A simple bean to hold data.
 */
public class Book
{
    
    /** The book name. */
    private String bookName;
    
    /** The image. */
    private String image;
    
    /** The price. */
    private int price;
    
    /** The description. */
    private String description;
    
    /** The likes. */
    private ArrayList<String> likes;
    
    /** The book path. */
    private String bookPath;
    
    /**
     * Instantiates a new book.
     *
     * @param bookName the book name
     * @param image the image
     * @param price the price
     * @param description the description
     * @param likes the likes
     * @param bookPath the book path
     */
    public Book(String bookName, String image, int price, String description, ArrayList<String> likes, String bookPath)
    {
	this.bookName = bookName;
	this.image = image;
	this.price = price;
	this.description = description;
	this.likes = likes;
	this.bookPath = bookPath;
    }
    
    /**
     * Gets the book path.
     *
     * @return the book path
     */
    public String getBookPath()
    {
	return bookPath;
    }
    
    /**
     * Gets the book name.
     *
     * @return the book name
     */
    public String getBookName()
    {
	return bookName;
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
     * Gets the image.
     *
     * @return the image
     */
    public String getImage()
    {
	return image;
    }
    
    /**
     * Gets the likes.
     *
     * @return the likes
     */
    public ArrayList<String> getLikes()
    {
	return likes;
    }
    
    /**
     * Gets the price.
     *
     * @return the price
     */
    public int getPrice()
    {
	return price;
    }
    
}
