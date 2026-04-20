/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class AddUser_admin extends javax.swing.JFrame {
    private String selectedCourse;
    private String userType;
    
    public AddUser_admin(String selectedCourse, String userType) {
        initComponents();
        
        this.selectedCourse = selectedCourse;
        this.userType = userType;
        
        initializeSystem();
        this.setLocationRelativeTo(null);
        setupCloseLabel();
        setupUserTypeSpecificFields();
    }
    
    public AddUser_admin() {
        this("Default Course", "Student");
    }
    
    private void initializeSystem() {
            CourseAdministrator.initializeSystem();
        
    }
    
    private void setupUserTypeSpecificFields() {
    jLabel1.setText("Add " + userType + " - " + selectedCourse);
    
    if ("Student".equals(userType)) {
        jTextField2.setEnabled(true);
        jLabel5.setText("<html>Student's year<br>(number from 1 to 5)</html>");
        jLabel5.setVisible(true);
        jTextField2.setVisible(true);
    } else if ("Lecturer".equals(userType)) {
        jLabel5.setText("Assigned Course");
        jTextField2.setText(selectedCourse);
        jTextField2.setEnabled(false);
        jLabel5.setVisible(true);
        jTextField2.setVisible(true);
    } else if ("AcademicOfficer".equals(userType) || "CourseAdministrator".equals(userType)) {
        // Для AcademicOfficer и CourseAdministrator скрываем дополнительное поле
        jLabel5.setVisible(false);
        jTextField2.setVisible(false);
    }
}
    
    
    
    private void setupCloseLabel() {

    }
    
    private void saveUser() {
        String email = jTextField1.getText().trim();
        String name = jTextField3.getText().trim();
        String password = jTextField4.getText().trim();
        String additionalInfo = jTextField2.getText().trim();
        
        // Валидация
        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email, Name and Password are required", 
                "Missing Information", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if ("Student".equals(userType) && additionalInfo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Year is required for Student", 
                "Missing Year", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка email через CourseAdministrator
        if (!CourseAdministrator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address", 
                "Invalid Email", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка пароля
        if (!CourseAdministrator.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 4 characters", 
                "Invalid Password", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверка года для студента
        if ("Student".equals(userType)) {
            if (!CourseAdministrator.isValidYear(additionalInfo)) {
                JOptionPane.showMessageDialog(this, 
                    "Year must be a number from 1 to 5", 
                    "Invalid Year", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        try {
            // Используем CourseAdministrator для добавления пользователя
            boolean success = CourseAdministrator.addUser(
                email, name, password, userType, selectedCourse, additionalInfo
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "✅ User added successfully!\n\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Type: " + userType + "\n" +
                    "Course: " + selectedCourse,
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearForm();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error adding user: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField2.setText("");
        jTextField1.requestFocus();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">  
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        adminMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));
        jPanel1.setForeground(new java.awt.Color(255, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Add user");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 11, 181, 55));

        jLabel11.setText("jLabel11");
        jLabel11.setAutoscrolls(true);
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 77, 52, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Add user");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 72, 181, 55));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 80));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Email");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 100, 30));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton6.setText("Save updates");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 170, 50));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 460, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Enter user details");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 170, 30));

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 290, 460, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Additional info (for student)");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 260, 110));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Name");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 100, 30));

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, 460, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Password");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 100, 30));

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, 460, 30));

        adminMenu.setText("File");

        jMenuItem1.setText("Admin Dashboard");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        adminMenu.add(jMenuItem1);

        jMenuItem2.setText("Log out");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        adminMenu.add(jMenuItem2);

        jMenuBar1.add(adminMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        saveUser();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        SwingUtilities.invokeLater(() -> {
            AdminDashboard nc = new AdminDashboard();
            nc.setVisible(true);
            this.dispose();
        });
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        CurrentUser.set(null, null, null, null);
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(AddUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddUser_admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu adminMenu;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
