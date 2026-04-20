package com.mycompany.assignmentdemolecturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CourseRepository {

    private Map<String, Course> courseMap = new HashMap<>();
    private String filePath;

    public CourseRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath);
    }


    // Загрузка курсов из файла
    public void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(";", 4);
                if (parts.length < 4) continue;

                String courseCode = parts[0].trim();
                String courseTitle = parts[1].trim();
                String courseDescription = parts[2].trim();
                int creditHours = Integer.parseInt(parts[3].trim());

                Course course = new Course(courseCode, courseTitle, courseDescription, creditHours);
                courseMap.put(courseCode, course);
            }

        } catch (IOException e) {
            System.err.println("Error loading courses: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in course file: " + e.getMessage());
        }
    }

    // Получить курс по коду
    public Course getCourse(String courseCode) {
        return courseMap.get(courseCode);
    }
    // Удалить курс
    public void removeCourse(String courseCode) {
        courseMap.remove(courseCode);
    }

    // Сохранить все курсы в файл
    public void saveToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Course course : courseMap.values()) {
                String line = String.join(";",
                        course.getCourseCode(),
                        course.getCourseTitle(),
                        course.getCourseDescription(),
                        String.valueOf(course.getCreditHours())
                );
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    public Map<String, Course> getCourseMap() {
        return courseMap;
    }

    public void setCourseMap(Map<String, Course> newMap) {
        if (newMap == null) throw new IllegalArgumentException("New map cannot be null");
        this.courseMap = new HashMap<>(newMap);
    }
}
