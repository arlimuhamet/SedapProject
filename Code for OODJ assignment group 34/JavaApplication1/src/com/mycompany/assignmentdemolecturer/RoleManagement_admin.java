/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
package com.mycompany.assignmentdemolecturer;
import static com.mycompany.assignmentdemolecturer.Main.courseRepo;
import static com.mycompany.assignmentdemolecturer.Main.recordsRepo;
import static com.mycompany.assignmentdemolecturer.Main.userRepo;
import javax.swing.SwingUtilities;

import javax.swing.JOptionPane;
import java.util.Map;

public class RoleManagement_admin extends javax.swing.JFrame {
    private String selectedCourse = "";
    
    public RoleManagement_admin(String selectedCourse) {
        this.selectedCourse = selectedCourse;
        initComponents();
        
        // Инициализируем систему если нужно
        if (userRepo == null) {
            String usersFile = "src/main/java/admin/users.txt";
            String coursesFile = "src/main/java/admin/courses.txt";
            userRepo = new UserRepository(usersFile);
            userRepo.loadFromFile(usersFile);
            courseRepo = new CourseRepository(coursesFile);
            courseRepo.loadFromFile(coursesFile);
        }
        
        loadUsersComboBox();
        this.setLocationRelativeTo(null);
        setupCloseLabel();
        
        jLabel1.setText("Role management - " + selectedCourse);
    }
    
    public RoleManagement_admin() {
        this("Default Course");
    }
    
    private void setupCloseLabel() {

    }
    
    private void loadUsersComboBox() {
        jComboBox2.removeAllItems();
        jComboBox3.removeAllItems();
        
        // Используем CourseAdministrator для получения пользователей
        Map<String, User> users = CourseAdministrator.getUsersByCourseAndType(selectedCourse, "AcademicOfficer");
        
        if (users.isEmpty()) {
            jComboBox2.addItem("No users available for this course");
            jComboBox3.addItem("No roles available");
            return;
        }
        
        // Загружаем пользователей
        for (User user : users.values()) {
            String displayText = CourseAdministrator.formatUserDisplay(user);
            jComboBox2.addItem(displayText);
        }
        
        // Загружаем доступные роли
        loadAvailableRoles();
    }
    
    private void loadAvailableRoles() {
        jComboBox3.removeAllItems();
        jComboBox3.addItem("CourseAdministrator");
        // Можно добавить другие роли если нужно
    }
    
    private User extractUserFromSelection(String selectedText) {
        if (selectedText == null || selectedText.startsWith("No users")) {
            return null;
        }
        
        // Извлекаем email из строки формата: "Name (email) - Type | Course: ..."
        try {
            int start = selectedText.indexOf("(");
            int end = selectedText.indexOf(")");
            if (start != -1 && end != -1) {
                String email = selectedText.substring(start + 1, end);
                return CourseAdministrator.getUserByEmail(email);
            }
        } catch (Exception e) {
            System.out.println("Error extracting user: " + e.getMessage());
        }
        return null;
    }
    
    private void saveUserRoleChange() {
        try {
            String selectedUser = (String) jComboBox2.getSelectedItem();
            String newRole = (String) jComboBox3.getSelectedItem();
            
            if (isInvalidSelection(selectedUser, newRole)) {
                return;
            }
            
            User user = extractUserFromSelection(selectedUser);
            if (user == null) {
                showError("Unable to find user in database");
                return;
            }
            
            // Проверяем допустимость смены роли
            if (!isRoleChangeAllowed(user.getTypeOfUser(), newRole)) {
                showRoleChangeError(user.getTypeOfUser(), newRole);
                return;
            }
            
            if (user.getTypeOfUser().equalsIgnoreCase(newRole)) {
                showRoleConflictError(user, newRole);
                return;
            }
            
            if (confirmRoleChange(user, newRole)) {
                // Используем CourseAdministrator для изменения роли
                boolean success = CourseAdministrator.changeUserRole(
                    user.getEmail(), newRole, selectedCourse
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Role changed successfully!\n\n" +
                        "User: " + user.getName() + "\n" +
                        "New role: " + newRole + "\n" +
                        "Updated in database",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    loadUsersComboBox(); // Обновляем список
                }
            }
            
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ============= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =============
    
    private boolean isInvalidSelection(String selectedUser, String newRole) {
        if (selectedUser == null || selectedUser.startsWith("No users") || newRole == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ Invalid selection!\n\n" +
                "Please make sure to:\n" +
                "1. Select a user from the list\n" + 
                "2. Select a new role to assign",
                "Selection Required",
                JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }
    
    private boolean isRoleChangeAllowed(String currentRole, String newRole) {
        // Только Lecturer может стать CourseAdministrator
        if ("AcademicOfficer".equals(currentRole)) {
            return "CourseAdministrator".equals(newRole);
        }
        return false;
    }
    
    private void showRoleChangeError(String currentRole, String newRole) {
        String message = "❌ Role change not allowed!\n\n";
        
        if ("Student".equals(currentRole)) {
            message += "Students cannot change their role.";
        } else if ("Lecturer".equals(currentRole)) {
            message += "Lecturers can only be promoted to Course Administrator.";
        } else if ("CourseAdministrator".equals(currentRole)) {
            message += "Course Administrators cannot change their role.";
        } else if ("AcademicOfficer".equals(currentRole)) {
            message += "Academic Officers cannot change their role.";
        }
        
        JOptionPane.showMessageDialog(this, message, "Role Change Not Allowed", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showRoleConflictError(User user, String newRole) {
        JOptionPane.showMessageDialog(this,
            "❌ Cannot assign same role!\n\n" +
            "User: " + user.getName() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Current role: " + user.getTypeOfUser() + "\n" +
            "Selected role: " + newRole + "\n\n" +
            "The user already has this role assigned.\n" +
            "Please select a different role.",
            "Role Conflict",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean confirmRoleChange(User user, String newRole) {
        int result = JOptionPane.showConfirmDialog(this,
            "🔀 Confirm Role Change\n\n" +
            "User: " + user.getName() + "\n" +
            "Email: " + user.getEmail() + "\n" +
            "Current role: " + user.getTypeOfUser() + "\n" +
            "New role: " + newRole + "\n\n" +
            "⚠️ This action will:\n" +
            "• Change the user's role immediately\n" +
            "• Update the database\n" +
            "• Cannot be undone\n\n" +
            "Are you sure you want to proceed?",
            "Confirm Role Change",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        return result == JOptionPane.YES_OPTION;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ============= UI CODE (остается без изменений) =============

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
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
        jLabel1.setText("Role management");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 11, 260, 55));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 80));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jComboBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Student", "Academic officer", "Lecturer" }));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Pick a user");

        jComboBox3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Student", "Academic officer", "Lecturer" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Assign new role");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, 0, 418, Short.MAX_VALUE)
                    .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(160, 160, 160))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 470, 320));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton6.setText("Save updates");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 160, 40));

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
        // TODO add your handling code here:
        saveUserRoleChange();
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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error setting look and feel: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            new RoleManagement_admin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu adminMenu;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
