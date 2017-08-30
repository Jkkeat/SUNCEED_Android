package com.google.firebase.quickstart.auth.models;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public int logintype;

    public User() {}

    public User(String username, String email, int logintype)
    {
        this.username = username; //1.User 2.Admin
        this.email = email;
        this.logintype = logintype;
    }

    public int getlogintype()
    {
        return logintype;
    }

}
