package com.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangbin
 * @since 2019-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mmall_shipping")
public class Shipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 收货姓名
     */
    @TableField("receiver_name")
    private String receiverName;

    /**
     * 收货固定电话
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 收货移动电话
     */
    @TableField("receiver_mobile")
    private String receiverMobile;

    /**
     * 省份
     */
    @TableField("receiver_province")
    private String receiverProvince;

    /**
     * 城市
     */
    @TableField("receiver_city")
    private String receiverCity;

    /**
     * 区/县
     */
    @TableField("receiver_district")
    private String receiverDistrict;

    /**
     * 详细地址
     */
    @TableField("receiver_address")
    private String receiverAddress;

    /**
     * 邮编
     */
    @TableField("receiver_zip")
    private String receiverZip;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String RECEIVER_NAME = "receiver_name";

    public static final String RECEIVER_PHONE = "receiver_phone";

    public static final String RECEIVER_MOBILE = "receiver_mobile";

    public static final String RECEIVER_PROVINCE = "receiver_province";

    public static final String RECEIVER_CITY = "receiver_city";

    public static final String RECEIVER_DISTRICT = "receiver_district";

    public static final String RECEIVER_ADDRESS = "receiver_address";

    public static final String RECEIVER_ZIP = "receiver_zip";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
