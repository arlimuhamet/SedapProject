/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;
import static com.mycompany.assignmentdemolecturer.Main.ACADEMIC_RECORDS_FILE;
import static com.mycompany.assignmentdemolecturer.Main.COURSES_FILE;
import static com.mycompany.assignmentdemolecturer.Main.USERS_FILE;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author alanabakirov
 */
public class RecoveryPlansLecturer extends javax.swing.JFrame {

    /**
     * Creates new form RecoveryPlansLecturer
     */
    public RecoveryPlansLecturer() {
        initComponents();
        prepareListToSend();
    }
    
        public void updateStudentsRecords() {
          try {
            String email = (String) jComboBox1.getSelectedItem();
            if (email == null || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a student first!",
                        "No Student Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
          }

          String milestone = milestoneTextfield.getText().trim();
          String task = taskTextfield.getText().trim();
          String recommendation = recommendationTextfield.getText().trim();

          if (milestone.isEmpty()) milestone = "None";
          if (task.isEmpty()) task = "None";
          if (recommendation.isEmpty()) recommendation = "None";


          Lecturer lecturer = (Lecturer) userRepo.getUserMap().get(CurrentUser.getEmail());
          lecturer.updateStudentsRecords(email, milestone, task, recommendation);


          JOptionPane.showMessageDialog(this,
                  "Student record updated successfully!",
                  "Success",
                  JOptionPane.INFORMATION_MESSAGE);

      } catch (Exception e) {
          // Catch any unexpected errors
          e.printStackTrace();
          JOptionPane.showMessageDialog(this,
                  "An error occurred while updating the record: " + e.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
      }
    }
    
    private void loadSelectedStudentInfo() {

        String email = (String) jComboBox1.getSelectedItem();
        if (email == null || email.isEmpty()) return;


        User student = userRepo.getUserMap().get(email);
        if (student == null) return;

        nameLabel.setText("Student: " + student.getName());


        Map<String, Map<String, AcademicRecordItem>> map = recordsRepo.getRecordsMap();
        Map<String, AcademicRecordItem> courses = map.get(email);

        if (courses == null || courses.isEmpty()) {
            courseLabel.setText("Course: none");
            milestoneTextfield.setText("");
            taskTextfield.setText("");
            recommendationTextfield.setText("");
            return;
        }

        Lecturer lecturer = (Lecturer) userRepo.getUserMap().get(CurrentUser.getEmail());
        String firstCourseCode = lecturer.getAssignedCourse();
        AcademicRecordItem item = courses.get(firstCourseCode);

        courseLabel.setText("Course: " + firstCourseCode);

        
        milestoneTextfield.setText(item.getMilestone());
        taskTextfield.setText(item.getRecoveryPlan());
        recommendationTextfield.setText(item.getRecommendations());
}

    
        public void prepareListToSend() {
            try {
                Lecturer lecturer = (Lecturer) userRepo.getUserMap().get(CurrentUser.getEmail());
                if (lecturer == null) return;

                List<String> filteredStudents = lecturer.loadProblemStudents();

                List<String> emails = new ArrayList<>();
                for (String line : filteredStudents) {
                    String studentName = line.split(":")[0].trim();
                    for (User u : userRepo.getUserMap().values()) {
                        if (u instanceof Student && u.getName().equals(studentName)) {
                            emails.add(((Student) u).getEmail());
                            break;
                        }
                    }
                }

                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(emails.toArray(new String[0])));

                
                if (emails.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "You currently have no students under your supervision.",
                            "No Students",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    loadSelectedStudentInfo();
                }

            } catch (Exception e) {
                e.printStackTrace();
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

        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        nameLabel = new javax.swing.JLabel();
        courseLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        milestoneTextfield = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        taskTextfield = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        recommendationTextfield = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        lecturerMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(255, 102, 102));

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel6.setText("Welcome to Recovery Plan Management");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(415, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel7.setText("Select Student:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        nameLabel.setText("Student: ");

        courseLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        courseLabel.setText("Course: ");

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel10.setText("Current milestone: ");

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel11.setText("Current task: ");

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel8.setText("Current recommendation: ");

        jButton2.setText("Update info");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lecturerMenu.setText("File");

        jMenuItem1.setText("Lecturer Dashboard");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        lecturerMenu.add(jMenuItem1);

        jMenuItem2.setText("Notification Center");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        lecturerMenu.add(jMenuItem2);

        jMenuItem3.setText("Change recovery plans");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        lecturerMenu.add(jMenuItem3);

        jMenuItem4.setText("Report feedback");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        lecturerMenu.add(jMenuItem4);

        jMenuItem5.setText("Log out");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        lecturerMenu.add(jMenuItem5);

        jMenuBar1.add(lecturerMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 880, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(nameLabel))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(courseLabel)
                                .addComponent(jLabel10))
                            .addGap(92, 92, 92)
                            .addComponent(milestoneTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(jLabel11)
                            .addGap(140, 140, 140)
                            .addComponent(taskTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(jLabel8)
                            .addGap(30, 30, 30)
                            .addComponent(recommendationTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(330, 330, 330)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addComponent(nameLabel)
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(courseLabel)
                            .addGap(8, 8, 8)
                            .addComponent(jLabel10))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(milestoneTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel11))
                        .addComponent(taskTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel8))
                        .addComponent(recommendationTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(40, 40, 40)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        loadSelectedStudentInfo();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        updateStudentsRecords();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        SwingUtilities.invokeLater(() -> {
            LecturerDashBoard nc = new LecturerDashBoard();
            nc.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        SwingUtilities.invokeLater(() -> {
            NotificationCenterLecturer nc = new NotificationCenterLecturer();
            nc.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        SwingUtilities.invokeLater(() -> {
            RecoveryPlansLecturer nc = new RecoveryPlansLecturer();
            nc.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        SwingUtilities.invokeLater(() -> {
            ReportFeedbackLecturer nc = new ReportFeedbackLecturer();
            nc.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        CurrentUser.set(null, null, null, null);
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
            java.util.logging.Logger.getLogger(RecoveryPlansLecturer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecoveryPlansLecturer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecoveryPlansLecturer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecoveryPlansLecturer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecoveryPlansLecturer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel courseLabel;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JMenu lecturerMenu;
    private javax.swing.JTextField milestoneTextfield;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField recommendationTextfield;
    private javax.swing.JTextField taskTextfield;
    // End of variables declaration//GEN-END:variables
}
