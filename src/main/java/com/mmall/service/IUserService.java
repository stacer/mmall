package com.mmall.service;

import com.mmall.pojo.User;
import com.mmall.vo.ServerResponse;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-10:32
 */
public interface IUserService {

    //用户登录
    ServerResponse<User> login(String username, String password);

    //用户注册
    ServerResponse<String> register(User user);

    //用户校验
    ServerResponse<String> checkValid(String str,String type);

    //根据用户名查找问题
    ServerResponse<String> selectQuestion(String username);

    //校验用户答案
    ServerResponse<String> checkAnswer(String username,String question,String answer);

    //重置密码
    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    //登录状态下的重置密码
    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    //校验是否是管理员
    ServerResponse checkAdminRole(User user);
}


