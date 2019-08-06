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
@TableName("mmall_pay_info")
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 订单号
     */
    @TableField("order_no")
    private Long orderNo;

    /**
     * 支付平台:1-支付宝,2-微信
     */
    @TableField("pay_platform")
    private Integer payPlatform;

    /**
     * 支付宝支付流水号
     */
    @TableField("platform_number")
    private String platformNumber;

    /**
     * 支付宝支付状态
     */
    @TableField("platform_status")
    private String platformStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String ORDER_NO = "order_no";

    public static final String PAY_PLATFORM = "pay_platform";

    public static final String PLATFORM_NUMBER = "platform_number";

    public static final String PLATFORM_STATUS = "platform_status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
