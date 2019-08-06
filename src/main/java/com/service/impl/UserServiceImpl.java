package com.service.impl;

import com.common.Const;
import com.common.ServerResponse;
import com.common.TokenCache;
import com.entity.User;
import com.mapper.UserMapper;
import com.service.IUserService;

import com.util.MD5Util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangbin
 * @since 2019-07-31
 */
@Service
public class UserServiceImpl implements IUserService {
    //注入依赖
    @Autowired
    private UserMapper userMapper;
    /**
     * @Description 登录
     * @author   zhangbin
     * @Date     2019-08-06 19:36
     * @param    [userName, password]
     * @return   com.common.ServerResponse<com.entity.User>
     **/
    @Override
    public ServerResponse<User> login(String userName, String password) {
        //用户名校验
        int resultCount = userMapper.checkUsername(userName);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //密码校验，MD5加密
        String mad5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(userName, mad5Password);
        if (user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }
    /**
     * @Description 注册
     * @author   zhangbin
     * @Date     2019-08-06 19:36
     * @param    [user]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkValid(Const.USERNAME, user.getUsername());
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(Const.EMAIL, user.getUsername());
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * @Description 参数校验公用方法
     * @author   zhangbin
     * @Date     2019-08-06 19:44
     * @param    [str, type]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> checkValid(String str,String type){
        if (StringUtils.isNotBlank(type)){
            //开始校验
            if (Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount == 0){
                    return ServerResponse.createByErrorMessage("用户名不存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
    /**
     * @Description 忘记密码问题
     * @author   zhangbin
     * @Date     2019-08-06 20:04
     * @param    [username]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> forgetGetQuestion(String username){
        ServerResponse<String> validResponse = this.checkValid(Const.USERNAME,username);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空");
    }

    /**
     * @Description 密码问题校验
     * @author   zhangbin
     * @Date     2019-08-06 20:20
     * @param    [username, question, answer]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if (resultCount >0){
            //问题是该用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }
    /**
     * @Description 忘记密码时重置密码
     * @author   zhangbin
     * @Date     2019-08-06 20:46
     * @param    [username, passwordNew, forgetToken]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse<String> validResponse = this.checkValid(Const.USERNAME,username);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }
    /**
     * @Description 登录状态重置密码
     * @author   zhangbin
     * @Date     2019-08-06 20:46
     * @param    [passwordOld, passwordNew, user]
     * @return   com.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew,User user){
        //为防止横向越权，需校验用户旧密码，保证指定是这个用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateById(user);
        if (updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }
}
