package com.mycompany.assignmentdemolecturer;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class Student extends User {

    private String major;
    private String year;

    public Student(String email, String name, String password, String major, String year) {
        super(email, name, password, "Student");
        this.major = major;
        this.year = year;
    }

    public String getMajor() { 
        return major; 
    }
    
    public String getYear() { 
        return year; 
    }

    public void setMajor(String major) { 
        this.major = major; 
    }
    
    public void setYear(String year) { 
        this.year = year; 
    }
    
    public Map<String, AcademicRecordItem> getAcademicRecords() {
        if (recordsRepo == null) {
            return Collections.emptyMap();
        }
        Map<String, AcademicRecordItem> records = recordsRepo.getStudentRecords(getEmail());
        return records != null ? records : Collections.emptyMap();
    }

    public List<String> getCourseDisplayList() {
        Map<String, AcademicRecordItem> recs = getAcademicRecords();
        List<String> out = new ArrayList<>();
        for (Map.Entry<String, AcademicRecordItem> entry : recs.entrySet()) {
            String code = entry.getKey();
            AcademicRecordItem item = entry.getValue();
            String title = item.getCourseTitle();
            if (courseRepo != null) {
                Course course = courseRepo.getCourse(code);
                if (course != null && course.getCourseTitle() != null) {
                    title = course.getCourseTitle();
                }
            }
            out.add(code + " - " + title);
        }
        
        return out;
    }

    public double calculateOverallWeightedGPA() {
        Map<String, AcademicRecordItem> recs = getAcademicRecords();
        if (recs.isEmpty()) {
            return 0.0;
        }
        double sumPoints = 0.0;
        int totalCredits = 0;
        for (AcademicRecordItem item : recs.values()) {
            sumPoints += item.getGPA() * item.getCreditHours();
            totalCredits += item.getCreditHours();
        }
        
        return totalCredits == 0 ? 0.0 : (sumPoints / totalCredits);
    }

    public double getCourseGPA(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return 0.0;
        }
        AcademicRecordItem item = getAcademicRecords().get(courseCode.trim());
        return item == null ? 0.0 : item.getGPA();
    }

    public int getCourseProgressPercent(String courseCode) {
        double gpa = getCourseGPA(courseCode);
        int percent = (int) Math.round((gpa / 4.0) * 100.0);
        return Math.max(0, Math.min(100, percent));
    }
    
    public String getCourseMilestone(String courseCode) {
        if (courseCode==null || courseCode.trim().isEmpty()) {
            return null;
        }
        AcademicRecordItem item = getAcademicRecords().get(courseCode.trim());
        if (item == null) {
            return null;
        }
        String milestone = item.getMilestone();
        return (milestone != null && !milestone.trim().isEmpty() && !milestone.equalsIgnoreCase("None")) 
                ? milestone : null;
    }

    public String getCourseRecoveryPlan(String courseCode) {
        if (courseCode==null || courseCode.trim().isEmpty()) {
            return null;
        }
        AcademicRecordItem item = getAcademicRecords().get(courseCode.trim());
        if (item == null) {
            return null;
        }
        String plan = item.getRecoveryPlan();
        return (plan != null && !plan.trim().isEmpty() && !plan.equalsIgnoreCase("None")) 
                ? plan : null;
    }

    public String getCourseRecommendations(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return null;
        }
        AcademicRecordItem item = getAcademicRecords().get(courseCode.trim());
        if (item == null) {
            return null;
        }
        String recommendations = item.getRecommendations();
        return (recommendations != null && !recommendations.trim().isEmpty() && !recommendations.equalsIgnoreCase("None")) 
                ? recommendations : null;
    }
    
    public List<Lecturer> getAssignedLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        if (userRepo == null) {
            return lecturers;
        }
        Map<String, AcademicRecordItem> studentRecords = getAcademicRecords();
        if (studentRecords.isEmpty()) {
            System.out.println("Student has no academic records");
            return lecturers;
        }
        Set<String> studentCourseCodes = studentRecords.keySet();
        System.out.println("DEBUG: Student courses: " + studentCourseCodes);
        Map<String, User> allUsers = userRepo.getUserMap();
        if (allUsers == null) {
            return lecturers;
        }
        for (User user : allUsers.values()) {
            if (user instanceof Lecturer) {
                Lecturer lecturer = (Lecturer) user;
                String assignedCourse = lecturer.getAssignedCourse();
                if (assignedCourse != null && studentCourseCodes.contains(assignedCourse.trim())) {
                    lecturers.add(lecturer);
                }
            }
        }
        return lecturers;
    }

    public List<String> getLecturerEmails() {
        List<String> emails = new ArrayList<>();
        List<Lecturer> lecturers = getAssignedLecturers();
        for (Lecturer lecturer : lecturers) {
            if (lecturer.getEmail() != null && !lecturer.getEmail().trim().isEmpty()) {
                emails.add(lecturer.getEmail());
            }
        }
        return emails;
    }
//ADDITIONAL METHOD MB IN FUTURE IMPLEMENTATION
    public List<String> getLecturerDisplayList() {
        List<String> displayList = new ArrayList<>();
        List<Lecturer> lecturers = getAssignedLecturers();
        for (Lecturer lecturer : lecturers) {
            String name = lecturer.getName();
            String email = lecturer.getEmail();
            if (email != null) {
                String display = (name != null ? name : "Unknown") + " (" + email + ")";
                displayList.add(display);
            }
        }
        
        return displayList;
    }
    public Lecturer getLecturerByEmail(String lecturerEmail) {
        if (lecturerEmail == null || lecturerEmail.trim().isEmpty()) {
            return null;
        }
        
        List<Lecturer> lecturers = getAssignedLecturers();
        for (Lecturer lecturer : lecturers) {
            if (lecturerEmail.trim().equalsIgnoreCase(lecturer.getEmail())) {
                return lecturer;
            }
        }
        
        return null;
    }
    
public Lecturer getLecturerForCourse(String courseCode) {
    for (User u : userRepo.userMap.values()) {
        if (u instanceof Lecturer) {
            Lecturer lecturer = (Lecturer) u;
            if (lecturer.getAssignedCourse().equals(courseCode)) {
                return lecturer;
            }
        }
    }
    return null;
}
    
    public boolean sendEmail(String recipientEmail, String subject, String messageBody) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            System.err.println("ERROR: Recipient email is empty");
            return false;
        }
        if (subject == null || subject.trim().isEmpty()) {
            System.err.println("ERROR: Subject is empty");
            return false;
        }
        if (messageBody == null || messageBody.trim().isEmpty()) {
            System.err.println("ERROR: Message body is empty");
            return false;
        }
        final String FROM_EMAIL = "alan.abakirov646@gmail.com";
        final String FROM_PASSWORD = "mnlvoqyxirssoprb";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasAssignedLecturers() {
        return !getAssignedLecturers().isEmpty();
    }

    public int getCourseCount() {
        return getAcademicRecords().size();
    }

    @Override
    public String toString() {
        return "Student{" +
                "email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", major='" + major + '\'' +
                ", year='" + year + '\'' +
                ", courses=" + getCourseCount() +
                ", lecturers=" + getLecturerEmails().size() +
                '}';
    }
}





