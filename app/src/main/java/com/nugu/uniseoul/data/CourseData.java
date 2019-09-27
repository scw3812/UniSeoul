package com.nugu.uniseoul.data;

import java.io.Serializable;

public class CourseData implements Serializable {
    private String courseImage;
    private String courseTitle;
    private String courseHomepage;
    private String courseAddress;
    private String[] courseBarrierFree;
    private String courseTripBarrierFree;
    private String courseTheme;
    private String courseTel;

    public String getCourseTel() {
        return courseTel;
    }

    public void setCourseTel(String courseTel) {
        this.courseTel = courseTel;
    }

    public String getCourseTheme() {
        return courseTheme;
    }

    public void setCourseTheme(String courseTheme) {
        this.courseTheme = courseTheme;
    }

    public String getCourseTripBarrierFree() {
        return courseTripBarrierFree;
    }

    public void setCourseTripBarrierFree(String courseTripBarrierFree) {
        this.courseTripBarrierFree = courseTripBarrierFree;
    }

    public String[] getCourseBarrierFree() {
        return courseBarrierFree;
    }

    public void setCourseBarrierFree(String[] courseBarrierFree) {
        this.courseBarrierFree = courseBarrierFree;
    }

    public String getCourseAddress() {
        return courseAddress;
    }

    public void setCourseAddress(String address) {
        this.courseAddress = address;
    }

    public String getCourseHomepage() {
        return courseHomepage;
    }

    public void setCourseHomepage(String courseHomepage) {
        this.courseHomepage = courseHomepage;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
