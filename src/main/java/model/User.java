/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nadis
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private String status;
    private int staffId;
    private String customId;
    private String contact_no;
    private String whatsapp_no;
    private String address;

    public User() {}
    
     public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String username, String email, String password, String role, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(int userId, String username, String email, String password, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public String getStatus() {return  status;}
    public void setStatus(String status) {this.status = status;}

    public int getStaffId() {return staffId;}
    public void setStaffId(int staffId) {this.staffId =staffId;}

    public String getCustomId() {return customId;}
    public void setCustomId(String customId) { this.customId=customId;}

    public String getContactNo() {return contact_no;}
    public void setContactNo(String contactNo) { this.contact_no=contactNo;}

    public String getWhatsappNo() {return whatsapp_no;}
    public void setWhatsappNo(String whatsappNo) { this.whatsapp_no=whatsappNo;}
    
    public String getAddress() {return address;}
    public void setAddress(String address) { this.address=address;}
    
}
