package com.models;

// TODO: Auto-generated Javadoc
/**
 * The Class LikeRequest.
 */
public class LikeRequest
{
    
    /** The is like. */
    private boolean isLike;
    
    /** The nick name. */
    private String nickName;
    
    /** The book name. */
    private String bookName;
    
    /**
     * Instantiates a new like request.
     *
     * @param nickName the nick name
     * @param bookName the book name
     * @param isLike the is like
     */
    public LikeRequest(String nickName, String bookName, boolean isLike)
    {
	this.nickName = nickName;
	this.bookName = bookName;
	this.isLike = isLike;
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
     * Gets the nick name.
     *
     * @return the nick name
     */
    public String getNickName()
    {
	return nickName;
    }
    
    /**
     * Gets the checks if is like.
     *
     * @return the checks if is like
     */
    public boolean getIsLike()
    {
	return isLike;
    }
}
