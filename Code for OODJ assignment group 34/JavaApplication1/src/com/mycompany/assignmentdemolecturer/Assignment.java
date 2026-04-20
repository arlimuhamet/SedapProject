//package com.mycompany.assignmentdemolecturer;
//
//import java.util.Map;
//import javax.swing.SwingUtilities;
//import assignment.Login;
//
//public class Assignment {
//
//public static final String USERS_FILE =
//        "/Users/alanabakirov/Desktop/users.txt";
//public static final String COURSES_FILE =
//        "/Users/alanabakirov/Desktop/courses.txt";
//public static final String ACADEMIC_RECORDS_FILE =
//        "/Users/alanabakirov/Desktop/academicrecordings.txt";
//
//    
//    public static UserRepository userRepo;
//    public static CourseRepository courseRepo;
//    public static AcademicRecordsRepository recordsRepo;
//    
//    
//    public static void main(String[] args) {
//        userRepo = new UserRepository(USERS_FILE);
//        userRepo.loadFromFile(USERS_FILE);
//        courseRepo = new CourseRepository(COURSES_FILE);
//        courseRepo.loadFromFile(COURSES_FILE);
//        recordsRepo = new AcademicRecordsRepository(ACADEMIC_RECORDS_FILE);
//        recordsRepo.loadFromFile(ACADEMIC_RECORDS_FILE);
//        
//        CurrentUser.set(null, null, null, null); 
//        
//        AppContext.init(USERS_FILE, COURSES_FILE, ACADEMIC_RECORDS_FILE);
//        
//        userRepo = AppContext.getUserRepo();
//        courseRepo = AppContext.getCourseRepo();
//        recordsRepo = AppContext.getRecordsRepo();
//        
//        SwingUtilities.invokeLater(() -> {
//            Login login = new Login();
//            login.setLocationRelativeTo(null);
//            login.setVisible(true);
//        });
//       
//        
//                
//        
//    }
//}
