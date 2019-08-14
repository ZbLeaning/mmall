package com.mmall.controller.portal;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户模板
 * @author zhangbin
 * @since 2019-07-31
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * @Description 用户登录
     * @author   zhangbin
     * @Date     2019-08-02 15:55
     * @param    userName, password, session
     * @return   java.lang.Object
     **/
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(userName, password);
        if (response.isSuccess()){
            //登录成功，将用户信息存入session
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    /**
     * @Description 登出
     * @author   zhangbin
     * @Date     2019-08-06 19:21
     * @param    [session]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
    /**
     * @Description 注册
     * @author   zhangbin
     * @Date     2019-08-06 19:37
     * @param    [user]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }
    /**
     * @Description 数据校验
     * @author   zhangbin
     * @Date     2019-08-06 19:46
     * @param    [str, type]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * @Description 获取用户信息
     * @author   zhangbin
     * @Date     2019-08-06 19:50
     * @param    [session]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
       User user = (User)session.getAttribute(Const.CURRENT_USER);
       if (user != null){
           return ServerResponse.createBySuccess(user);
       }
       return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户信息");
    }
    /**
     * @Description 忘记密码提示问题
     * @author   zhangbin
     * @Date     2019-08-06 20:05
     * @param    [username]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.forgetGetQuestion(username);
    }
    /**
     * @Description 忘记密码答案校验
     * @author   zhangbin
     * @Date     2019-08-06 20:21
     * @param    [username, question, answer]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
       return iUserService.checkAnswer(username,question,answer);
    }
    /**
     * @Description 重置密码
     * @author   zhangbin
     * @Date     2019-08-06 20:33
     * @param    [username, passwordNew, forgetToken]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "forget_rest_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetRestPassword(username,passwordNew,forgetToken);
    }
    /**
     * @Description 登录状态重置密码
     * @author   zhangbin
     * @Date     2019-08-06 20:45
     * @param    [session, passwordOld, passwordNew]
     * @return   com.mmall.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "rest_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * @Description 更新用户个人信息
     * @author   zhangbin
     * @Date     2019-08-12 18:47
     * @param    [session, user]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateInfomation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * @Description 获取用户信息
     * @author   zhangbin
     * @Date     2019-08-12 18:52
     * @param    [session]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}

