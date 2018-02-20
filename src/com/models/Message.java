package com.models;

/**
 * A simple bean to hold data
 */
public class Message
{
    private String messageId;
    
    private String from;
    
    private String to;
    
    private String content;
    
    private String date;
    
    private int isViewed;
    
    public Message(String messageId, String from, String to, String content, String date, int isViewed)
    {
	this.messageId = messageId;
	this.from = from;
	this.to = to;
	this.content = content;
	this.date = date;
	this.isViewed = isViewed;
    }
    
    public String getContent()
    {
	return content;
    }
    
    public String getDate()
    {
	return date;
    }
    
    public String getFrom()
    {
	return from;
    }
    
    public int getIsViewed()
    {
	return isViewed;
    }
    
    public String getMessageId()
    {
	return messageId;
    }
    
    public String getTo()
    {
	return to;
    }
    
}
