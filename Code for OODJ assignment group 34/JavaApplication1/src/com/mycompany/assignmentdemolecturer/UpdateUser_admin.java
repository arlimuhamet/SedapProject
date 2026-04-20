/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import javax.swing.SwingUtilities;

import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;

public class UpdateUser_admin extends javax.swing.JFrame {
    private String selectedCourse;
    private String userType;
    private Map<String, User> comboItemsMap = new HashMap<>();

    public UpdateUser_admin(String selectedCourse, String userType) {
        initComponents();
        jTextField2.setVisible(false);
        this.selectedCourse = selectedCourse;
        this.userType = userType;
        this.setLocationRelativeTo(null);
        
        // Инициализируем систему через CourseAdministrator
        initializeSystem();
        
        loadUsersComboBox();
        setupCloseLabel();
        updateWindowTitle();
    }
    
    public UpdateUser_admin() {
        this("Physics II", "Student");
    }
    
    private void setupUserTypeSpecificFields() {
    jLabel1.setText("Update " + userType + " - " + selectedCourse);
    
    if (userType.equals("Student")) {
        jLabel6.setText("Year (1-5)");
        jTextField2.setEnabled(true);
    } else if (userType.equals("Lecturer")) {
        jLabel6.setText("Assigned Course (Course Code)");
        jTextField2.setEnabled(true);
    } else if (userType.equals("CourseAdministrator") || userType.equals("AcademicOfficer")) {
        // Для CourseAdministrator и AcademicOfficer скрываем/отключаем поле
        //jLabel6.setText("No additional info required");
//        jTextField2.setEnabled(false);
//        jTextField2.setText("");
        jTextField2.setVisible(false); // Можно скрыть
        jLabel6.setVisible(false);
//    } else {
//        jLabel6.setText("Additional Info");
//        jTextField2.setEnabled(true);
    }
}
    
    private void initializeSystem() {
        // Используем CourseAdministrator для инициализации
        if (userRepo == null) {
            CourseAdministrator.initializeSystem();
        }
    }
    
    private void updateWindowTitle() {
        jLabel1.setText("Update " + userType + " - " + selectedCourse);
    }
    
    private void setupCloseLabel() {

    }
    
    private void loadUsersComboBox() {
    jComboBox2.removeAllItems();
    comboItemsMap.clear();
    
    if (userType.equals("Lecturer")) {

        Map<String, User> users = CourseAdministrator.getUsersByCourseAndType(selectedCourse, userType);
        
        if (users.isEmpty()) {
            jComboBox2.addItem("No " + userType + "s available for " + selectedCourse);
            return;
        }
        
        // Заполняем комбобокс
        for (User user : users.values()) {
            String displayText = CourseAdministrator.formatUserDisplay(user);
            jComboBox2.addItem(displayText);
            comboItemsMap.put(displayText, user);
        }
    }else if(userType.equals("Student")){
        Map<String, User> users = CourseAdministrator.getListOfStudentsForSubject(selectedCourse);
        
        if (users.isEmpty()) {
            jComboBox2.addItem("No " + userType + "s available for " + selectedCourse);
            return;
        }
        
        // Заполняем комбобокс
        for (User user : users.values()) {
            String displayText = CourseAdministrator.formatUserDisplay(user);
            jComboBox2.addItem(displayText);
            comboItemsMap.put(displayText, user);
        }
        
    }else if (userType.equals("CourseAdministrator") || userType.equals("AcademicOfficer")) {
        // Для CourseAdministrator и AcademicOfficer загружаем всех
        if (userRepo != null) {
            int count = 0;
            for (User user : userRepo.getUserMap().values()) {
                if (user.getTypeOfUser().equals(userType)) {
                    String displayText = user.getName() + " - " + user.getEmail();
                    jComboBox2.addItem(displayText);
                    comboItemsMap.put(displayText, user);
                    count++;
                }
            }
            
            if (count == 0) {
                jComboBox2.addItem("No " + userType + "s available");
                return;
            }
        }
    }
    
    // Автоматически загружаем первого пользователя
    if (jComboBox2.getItemCount() > 0) {
        jComboBox2.setSelectedIndex(0);
        loadSelectedUserData();
    }
}
    
