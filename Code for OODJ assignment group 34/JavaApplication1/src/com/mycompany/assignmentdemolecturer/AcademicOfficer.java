/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Issatay
 */
public class AcademicOfficer extends User {


    public AcademicOfficer(String email, String name, String password) {
        super(email, name, password, "AcademicOfficer");
        
    }

    @Override
public String toString() {
    return "AcademicOfficer{" +
            "email='" + getEmail() + '\'' +
            ", name='" + getName() + '\'' +
            ", password='" + getPassword() + '\'' +
            ", type='" + getTypeOfUser() + '\'' +
            '}';
}

public String generateSimpleReport(String name, String program, String cgpa, 
                                   Map<String, AcademicRecordItem> courses) {
    StringBuilder report = new StringBuilder();
    
    report.append("Academic Report\n");
    report.append("===============\n\n");
    
    report.append("Student Name: ").append(name).append("\n");
    report.append("Program: ").append(program).append("\n");
    report.append("CGPA: ").append(cgpa).append("\n\n");
    
    report.append("Course Details:\n");
    report.append("---------------\n");
    
    for (Map.Entry<String, AcademicRecordItem> entry : courses.entrySet()) {
        AcademicRecordItem item = entry.getValue();
        report.append("Course: ").append(entry.getKey()).append(" - ")
              .append(item.getCourseTitle()).append("\n");
        report.append("  GPA: ").append(item.getGPA()).append("\n");
        report.append("  Credit Hours: ").append(item.getCreditHours()).append("\n");
        report.append("  Attempts: ").append(item.getAttempts()).append("\n\n");
    }
    
    report.append("\nReport Generated: ").append(new java.util.Date());
    
    return report.toString();
}

public void sendEmailToStudent(String toEmail, String subject, String message) {
    // Create a Lecturer object to send email
    Lecturer emailSender = new Lecturer(
        CurrentUser.getEmail(),
        CurrentUser.getName(),
        CurrentUser.getPassword(),
        "" // no assigned course needed
    );
    
    String fullMessage = "Dear Student,\n\n" + 
                        message + "\n\nSincerely,\n" + 
                        CurrentUser.getName() + "\nAcademic Office";
    
    emailSender.sendEmail(toEmail, "[Academic Office] " + subject, fullMessage);
}
}

