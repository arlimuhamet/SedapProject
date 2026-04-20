package com.mycompany.assignmentdemolecturer;

public abstract class User {
    private String email;
    private String name;
    private String password;
    private String typeOfUser;

    public User(String email, String name, String password, String typeOfUser) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.typeOfUser = typeOfUser;
    }
    @Override
    public String toString() {
    return "User{" +
            "email='" + getEmail() + '\'' +
            ", name='" + getName() + '\'' +
            ", password='" + getPassword() + '\'' +
            ", type='" + getTypeOfUser() + '\'' +
            '}';
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getTypeOfUser() { return typeOfUser; }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
