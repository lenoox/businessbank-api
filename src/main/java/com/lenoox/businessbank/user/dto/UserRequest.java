package com.lenoox.businessbank.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
}
