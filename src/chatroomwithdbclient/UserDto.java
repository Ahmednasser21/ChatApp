/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroomwithdbclient;

/**
 *
 * @author Ahmed
 */
public class UserDto {
    private String username;
    private String email;
    private String password;
    private String online;

    public UserDto() {
        this.online = "online";
    }

    public UserDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.online = "online";
    }
    public UserDto(String username, String online) {
        this.username = username;
        
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String isIsActive() {
        return online;
    }

    public void setIsActive(String online) {
        this.online = online;
    }
    
    
    
    
}
