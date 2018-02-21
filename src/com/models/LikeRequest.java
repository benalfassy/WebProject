package com.models;

public class LikeRequest
{
    private boolean isLike;
    
    private String nickName;
    
    private String bookName;
    
    public LikeRequest(String nickName, String bookName, boolean isLike)
    {
	this.nickName = nickName;
	this.bookName = bookName;
	this.isLike = isLike;
    }
    
    public String getBookName()
    {
	return bookName;
    }
    
    public String getNickName()
    {
	return nickName;
    }
    
    public boolean getIsLike()
    {
	return isLike;
    }
}
