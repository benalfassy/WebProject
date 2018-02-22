package com.models;

// TODO: Auto-generated Javadoc
/**
 * A simple bean to hold data.
 */
public class Message
{
    
    /** The message id. */
    private String messageId;
    
    /** The from. */
    private String from;
    
    /** The to. */
    private String to;
    
    /** The content. */
    private String content;
    
    /** The date. */
    private String date;
    
    /** The is viewed. */
    private int isViewed;
    
    /**
     * Instantiates a new message.
     *
     * @param messageId the message id
     * @param from the from
     * @param to the to
     * @param content the content
     * @param date the date
     * @param isViewed the is viewed
     */
    public Message(String messageId, String from, String to, String content, String date, int isViewed)
    {
	this.messageId = messageId;
	this.from = from;
	this.to = to;
	this.content = content;
	this.date = date;
	this.isViewed = isViewed;
    }
    
    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent()
    {
	return content;
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
     * Gets the from.
     *
     * @return the from
     */
    public String getFrom()
    {
	return from;
    }
    
    /**
     * Gets the checks if is viewed.
     *
     * @return the checks if is viewed
     */
    public int getIsViewed()
    {
	return isViewed;
    }
    
    /**
     * Gets the message id.
     *
     * @return the message id
     */
    public String getMessageId()
    {
	return messageId;
    }
    
    /**
     * Gets the to.
     *
     * @return the to
     */
    public String getTo()
    {
	return to;
    }
    
}
