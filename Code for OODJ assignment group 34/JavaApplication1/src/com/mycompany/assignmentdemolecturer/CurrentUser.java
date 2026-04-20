/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignmentdemolecturer;

/**
 *
 * @author Issatay
 */
public class CurrentUser {

    private static String email;
    private static String name;
    private static String password;
    private static String typeOfUser;

    public static void set(String email, String name, String password, String typeOfUser) {
        CurrentUser.email = email;
        CurrentUser.name = name;
        CurrentUser.password = password;
        CurrentUser.typeOfUser = typeOfUser;
    }

    public static String getEmail() { return email; }
    public static String getName() { return name; }
    public static String getPassword() { return password; }
    public static String getTypeOfUser() { return typeOfUser; }
}

