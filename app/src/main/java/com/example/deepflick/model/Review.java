package com.example.deepflick.model;

public class Review {
    //data members
    private String mAuthor;
    private String mContent;
    private String mId;
    private String mUrl;

    //constructor
    public Review(){}
    
    //getter method
    public String getAuthor(){ return mAuthor; }
    //setter method
    public void setAuthor(String mAuthor) { this.mAuthor = mAuthor; }

    public String getContent(){ return mContent; }
    public void setContent(String mContent ){ this.mContent = mContent; }

    public String getId(){ return mId; }
    public void setId(String mId){ this.mId = mId; }

    public String getUrl(){ return mUrl; }
    public void setUrl(String mUrl){ this.mUrl = mUrl; }
}
