package com.mmall.pojo;

import java.math.BigDecimal;
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
@TableName("mmall_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id,对应mmall_category表的主键
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品副标题
     */
    @TableField("subtitle")
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * 图片地址,json格式,扩展用
     */
    @TableField("sub_images")
    private String subImages;

    /**
     * 商品详情
     */
    @TableField("detail")
    private String detail;

    /**
     * 价格,单位-元保留两位小数
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    @TableField("status")
    private Integer status;

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

    public static final String CATEGORY_ID = "category_id";

    public static final String NAME = "name";

    public static final String SUBTITLE = "subtitle";

    public static final String MAIN_IMAGE = "main_image";

    public static final String SUB_IMAGES = "sub_images";

    public static final String DETAIL = "detail";

    public static final String PRICE = "price";

    public static final String STOCK = "stock";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
