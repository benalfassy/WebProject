package com.models;

/**
 * A simple bean to hold data
 */
public class Review
{
    private String reviewId;
    
    private String bookName;
    
    private String review;
    
    private String reviewerUsername;
    
    private int isApproved;
    
    private String date;
    
    public Review(String reviewId, String bookName, String review, String reviewerUsername, int isApproved, String date)
    {
	this.reviewId = reviewId;
	this.bookName = bookName;
	this.review = review;
	this.reviewerUsername = reviewerUsername;
	this.isApproved = isApproved;
	this.date = date;
    }
    
    public String getDate()
    {
	return date;
    }
    
    public String getBookName()
    {
	return bookName;
    }
    
    public int getIsApproved()
    {
	return isApproved;
    }
    
    public String getReview()
    {
	return review;
    }
    
    public String getReviewerUsername()
    {
	return reviewerUsername;
    }
    
    public String getReviewId()
    {
	return reviewId;
    }
    
}
