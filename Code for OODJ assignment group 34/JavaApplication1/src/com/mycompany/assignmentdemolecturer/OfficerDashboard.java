/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;
import java.awt.HeadlessException;
import java.util.Map;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author muhamedarli
 */
public class OfficerDashboard extends javax.swing.JFrame {
    

    /**
     * Creates new form OfficerDashboard
     */
    public OfficerDashboard() {
        initComponents();
        
        try {
        loadStudentsAtRisk();
    } catch (Exception e) {
        System.out.println("Error in constructor: " + e.getMessage());
        e.printStackTrace();
        // Don't show error dialog here, let loadStudentsAtRisk handle it
    }
    
    setVisible(true);
    toFront();
    requestFocus();
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    
    }
    
    /**
 * Load students at risk data into the table
 *//**
 * Load students at risk data into the table - DEBUG VERSION
 */
public void loadStudentsAtRisk() {
    try {
        System.out.println("=== ADJUSTED RISK CRITERIA VERSION ===");
        
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) StudentTable.getModel();
        model.setRowCount(0);
        
        
        Map<String, Map<String, AcademicRecordItem>> recordsMap = recordsRepo.getRecordsMap();
        
        System.out.println("Found " + recordsMap.size() + " students in academic records");
        
        int studentsAtRiskCount = 0;
        
        for (String email : recordsMap.keySet()) {
            Map<String, AcademicRecordItem> courses = recordsMap.get(email);
            
            double totalGPA = 0;
            int courseCount = 0;
            int failedCourses = 0;
            
            System.out.println("Student: " + email);
            
            for (AcademicRecordItem course : courses.values()) {
                double courseGPA = course.getGPA();
                totalGPA += courseGPA;
                courseCount++;
                
                System.out.println("  Course: " + course.getCourseTitle() + " - GPA: " + courseGPA);
                
                if (courseGPA < 2.0) {
                    failedCourses++;
                }
            }
            
            double cgpa = courseCount > 0 ? totalGPA / courseCount : 0;
            
            System.out.println("  CGPA: " + String.format("%.2f", cgpa) + ", Failed courses: " + failedCourses);
            
            // ADJUSTED RISK CRITERIA - More realistic for your data
            boolean isAtRisk = false;
            String riskReason = "";
            
            if (cgpa < 2.0) {
                isAtRisk = true;
                riskReason = "Low CGPA (< 2.0)";
            } else if (failedCourses > 3) {      
                isAtRisk = true;
                riskReason = failedCourses + " failed courses";
            } 
            
            System.out.println("  At risk: " + isAtRisk + " - " + riskReason);
            
            if (isAtRisk) {
                // Find student info
                User user = userRepo.getUser(email);
                String studentName = "Unknown";
                String program = "Unknown";
                
                if (user != null) {
                    studentName = user.getName();
                    if (user instanceof Student) {
                        Student student = (Student) user;
                        program = student.getMajor();
                    }
                } else {
                    studentName = email.split("@")[0]; // Use username part of email
                }
                
                model.addRow(new Object[]{
                    studentName,
                    program,
                    String.format("%.2f", cgpa),
                    riskReason
                });
                
                studentsAtRiskCount++;
                System.out.println("  ✅ ADDED TO TABLE");
            }
            System.out.println("---");
        }
        
        // Update display and label to match new criteria
        StudentRiskLabel.setText("Students at Risk: " + studentsAtRiskCount);
        FilterLabel.setText("Students at Risk (CGPA < 2.0 or any failed courses)");
        
        if (studentsAtRiskCount > 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Found " + studentsAtRiskCount + " students at risk!");
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No at-risk students found with current criteria.");
        }
        
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        StudentRiskPanel = new javax.swing.JPanel();
        StudentRiskLabel = new javax.swing.JLabel();
        RunButton = new javax.swing.JButton();
        GenerateButton = new javax.swing.JButton();
        TablePanel = new javax.swing.JScrollPane();
        StudentTable = new javax.swing.JTable();
        FilterLabel = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuItemDash = new javax.swing.JMenuItem();
        MenuItemEnrol = new javax.swing.JMenuItem();
        MenuItemNot = new javax.swing.JMenuItem();
        MenuItemLog = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel12.setText("Welcome to Officer Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(498, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        StudentRiskPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        StudentRiskLabel.setText("Students at Risk:");

        javax.swing.GroupLayout StudentRiskPanelLayout = new javax.swing.GroupLayout(StudentRiskPanel);
        StudentRiskPanel.setLayout(StudentRiskPanelLayout);
        StudentRiskPanelLayout.setHorizontalGroup(
            StudentRiskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StudentRiskPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentRiskLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );
        StudentRiskPanelLayout.setVerticalGroup(
            StudentRiskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StudentRiskPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(StudentRiskLabel)
                .addGap(15, 15, 15))
        );

        RunButton.setText("Run Eligibility Check");
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunButtonActionPerformed(evt);
            }
        });

        GenerateButton.setText("Generate Reports");
        GenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateButtonActionPerformed(evt);
            }
        });

        StudentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student Nam", "Program", "CGPA", "Failed Courses"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TablePanel.setViewportView(StudentTable);

        FilterLabel.setText("Students at Risk (CGPA < 2.0 or >3 failed courses)");

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton13.setText("Account details");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jMenu1.setText("Menu");

        MenuItemDash.setText("Dashboard");
        MenuItemDash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemDashActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemDash);

        MenuItemEnrol.setText("Enrolment Approval");
        MenuItemEnrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemEnrolActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemEnrol);

        MenuItemNot.setText("Notification Center");
        MenuItemNot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemNotActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemNot);

        MenuItemLog.setText("Log out");
        MenuItemLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemLogActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemLog);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(694, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(StudentRiskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addComponent(FilterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addComponent(TablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(489, 489, 489)
                            .addComponent(RunButton)
                            .addGap(6, 6, 6)
                            .addComponent(GenerateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(StudentRiskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(265, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(96, 96, 96)
                    .addComponent(FilterLabel)
                    .addGap(6, 6, 6)
                    .addComponent(TablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(RunButton)
                        .addComponent(GenerateButton))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
        // TODO add your handling code here:
        // Run Eligibility Check = Refresh the table
        loadStudentsAtRisk();
    }//GEN-LAST:event_RunButtonActionPerformed

    private void GenerateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateButtonActionPerformed
        // TODO add your handling code here:
        
    // Get selected student
    int selectedRow = StudentTable.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Please select a student first!", 
            "No Selection", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        // Get student info
        String studentName = (String) StudentTable.getValueAt(selectedRow, 0);
        String program = (String) StudentTable.getValueAt(selectedRow, 1);
        String cgpa = (String) StudentTable.getValueAt(selectedRow, 2);
        String riskReason = (String) StudentTable.getValueAt(selectedRow, 3);
        
        // Find student by name
        String studentEmail = null;
        for (User user : userRepo.getUserMap().values()) {
            if (user.getName().equals(studentName)) {
                studentEmail = user.getEmail();
                break;
            }
        }
        
        if (studentEmail == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }
        
        // Get courses
        Map<String, AcademicRecordItem> courses = recordsRepo.getStudentRecords(studentEmail);
        
        // Generate PDF report (instead of using AcademicOfficer's method)
        generatePDFReport(studentName, studentEmail, program, cgpa, riskReason, courses);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        e.printStackTrace();
    }

}




private void generatePDFReport(String name, String email, String program, 
                              String cgpa, String riskReason, Map<String, AcademicRecordItem> courses) {
    try {
        // Create filename
        String fileName = name.replace(" ", "_") + "_Report_" + 
                         new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".pdf";
        String downloadsPath = System.getProperty("user.home") + "/Downloads/";
        String filePath = downloadsPath + fileName;
        
        // Create PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
        document.open();
        
        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("Academic Performance Report", titleFont);
        document.add(title);
        document.add(new Paragraph(" "));
        
        // Student Information
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        
        document.add(new Paragraph("STUDENT INFORMATION", boldFont));
        document.add(new Paragraph("Name: " + name, normalFont));
        document.add(new Paragraph("Email: " + email, normalFont));
        document.add(new Paragraph("Program: " + program, normalFont));
        document.add(new Paragraph("CGPA: " + cgpa, normalFont));
        document.add(new Paragraph("Risk Status: " + riskReason, normalFont));
        document.add(new Paragraph(" "));
        
        // Course Details
        document.add(new Paragraph("COURSE DETAILS", boldFont));
        document.add(new Paragraph(" "));
        
        // List each course
        for (Map.Entry<String, AcademicRecordItem> entry : courses.entrySet()) {
            AcademicRecordItem item = entry.getValue();
            double gpa = item.getGPA();
            String status = gpa < 2.0 ? "FAILED" : "PASSED";
            
            document.add(new Paragraph(
                "• " + entry.getKey() + " - " + item.getCourseTitle(), normalFont));
            document.add(new Paragraph(
                "  GPA: " + gpa + " | Credits: " + item.getCreditHours() + 
                " | Status: " + status, normalFont));
            document.add(new Paragraph(" "));
        }
        
        // Footer
        Font smallFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
        Paragraph footer = new Paragraph(
            "Generated on: " + new java.util.Date() + 
            "\nAcademic Officer System", smallFont);
        document.add(footer);
        
        document.close();
        
        // Success message
        JOptionPane.showMessageDialog(this, 
            "✅ PDF Report Created!\n\n" +
            "Saved to: " + fileName + 
            "\nLocation: Downloads folder", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "❌ Error creating PDF: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }


       
    }//GEN-LAST:event_GenerateButtonActionPerformed

    private void MenuItemDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemDashActionPerformed
        // TODO add your handling code here:
        // Dashboard menu item - refresh data
       loadStudentsAtRisk();
       
       
    }//GEN-LAST:event_MenuItemDashActionPerformed

    private void MenuItemEnrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemEnrolActionPerformed
        // TODO add your handling code here:
        OfficerEnrolment enrolment = new OfficerEnrolment();
        enrolment.setVisible(true);
        this.dispose(); // Close current dashboard
        
    }//GEN-LAST:event_MenuItemEnrolActionPerformed

    private void MenuItemLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemLogActionPerformed
        CurrentUser.set(null, null, null, null);
        this.dispose();
        
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
      
    }//GEN-LAST:event_MenuItemLogActionPerformed

    private void MenuItemNotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemNotActionPerformed
        // TODO add your handling code here:
         OfficerNotification notification = new OfficerNotification();
        notification.setVisible(true);
        this.dispose(); // Close current dashboard
    }//GEN-LAST:event_MenuItemNotActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        SwingUtilities.invokeLater(() -> {
            updateUserInfoOfficer ps = new updateUserInfoOfficer();
            ps.setLocationRelativeTo(this);
            ps.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jButton13ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(OfficerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(OfficerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(OfficerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(OfficerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    
    /* ====== ADD THIS CODE - THIS IS WHAT CREATES THE WINDOW ====== */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            System.out.println("Creating OfficerDashboard window...");
            new OfficerDashboard().setVisible(true);
            System.out.println("Window should be visible now!");
        }
    });
        //</editor-fold>
          
        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FilterLabel;
    private javax.swing.JButton GenerateButton;
    private javax.swing.JMenuItem MenuItemDash;
    private javax.swing.JMenuItem MenuItemEnrol;
    private javax.swing.JMenuItem MenuItemLog;
    private javax.swing.JMenuItem MenuItemNot;
    private javax.swing.JButton RunButton;
    private javax.swing.JLabel StudentRiskLabel;
    private javax.swing.JPanel StudentRiskPanel;
    private javax.swing.JTable StudentTable;
    private javax.swing.JScrollPane TablePanel;
    private javax.swing.JButton jButton13;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
