package com.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangbin
 * @since 2019-07-31
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    private final static Logger logger = LoggerFactory.getLogger(CartController.class);
}

