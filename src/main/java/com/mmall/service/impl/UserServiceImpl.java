package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.mapper.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

import com.mmall.util.MD5Util;

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
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
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
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
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
    /**
     * @Description 更新用户个人信息
     * @author   zhangbin
     * @Date     2019-08-12 18:48
     * @param    [user]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    public ServerResponse<User> updateInfomation(User user){
        //username不可更新
        //email校验，校验新的email是不是已经存在并且存在的email如果相同，不能是当前用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateById(updateUser);
        if (updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }
    /**
     * @Description 获取用户信息
     * @author   zhangbin
     * @Date     2019-08-12 18:53
     * @param    [userId]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectById(userId);
        if (user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
