package com.google.firebase.quickstart.auth.models;

public class Post {

    public String  Post_URL;
    public boolean Post_IsForPublish;
    public double  Post_Price;
    public int     Post_ID;
    public String  Post_Title;
    public String  Post_Description;

    public Post(){};
    public Post(String Post_URL,
                 double Post_Price,
                 int Post_ID,
                 boolean Post_IsForPublish,
                 String Post_Title,
                 String Post_Description)
    {
        this.Post_URL = Post_URL;
        this.Post_ID = Post_ID;
        this.Post_Price = Post_Price;
        this.Post_IsForPublish = Post_IsForPublish;
        this.Post_Title = Post_Title;
        this.Post_Description = Post_Description;
    }

    public String getPostURL()
    {
        return Post_URL;
    }

    public String getPostTitle()
    {
        return Post_Title;
    }

    public String getPostDescription()
    {
        return Post_Description;
    }

    public int getPostID()
    {
        return Post_ID;
    }

    public String getPostID_String()
    {
        return String.valueOf(Post_ID);
    }

    public double getPostPrice()
    {
        return Post_Price;
    }

    public String getPostPrice_String()
    {
        return String.valueOf(Post_Price);
    }
}
