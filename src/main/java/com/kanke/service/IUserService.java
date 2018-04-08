package com.kanke.service;

import com.kanke.commom.ServerResponse;
import com.kanke.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String newPassword,String forgetToken);

    ServerResponse<String> resetPassword(String oldPassword,String newPassword,User user);

    ServerResponse<User> update_information(User user);

    ServerResponse<User> get_information(Integer userId);

    ServerResponse checkAdminRole(User user);
}
