package com.nugu.uniseoul.data;

import java.io.Serializable;

public class VolData implements Serializable {
    private String v_title;
    private String v_content;
    private String v_num;
    private String current_uni;
    private String max_uni;
    private String current_helper;
    private String max_helper;
    private String s_date;
    private String e_date;
    private String r_date;
    private String r_time;
    private String v_place;



    public String getTitle() {
        return v_title;
    }
    public void setTitle(String title) {
        this.v_title = title;
    }

    public String getContent() {
        return v_content;
    }
    public void setContent(String content) {
        this.v_content = content;
    }


    public String getV_num() {
        return v_num;
    }
    public void setV_num(String content) {
        this.v_num = content;
    }

    public String getCurrent_uni() {
        return current_uni;
    }
    public void setCurrent_uni(String content) { this.current_uni = content;}


    public String getMax_uni() {
        return max_uni;
    }
    public void setMax_uni(String content) { this.max_uni = content;}


    public String getMax_helper() { return max_helper; }
    public void setMax_helper(String content) { this.max_helper = content;}

    public String getCurrent_helper() {
        return current_helper;
    }
    public void setCurrent_helper(String content) { this.current_helper = content;}


    public String getE_date() {
        return e_date;
    }
    public void setE_date(String content) { this.e_date = content;}

    public String getS_date() {
        return s_date;
    }
    public void setS_date(String content) { this.s_date = content;}

    public String getR_date() {
        return r_date;
    }
    public void setR_date(String content) { this.r_date = content;}

    public String getR_time() {
        return r_time;
    }
    public void setR_time(String content) { this.r_time = content;}

    public String getPlace() {
        return v_place;
    }
    public void setPlace(String content) { this.v_place = content;}


}
