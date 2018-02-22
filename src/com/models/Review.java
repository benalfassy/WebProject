package com.models;

// TODO: Auto-generated Javadoc
/**
 * A simple bean to hold data.
 */
public class Review
{
    
    /** The review id. */
    private String reviewId;
    
    /** The book name. */
    private String bookName;
    
    /** The review. */
    private String review;
    
    /** The reviewer username. */
    private String reviewerUsername;
    
    /** The is approved. */
    private int isApproved;
    
    /** The date. */
    private String date;
    
    /**
     * Instantiates a new review.
     *
     * @param reviewId the review id
     * @param bookName the book name
     * @param review the review
     * @param reviewerUsername the reviewer username
     * @param isApproved the is approved
     * @param date the date
     */
    public Review(String reviewId, String bookName, String review, String reviewerUsername, int isApproved, String date)
    {
	this.reviewId = reviewId;
	this.bookName = bookName;
	this.review = review;
	this.reviewerUsername = reviewerUsername;
	this.isApproved = isApproved;
	this.date = date;
    }
    
    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate()
    {
	return date;
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
     * Gets the checks if is approved.
     *
     * @return the checks if is approved
     */
    public int getIsApproved()
    {
	return isApproved;
    }
    
    /**
     * Gets the review.
     *
     * @return the review
     */
    public String getReview()
    {
	return review;
    }
    
    /**
     * Gets the reviewer username.
     *
     * @return the reviewer username
     */
    public String getReviewerUsername()
    {
	return reviewerUsername;
    }
    
    /**
     * Gets the review id.
     *
     * @return the review id
     */
    public String getReviewId()
    {
	return reviewId;
    }
    
}