    private void loadSelectedUserData() {
        String selectedText = (String) jComboBox2.getSelectedItem();
        
        if (selectedText != null && comboItemsMap.containsKey(selectedText)) {
            User user = comboItemsMap.get(selectedText);
            displayUserInTextArea(user);
//            updateAdditionalInfoLabel(user);
        }
    }
    
//    private void updateAdditionalInfoLabel(User user) {
//    if (user instanceof Student) {
//        jLabel6.setText("Year (1-5)");
//        jTextField2.setEnabled(true);
//    } else if (user instanceof Lecturer) {
//        jLabel6.setText("Assigned Course (Course Code)");
//        jTextField2.setEnabled(true);
//    } else if (user instanceof CourseAdministrator) {
//        jLabel6.setVisible(false);
//        jTextField2.setVisible(false);
////        jTextField2.setText("");
//    } else if (user instanceof AcademicOfficer) {
//        jLabel6.setVisible(false);
//        jTextField2.setVisible(false);
//    } else {
//        jLabel6.setText("Additional Info");
//        jTextField2.setEnabled(true);
//    }
//}

private void displayUserInTextArea(User user) {
    jTextField1.setText(user.getEmail());
    jTextField3.setText(user.getName());
    jTextField4.setText(user.getPassword());
    
    if (user instanceof Student) {
        Student s = (Student) user;
        // Извлекаем только номер года (без "Year ")
        String year = s.getYear().replace("Year", "").trim();
        jTextField2.setText(year);
        jTextField2.setVisible(true);
        jLabel6.setText("Year (1-5)");
    } else if (user instanceof Lecturer) {
        Lecturer l = (Lecturer) user;
        jTextField2.setText(l.getAssignedCourse());
        //jTextField2.setEnabled(true);
        jLabel6.setText("<html>Assigned Course <br>(Course Code)</html>");
    } else if (user instanceof CourseAdministrator) {
        jLabel6.setVisible(false);
        jTextField2.setVisible(false);
    } else if (user instanceof AcademicOfficer) {
        
        jLabel6.setVisible(false);
        jTextField2.setVisible(false);
    } else {
        jTextField2.setText("");
        //jTextField2.setEnabled(true);
        jLabel6.setText("");
    }
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
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
        jLabel1.setText("Update user");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 11, 590, 55));

        jLabel11.setText("jLabel11");
        jLabel11.setAutoscrolls(true);
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(555, 77, 52, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Add user");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 72, 181, 55));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 80));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Pick a user");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 319, Short.MAX_VALUE))
                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 580, 150));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Enter new details");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 190, 40));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton6.setText("Save updates");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 550, 240, 60));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Email");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 100, 30));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 430, 30));

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 470, 430, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 260, 110));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Name");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 100, 30));

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, 430, 30));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Password");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 100, 30));

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 420, 430, 30));

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 740, 250));

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
    String newEmail = jTextField1.getText().trim();
    String name = jTextField3.getText().trim();
    String password = jTextField4.getText().trim();
    String additionalInfo = jTextField2.getText().trim();
    
    // Валидация обязательных полей
    if (newEmail.isEmpty() || name.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Email, Name and Password are required", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Получаем выбранного пользователя
    String selectedText = (String) jComboBox2.getSelectedItem();
    if (selectedText == null || !comboItemsMap.containsKey(selectedText)) {
        JOptionPane.showMessageDialog(this, 
            "Please select a user to update", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    User originalUser = comboItemsMap.get(selectedText);
    String originalEmail = originalUser.getEmail();
    String userType = originalUser.getTypeOfUser();
    
    // Проверяем валидность email
    if (!CourseAdministrator.isValidEmail(newEmail)) {
        JOptionPane.showMessageDialog(this, 
            "Please enter a valid email address", 
            "Invalid Email", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Проверяем валидность пароля
    if (!CourseAdministrator.isValidPassword(password)) {
        JOptionPane.showMessageDialog(this, 
            "Password must be at least 4 characters", 
            "Invalid Password", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Для Student дополнительная проверка года
    if (userType.equals("Student")) {
        if (!CourseAdministrator.isValidYear(additionalInfo)) {
            JOptionPane.showMessageDialog(this, 
                "Year must be a number from 1 to 5", 
                "Invalid Year", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        additionalInfo = "Year " + additionalInfo; // Добавляем "Year " для соответствия формату
    }
    
    // Для Lecturer проверка курса
    else if (userType.equals("Lecturer")) {
        if (additionalInfo.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Course code is required for Lecturer", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Проверяем, существует ли такой курс
        if (!CourseAdministrator.isValidCourseCode(additionalInfo)) {
            JOptionPane.showMessageDialog(this, 
                "Course '" + additionalInfo + "' does not exist.\n" +
                "Please enter a valid course code (e.g., C101, P212)", 
                "Invalid Course", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    // Для CourseAdministrator и AcademicOfficer - игнорируем дополнительное поле
    else if (userType.equals("CourseAdministrator") || userType.equals("AcademicOfficer")) {
        additionalInfo = ""; // Очищаем, так как у них нет дополнительных полей
    }
    
    try {
        // Определяем курс для передачи в updateUser
        String courseForUpdate = "";
        
        if (userType.equals("Student")) {
            courseForUpdate = selectedCourse;
        } else if (userType.equals("Lecturer")) {
            courseForUpdate = additionalInfo;
        }
        // Для AcademicOfficer и CourseAdministrator курс не нужен - оставляем пустую строку
        
        // Используем CourseAdministrator для обновления пользователя
        boolean success = CourseAdministrator.updateUser(
            originalEmail,      // oldEmail
            newEmail,           // newEmail
            name,               // name
            password,           // password
            userType,           // type
            courseForUpdate,    // курс (только для Student и Lecturer)
            userType.equals("Student") ? additionalInfo : "" // Для Student передаем год, для других - пустую строку
        );
        
        if (success) {
            String successMessage = "✅ User updated successfully!\n\n" +
                "Name: " + name + "\n" +
                "Email: " + newEmail + "\n" +
                "Type: " + userType + "\n";
            
            // Добавляем информацию в зависимости от типа пользователя
            if (userType.equals("Student")) {
                successMessage += "Course: " + selectedCourse + "\n";
                successMessage += "Year: " + additionalInfo.replace("Year ", "") + "\n";
            } else if (userType.equals("Lecturer")) {
                successMessage += "Course: " + additionalInfo + "\n";
            }
            
            JOptionPane.showMessageDialog(this, 
                successMessage,
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Обновляем комбобокс с новым курсом (только для Lecturer)
            if (userType.equals("Lecturer") && !additionalInfo.equals(selectedCourse)) {
                selectedCourse = additionalInfo;
                updateWindowTitle();
            }
            
            loadUsersComboBox();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update user. User with this email may already exist.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "❌ Error updating user: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_jButton6ActionPerformed
    private boolean isNumeric(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    try {
        Integer.parseInt(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        loadSelectedUserData();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateUser_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateUser_admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu adminMenu;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
