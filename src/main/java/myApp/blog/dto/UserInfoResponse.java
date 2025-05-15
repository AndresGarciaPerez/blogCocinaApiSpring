package myApp.blog.dto;

import lombok.Data;

@Data
public class UserInfoResponse {

    private String username;
    private String role;


    public UserInfoResponse(String username, String role){
        this.username = username;
        this.role = role;
    }


}
