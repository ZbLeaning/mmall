package com.mapper;

import com.common.ServerResponse;
import com.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangbin
 * @since 2019-07-31
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 用户名校验
     * @param userName
     * @return
     */
    int checkUsername(String userName);

    /**
     * 邮箱校验
     * @param email
     * @return
     */
    int checkEmail(String email);
    /**
     * 登录返回用户信息
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password") String password);

    /**
     * 数据校验
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 根据用户名查询对应密码提示问题
     * @param username
     * @return
     */
    String selectQuestionByUsername(String username);

    /**
     * 校验找回密码答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    /**
     * 更新密码
     * @param username
     * @param password
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    /**
     * 校验用户旧密码
     * @param password
     * @param userId
     * @return
     */
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);
}
