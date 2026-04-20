package com.mycompany.assignmentdemolecturer;

public class AcademicRecordItem {

    private String courseTitle;
    private int creditHours;
    private double GPA;
    private String milestone;
    private String recoveryPlan;
    private String recommendations;
    private String attempts;

    public AcademicRecordItem(String courseTitle, int creditHours, double GPA,
                              String milestone, String recoveryPlan,
                              String recommendations, String attempts) {
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.GPA = GPA;
        this.milestone = milestone;
        this.recoveryPlan = recoveryPlan;
        this.recommendations = recommendations;
        this.attempts = attempts;
    }

   
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public double getGPA() { return GPA; }
    public void setGPA(double GPA) { this.GPA = GPA; }

    public String getMilestone() { return milestone; }
    public void setMilestone(String milestone) { this.milestone = milestone; }

    public String getRecoveryPlan() { return recoveryPlan; }
    public void setRecoveryPlan(String recoveryPlan) { this.recoveryPlan = recoveryPlan; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public String getAttempts() { return attempts; }
    public void setAttempts(String attempts) { this.attempts = attempts; }

    @Override
    public String toString() {
        return "{courseTitle: \"" + courseTitle + "\", creditHours: " + creditHours +
                ", GPA: " + GPA + ", milestone: \"" + milestone + "\", recoveryPlan: \"" +
                recoveryPlan + "\", recommendations: \"" + recommendations + "\", attempts: \"" + attempts + "\"}";
    }
}
