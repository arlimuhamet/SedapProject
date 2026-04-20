package com.mycompany.assignmentdemolecturer;

public class Course {

    private String courseCode;
    private String courseTitle;
    private String courseDescription;
    private int creditHours;

    public Course(String courseCode, String courseTitle, String courseDescription, int creditHours) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.creditHours = creditHours;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }
}
