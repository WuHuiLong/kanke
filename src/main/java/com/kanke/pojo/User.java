package com.kanke.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;

    private String username;

    private String password;

    private String telephone;

    private String email;

    private String name;

    private Integer members;

    private Integer role;

    private String question;

    private String answer;

    private Date createTime;

    private Date updateTime;
}