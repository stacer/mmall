package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.vo.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author Administrator
 * @create 2017-07-25-10:33
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String userName, String password) {
        //校验用户名是否存在
        int count = userMapper.checkUserName(userName);
        if(count == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //密码登录md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName, password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        //校验用户名是否存在
        int count = userMapper.checkUserName(user.getUsername());
        if(count > 0){
            return ServerResponse.createByErrorMessage("用户名存在");
        }
        //校验email是否存在
        int countByEmail = userMapper.checkByEmail(user.getEmail());
        if(countByEmail > 0){
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insertResult = userMapper.insert(user);
        if(insertResult == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");

    }

    /**
     * 用户校验
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int count = userMapper.checkUserName(str);
                if(count > 0){
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int countByEmail = userMapper.checkByEmail(str);
                if(countByEmail > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 查询用户问题
     * @param username
     * @return
     */
    public ServerResponse selectQuestion(String username){
        //校验用户名是否存在
        int count = userMapper.checkUserName(username);
        if(count == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题不存在");
    }

    /**
     * 校验答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount= userMapper.checkAnswer(username,question,answer);
        //用户身份验证需要失效
        if(resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token不存在");
        }
        int count = userMapper.checkUserName(username);
        if(count == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效");
        }

        //将token做对比
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登录状态下的重置Miami
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权,要校验一下用户的旧密码,一定要指定这个用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要校验,email是不是已经存在,并且存在的email如果相同的话,不能是当前这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email已经存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
