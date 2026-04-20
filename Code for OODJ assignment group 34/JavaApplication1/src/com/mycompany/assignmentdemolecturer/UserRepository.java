package com.mycompany.assignmentdemolecturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UserRepository {

    public Map<String, User> userMap = new HashMap<>();
    private String filePath;

    public UserRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath);
    }

    public int getUserCount() {
        return userMap.size();
    }

    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                String email = parts[0].trim();
                String name = parts[1].trim();
                String password = parts[2].trim();
                String type = parts[3].trim();

                User user = null;

                // Classic switch for Java 11
                switch (type) {
                    case "Student":
                        user = new Student(email, name, password, parts[4], parts[5]);
                        break;
                    case "Lecturer":
                        user = new Lecturer(email, name, password, parts[4]);
                        break;
                    case "CourseAdministrator":
                        user = new CourseAdministrator(email, name, password);
                        break;
                    case "AcademicOfficer":
                        user = new AcademicOfficer(email, name, password);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown user type: " + type);
                }

                userMap.put(email, user);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public User getUser(String email) {
        return userMap.get(email);
    }

    public void saveToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            for (User user : userMap.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(user.getEmail()).append(";")
                  .append(user.getName()).append(";")
                  .append(user.getPassword()).append(";")
                  .append(user.getTypeOfUser());

                if (user instanceof Student) {
                    Student s = (Student) user;
                    sb.append(";").append(s.getMajor()).append(";").append(s.getYear());
                } else if (user instanceof Lecturer) {
                    Lecturer l = (Lecturer) user;
                    sb.append(";").append(l.getAssignedCourse());
                } else if (user instanceof CourseAdministrator) {
                    sb.append(";");
                } else if (user instanceof AcademicOfficer) {
                    sb.append(";");
                }

                writer.write(sb.toString() + "\n");
            }

        } catch (IOException e) {
            System.err.println("Error save: " + e.getMessage());
        }
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> newMap) {
        if (newMap == null) {
            throw new IllegalArgumentException("New map cannot be null");
        }
        this.userMap = new HashMap<>(newMap);
    }

    public void addUser(String email,
                        String name,
                        String password,
                        String type,
                        String... extra) {

        if (userMap.containsKey(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists!");
        }

        User user = null;

        // Classic switch for Java 11
        switch (type) {
            case "Student":
                if (extra.length < 2)
                    throw new IllegalArgumentException("Student requires: major, year");
                user = new Student(email, name, password, extra[0], extra[1]);
                break;
            case "Lecturer":
                if (extra.length < 1)
                    throw new IllegalArgumentException("Lecturer requires: assignedCourse");
                user = new Lecturer(email, name, password, extra[0]);
                break;
            case "CourseAdministrator":
                List<String> semesters = Arrays.asList(extra[0].split("\\|"));
                user = new CourseAdministrator(email, name, password);
                break;
            case "AcademicOfficer":
                List<String> years = Arrays.asList(extra[0].split("\\|"));
                user = new AcademicOfficer(email, name, password);
                break;
            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }

        userMap.put(email, user);
    }
}
