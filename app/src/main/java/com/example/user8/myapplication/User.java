package com.example.user8.myapplication;

public class User
{
    public String u_email,u_username,u_imgURL;

    public String getU_imgURL() {
        return u_imgURL;
    }

    public void setU_imgURL(String u_imgURL) {
        this.u_imgURL = u_imgURL;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_username() {
        return u_username;
    }

    public void setU_username(String u_username) {
        this.u_username = u_username;
    }

    public User(String u_email, String u_username, String u_imgURL)
    {
        this.u_email = u_email;
        this.u_username = u_username;
        this.u_imgURL = u_imgURL;
    }

    User(){}

}
