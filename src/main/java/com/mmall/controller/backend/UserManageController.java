package com.mmall.controller.backend;/**
 * ClassName: UserManageController
 * Description:  TODO
 * Date:      2019-08-12 18:54
 * author     admin
 * version    V1.0
 */

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: UserManageController
 * @Description: 后台管理员控制类
 * @Authon: zhangbin
 * @Date 2019-08-12 18:54
 * @Version: 1.0
 */
@RestController("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    /**
     * @Description 管理员登录
     * @author   zhangbin
     * @Date     2019-08-12 18:58
     * @param    [username, password, session]
     * @return   com.mmall.common.ServerResponse<com.mmall.pojo.User>
     **/
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                //管理员登录
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
