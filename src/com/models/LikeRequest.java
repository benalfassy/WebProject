package com.models;

public class LikeRequest
{
    private boolean isLike;
    
    private String username;
    
    private String bookName;
    
    public LikeRequest(String username, String bookName, boolean isLike)
    {
	this.username = username;
	this.bookName = bookName;
	this.isLike = isLike;
    }
    
    public String getBookName()
    {
	return bookName;
    }
    
    public String getUsername()
    {
	return username;
    }
    
    public boolean getIsLike()
    {
	return isLike;
    }
}
