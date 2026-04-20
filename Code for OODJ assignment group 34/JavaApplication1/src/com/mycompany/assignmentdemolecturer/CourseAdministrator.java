package com.mycompany.assignmentdemolecturer;

import java.util.*;
import javax.swing.JOptionPane;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import static com.mycompany.assignmentdemolecturer.Main.ACADEMIC_RECORDS_FILE;
import static com.mycompany.assignmentdemolecturer.Main.COURSES_FILE;
import static com.mycompany.assignmentdemolecturer.Main.USERS_FILE;

public class CourseAdministrator extends User {
    
    public CourseAdministrator(String email, String name, String password) {
        super(email, name, password, "CourseAdministrator");
    }
    
    // ============= ПРОВЕРКА ИНИЦИАЛИЗАЦИИ =============
    
    private static void checkRepositories() {
        if (userRepo == null || courseRepo == null) {
            throw new RuntimeException("Repositories not initialized. Call SystemInitializer.initialize() first.");
        }
    }
    
    // ============= USER MANAGEMENT METHODS =============
    
    public static boolean addUser(String email, String name, String password, 
                                 String type, String course, String year) {
        try {
            checkRepositories();
            
            if (userRepo.userMap.containsKey(email)) {
                throw new IllegalArgumentException("User with email " + email + " already exists!");
            }
            
            String[] extraFields = getExtraFieldsForUser(type, course, year);
            userRepo.addUser(email, name, password, type, extraFields);
            
            // Используем путь из Assignment или дефолтный
            String filePath = getUsersFilePath();
            userRepo.saveToFile(filePath);
            return true;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage(), e);
        }
    }
    
    public static boolean isValidCourseCode(String courseCode) {
    if (courseRepo == null) {
        initializeSystem();
    }
    
    if (courseCode == null || courseCode.trim().isEmpty()) {
        return false;
    }
    
    // Проверяем, существует ли курс в репозитории
    return courseRepo.getCourse(courseCode.trim()) != null;
}
    
    public static boolean updateUser(String oldEmail, String newEmail, String name, 
                                String password, String type, String course, String year) {
    if (userRepo == null) {
        initializeSystem();
    }
    
    // Получаем оригинального пользователя
    User originalUser = userRepo.getUser(oldEmail);
    if (originalUser == null) {
        System.out.println("User not found: " + oldEmail);
        return false;
    }
    
    // Проверяем уникальность нового email (если изменился)
    if (!oldEmail.equals(newEmail) && userRepo.getUser(newEmail) != null) {
        System.out.println("Email already exists: " + newEmail);
        return false;
    }
    
    try {
        // Удаляем старого пользователя
        userRepo.getUserMap().remove(oldEmail);
        
        // Создаем обновленного пользователя
        String[] extraFields;
        switch (type) {
            case "Student":
                extraFields = new String[]{course, year}; // major, year
                break;
            case "Lecturer":
                extraFields = new String[]{course}; // assignedCourse
                break;
            case "CourseAdministrator":
            case "AcademicOfficer":
                extraFields = new String[]{}; // пустой массив
                break;
            default:
                System.out.println("Unknown user type: " + type);
                return false;
        }
        
        // Добавляем обновленного пользователя
        if (!type.equals("AcademicOfficer")) userRepo.addUser(newEmail, name, password, type, extraFields);
        else userRepo.addUser(newEmail, name, password, type, "");
        
        // Сохраняем в файл
        userRepo.saveToFile(USERS_FILE);
        
        System.out.println("✓ User updated: " + oldEmail + " → " + newEmail);
        return true;
        
    } catch (Exception e) {
        System.err.println("❌ Error updating user: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    public static boolean deleteUser(String email, String courseCode, String userType) {
    if (userRepo == null) {
        initializeSystem();
    }
    
    try {
        User user = userRepo.getUser(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            return false;
        }
        
        // Нормализуем типы для сравнения
        String normalizedUserTypeFromUser = user.getTypeOfUser().replace(" ", "").trim();
        String normalizedUserType = userType.replace(" ", "").trim();
        
        // Проверяем тип пользователя
        if (!normalizedUserTypeFromUser.equalsIgnoreCase(normalizedUserType)) {
            System.out.println("User type mismatch: expected " + userType + 
                               ", got " + user.getTypeOfUser());
            return false;
        }
        
        // Для AcademicOfficer и CourseAdministrator пропускаем проверку курса
        boolean belongsToCourse = true;
        
//        if (user instanceof Student) {
//            Student s = (Student) user;
//            belongsToCourse = s.getMajor().equals(courseCode);
//            if (!belongsToCourse) {
//                System.out.println("Student " + email + " does not belong to course " + courseCode);
//                return false;
//            }
//        } else if (user instanceof Lecturer) {
//            Lecturer l = (Lecturer) user;
//            belongsToCourse = l.getAssignedCourse().equals(courseCode);
//            if (!belongsToCourse) {
//                System.out.println("Lecturer " + email + " does not belong to course " + courseCode);
//                return false;
//            }
//        }
        // Для AcademicOfficer и CourseAdministrator всегда true
        
        // Удаляем пользователя
        userRepo.getUserMap().remove(email);
        
        // Сохраняем изменения
        userRepo.saveToFile(USERS_FILE);
        
        System.out.println("✓ User deleted: " + email + " (" + user.getName() + ")");
        return true;
        
    } catch (Exception e) {
        System.err.println("❌ Error deleting user: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    // ============= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =============
    
    private static String[] getExtraFieldsForUser(String type, String course, String year) {
        switch (type) {
            case "Student":
                if (year == null || year.isEmpty()) {
                    throw new IllegalArgumentException("Student requires year");
                }
                return new String[]{course, "Year " + year};
            case "Lecturer":
                return new String[]{course};
            case "CourseAdministrator":
            case "AcademicOfficer":
                return new String[]{""};
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
    
    private static String getUsersFilePath() {
        // Если в Assignment задан путь, используем его, иначе дефолтный
        if (userRepo != null) {
            // Попробуем получить путь из репозитория
            try {
                // Если filePath приватное, создадим метод в UserRepository
                return USERS_FILE; // дефолтный путь
            } catch (Exception e) {
                return USERS_FILE;
            }
        }
        return USERS_FILE;
    }
    
    // ============= GETTER METHODS =============
    
    public static Map<String, User> getUsersByCourseAndType(String courseCode, String userType) {
        checkRepositories();
        
        Map<String, User> filteredUsers = new HashMap<>();
        
        for (User user : userRepo.userMap.values()) {
            if (!user.getTypeOfUser().equals(userType)) continue;
            
            if (isUserRelatedToCourse(user, courseCode)) {
                filteredUsers.put(user.getEmail(), user);
            }
        }
        
        return filteredUsers;
    }
    
public static Map<String, User> getListOfStudentsForSubject(String courseId) {

    Map<String, User> result = new HashMap<>();

    // Получаем все записи
    Map<String, Map<String, AcademicRecordItem>> allRecords = recordsRepo.getRecordsMap();

    for (Map.Entry<String, Map<String, AcademicRecordItem>> entry : allRecords.entrySet()) {

        String studentEmail = entry.getKey();
        Map<String, AcademicRecordItem> studentCourses = entry.getValue();

        // Если студент НЕ записан на этот курс → пропускаем
        if (!studentCourses.containsKey(courseId)) continue;

        // Берём пользователя
        User user = userRepo.getUserMap().get(studentEmail);

        // Фильтруем только студентов
        if (user != null && "Student".equals(user.getTypeOfUser())) {
            result.put(studentEmail, user);
        }
    }

    return result;
}
    
    public static Map<String, User> getUsersByType(String userType) {
    Map<String, User> result = new HashMap<>();
    
    if (userRepo == null) {
        initializeSystem();
    }
    
    for (User user : userRepo.getUserMap().values()) {
        if (user.getTypeOfUser().equals(userType)) {
            result.put(user.getEmail(), user);
        }
    }
    
    return result;
}
    
    private static boolean isUserRelatedToCourse(User user, String courseCode) {
        if (user instanceof Student) {
            return ((Student) user).getMajor().equals(courseCode);
        } else if (user instanceof Lecturer) {
            return ((Lecturer) user).getAssignedCourse().equals(courseCode);
        }
        return true;
    }
    
    public static List<String> getAllCoursesForComboBox() {
        checkRepositories();
        
        List<String> courses = new ArrayList<>();
        for (Course course : courseRepo.getCourseMap().values()) {
            courses.add(course.getCourseCode() + " - " + course.getCourseTitle());
        }
        return courses;
    }
    
    // ============= VALIDATION METHODS =============
    
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }
    
    public static boolean isValidYear(String year) {
        if (year == null || year.isEmpty()) return false;
        try {
            int yearNum = Integer.parseInt(year);
            return yearNum >= 1 && yearNum <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // ============= UTILITY METHODS =============
    
    public static String formatUserDisplay(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getName())
          .append(" (").append(user.getEmail()).append(")")
          .append(" - ").append(user.getTypeOfUser());
        
        if (user instanceof Student) {
            Student s = (Student) user;
            sb.append(" | ").append(s.getMajor())
              .append(" | Year ").append(s.getYear().replace("Year ", ""));
        } else if (user instanceof Lecturer) {
            Lecturer l = (Lecturer) user;
            sb.append(" | Course: ").append(l.getAssignedCourse());
        }
        
        return sb.toString();
    }
    
    public static String extractCourseCode(String comboBoxText) {
        if (comboBoxText == null || !comboBoxText.contains(" - ")) {
            return comboBoxText;
        }
        return comboBoxText.split(" - ")[0];
    }
    
    @Override
    public String toString() {
        return "CourseAdministrator{" +
                "email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", type='" + getTypeOfUser() + '\'' +
                '}';
    }
    
    public static boolean changeUserRole(String email, String newRole, String course) {
    try {
        checkRepositories();
        
        User user = userRepo.getUser(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + email);
        }
        
        // Извлекаем год если это студент
        String year = "";
        if (user instanceof Student) {
            year = ((Student) user).getYear().replace("Year ", "");
        }
        
        // Удаляем старого пользователя
        userRepo.userMap.remove(email);
        
        // Создаем нового с другой ролью
        String[] extraFields = getExtraFieldsForUser(newRole, course, year);
        userRepo.addUser(email, user.getName(), user.getPassword(), newRole, extraFields);
        
        // Сохраняем
        userRepo.saveToFile(USERS_FILE);
        return true;
        
    } catch (Exception e) {
        throw new RuntimeException("Failed to change user role: " + e.getMessage(), e);
    }
}
    
    public static User getUserByEmail(String email) {
    checkRepositories();
    
    if (userRepo == null || userRepo.getUserMap() == null) {
        return null;
    }
    
    return userRepo.getUser(email);
}
    
    public static void initializeSystem() {
        if (userRepo == null) {
            String usersFile = "src/main/java/admin/users.txt";
            String coursesFile = "src/main/java/admin/courses.txt";
            
            userRepo = new UserRepository(usersFile);
            userRepo.loadFromFile(usersFile);
            courseRepo = new CourseRepository(coursesFile);
            courseRepo.loadFromFile(coursesFile);
        }
    }
}