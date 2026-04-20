package com.mycompany.assignmentdemolecturer;

import java.util.Map;
import javax.swing.DefaultListModel;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

public static final String USERS_FILE =
        "/Users/alanabakirov/Desktop/users.txt";
public static final String COURSES_FILE =
        "/Users/alanabakirov/Desktop/courses.txt";
public static final String ACADEMIC_RECORDS_FILE =
        "/Users/alanabakirov/Desktop/academicrecordings.txt";
    
    public static UserRepository userRepo;
    public static CourseRepository courseRepo;
    public static AcademicRecordsRepository recordsRepo;
    
    
    
    public static void main(String[] args) {
        userRepo = new UserRepository(USERS_FILE);
        userRepo.loadFromFile(USERS_FILE);

        courseRepo = new CourseRepository(COURSES_FILE);
        courseRepo.loadFromFile(COURSES_FILE);

        recordsRepo = new AcademicRecordsRepository(ACADEMIC_RECORDS_FILE);
        recordsRepo.loadFromFile(ACADEMIC_RECORDS_FILE);
        CurrentUser.set(null, null, null, null); 
        
        
        System.out.println(userRepo.getUserMap());
        System.out.println(courseRepo.getCourseMap());
        System.out.println(recordsRepo.getRecordsMap());
        System.out.println(userRepo.getUser("alanabakirov@gmail.com").getPassword());
        CurrentUser.set(null, null, null, null);
        
         
             System.out.println("Current User:");
    System.out.println("Email: " + CurrentUser.getEmail());
    System.out.println("Name: " + CurrentUser.getName());
    System.out.println("Password: " + CurrentUser.getPassword());
    System.out.println("Type: " + CurrentUser.getTypeOfUser());
  
        
            SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }
}
