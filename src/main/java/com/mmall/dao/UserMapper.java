package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //校验用户是否存在
    int checkUserName(String userName);

    //校验邮箱是否存在
    int checkByEmail(String email);

    //用户登录
    User selectLogin(@Param("userName") String userName, @Param("password") String password);

    //查找问题
    String selectQuestionByUsername(String username);

    //校验答案
    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);

    int checkEmailByUserId(@Param("emial")String emial,@Param("userId")Integer userId);
}