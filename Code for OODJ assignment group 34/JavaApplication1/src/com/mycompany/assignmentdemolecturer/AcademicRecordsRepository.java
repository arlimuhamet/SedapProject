package com.mycompany.assignmentdemolecturer;

import java.io.*;
import java.util.*;

public class AcademicRecordsRepository {

    private Map<String, Map<String, AcademicRecordItem>> recordsMap = new HashMap<>();
    private String filePath;

    public AcademicRecordsRepository(String filePath) {
        this.filePath = filePath;
        loadFromFile(filePath);
    }

    // Загрузка данных из файла
    public void loadFromFile(String filePath) {
    recordsMap.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(";");
            String email = parts[0].trim();
            Map<String, AcademicRecordItem> studentCourses = new HashMap<>();

            for (int i = 1; i < parts.length; i++) {
                String[] fields = parts[i].split(",", -1);
                if (fields.length < 8) continue;

                String courseCode = fields[0].trim();
                String courseTitle = fields[1].trim();
                int creditHours = Integer.parseInt(fields[2].trim());
                double GPA = Double.parseDouble(fields[3].trim());
                String milestone = fields[4].trim();
                String recoveryPlan = fields[5].trim();
                String recommendations = fields[6].trim();
                String attempts = fields[7].trim();

                AcademicRecordItem item = new AcademicRecordItem(
                        courseTitle, creditHours, GPA, milestone, recoveryPlan, recommendations, attempts
                );
                studentCourses.put(courseCode, item);
            }

            recordsMap.put(email, studentCourses);
        }

    } catch (IOException e) {
        System.err.println("Error loading academic records: " + e.getMessage());
    }
}


    // Получить академические записи студента
    public Map<String, AcademicRecordItem> getStudentRecords(String email) {
        return recordsMap.getOrDefault(email, new HashMap<>());
    }


    // Удалить курс студента
    public void removeCourse(String email, String courseCode) {
        if (recordsMap.containsKey(email)) {
            recordsMap.get(email).remove(courseCode);
        }
    }

    // Сохранить все данные обратно в файл
    public void saveToFile(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (String email : recordsMap.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(email);

            Map<String, AcademicRecordItem> courses = recordsMap.get(email);
            for (String courseCode : courses.keySet()) {
                AcademicRecordItem item = courses.get(courseCode);
                sb.append(";")
                  .append(courseCode).append(",")
                  .append(item.getCourseTitle()).append(",")
                  .append(item.getCreditHours()).append(",")
                  .append(item.getGPA()).append(",")
                  .append(item.getMilestone()).append(",")
                  .append(item.getRecoveryPlan()).append(",")
                  .append(item.getRecommendations()).append(",")
                  .append(item.getAttempts());
            }
            writer.write(sb.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error saving academic records: " + e.getMessage());
    }
}


    // Получить полную карту записей
    public Map<String, Map<String, AcademicRecordItem>> getRecordsMap() {
        return recordsMap;
    }
    
    public void setRecordsMap(Map<String, Map<String, AcademicRecordItem>> newRecordsMap) {
    if (newRecordsMap == null) {
        this.recordsMap = new HashMap<>();
    } else {
        this.recordsMap = newRecordsMap;
    }
}
}
