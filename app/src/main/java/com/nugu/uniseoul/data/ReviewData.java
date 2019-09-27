package com.nugu.uniseoul.data;

import java.io.Serializable;

public class ReviewData implements Serializable {
    private String title;
    private String content;
    private String user_email;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }



    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {return user_email;}
    public void setEmail(String user_email) {this.user_email = user_email;}
}
