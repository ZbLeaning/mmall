package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * 用户管理service接口
 * @author zhangbin
 * @since 2019-07-31
 */
public interface IUserService{
    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    ServerResponse<User> login(String userName, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 数据校验
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str,String type);

    /**
     * 忘记密码问题获取
     * @param username
     * @return
     */
    ServerResponse<String> forgetGetQuestion(String username);

    /**
     * 校验找回密码答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username,String question,String answer);

    /**
     * 密码修改
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken);

    /**
     * 登录状态重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew,User user);

    /**
     * 更新用户个人信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInfomation(User user);

    /**
     * 通过userId获取用户信息
     * @param userId
     * @return
     */
    ServerResponse<User> getInformation(Integer userId);
}
