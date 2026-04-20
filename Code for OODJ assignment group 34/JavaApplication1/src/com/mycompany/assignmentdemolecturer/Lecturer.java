package com.mycompany.assignmentdemolecturer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import static com.mycompany.assignmentdemolecturer.Main.ACADEMIC_RECORDS_FILE;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Lecturer extends User {

    private String assignedCourse;

    public Lecturer(String email, String name, String password, String assignedCourse) {
        super(email, name, password, "Lecturer");
        this.assignedCourse = assignedCourse;
    }
    @Override
public String toString() {
    return "Lecturer{" +
            "email='" + getEmail() + '\'' +
            ", name='" + getName() + '\'' +
            ", password='" + getPassword() + '\'' +
            ", type='" + getTypeOfUser() + '\'' +
            ", assignedCourse='" + getAssignedCourse() + '\'' +
            '}';
}

    public String getAssignedCourse() { return assignedCourse; }
    public void setAssignedCourse(String assignedCourse) { this.assignedCourse = assignedCourse; }
    
    public List<String> loadProblemStudents() {

        String courseId = this.getAssignedCourse();

        Course course = courseRepo.getCourseMap().get(courseId);
        String courseTitle = course.getCourseTitle();

        System.out.println("Lecturer course: " + courseTitle);


        // Список для результата
        List<String> filteredStudents = new ArrayList<>();

        // Все записи
        Map<String, Map<String, AcademicRecordItem>> allRecords = recordsRepo.getRecordsMap();

        for (Map.Entry<String, Map<String, AcademicRecordItem>> studentEntry : allRecords.entrySet()) {

            String studentEmail = studentEntry.getKey();
            Map<String, AcademicRecordItem> studentCourses = studentEntry.getValue();


            // У студента нет курса этого лектора → пропускаем
            if (!studentCourses.containsKey(courseId)) continue;

            AcademicRecordItem recordItem = studentCourses.get(courseId);

            double gpa = recordItem.getGPA();

            // GPA >= 2 — пропускаем
            if (gpa >= 2) continue;


            // Формируем строку ТОЛЬКО ДЛЯ КУРСА ЛЕКТОРА
            Student student = (Student) userRepo.getUserMap().get(studentEmail);

            String line = student.getName()
                         + ": "
                         + courseId
                         + " - "
                         + recordItem.getGPA()
                         + ", "
                         + recordItem.getRecoveryPlan();

            filteredStudents.add(line);
        }

        String result = String.join("\n", filteredStudents);
        System.out.println(result);
        return filteredStudents;
}
        public void sendEmail(String toEmail, String subject, String messageText) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("alan.abakirov646@gmail.com", "mnlvoqyxirssoprb");
            }
        });


        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("alan.abakirov646@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageText);


            Transport.send(message);

            System.out.println("Письмо успешно отправлено на " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
        
    public void updateStudentsRecords(String email, String newMilestone, String newTask, String newRecommendation) {
        Map<String, Map<String, AcademicRecordItem>> recordsMap = recordsRepo.getRecordsMap();
        
        Lecturer lecturer = (Lecturer) userRepo.getUserMap().get(CurrentUser.getEmail());
        
        String courseCode = lecturer.getAssignedCourse();

        Map<String, AcademicRecordItem> userCourses = recordsMap.get(email);

        AcademicRecordItem courseRecord = userCourses.get(courseCode);

        courseRecord.setMilestone(newMilestone);
        courseRecord.setRecoveryPlan(newTask);
        courseRecord.setRecommendations(newRecommendation);

        userCourses.put(courseCode, courseRecord);
        recordsMap.put(email, userCourses);

        recordsRepo.setRecordsMap(recordsMap);
        recordsRepo.saveToFile(ACADEMIC_RECORDS_FILE);

        System.out.println("Record updated successfully!");
    }
    
        public void generatePdfToDesktop(String name, String course, String grade, String milestone, String task, String recommendation) {
        try {
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            String pdfPath = desktopPath + File.separator + name + " report.pdf";

            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            document.open();

            document.add(new Paragraph("This PDF is report, which is created for lecturer to study current state of student:"));
            document.add(new Paragraph("Student name is "+name+". Its overall review of "+course+" course. Student's GPA for this module is "+grade));
            document.add(new Paragraph("Current student milestone: "+milestone));
            document.add(new Paragraph("Current student task: "+task));
            document.add(new Paragraph("Recommendation given for student: "+recommendation));

            document.close();

            System.out.println("PDF created successfully at: " + pdfPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
