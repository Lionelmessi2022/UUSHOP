package com.test.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.com.test.vo.ResultVO;
import com.test.entity.Admin;
import com.test.exception.ShopException;
import com.test.form.AdminLoginForm;
import com.test.result.ResponseEnum;
import com.test.service.AdminService;
import com.test.util.JwtUtil;
import com.test.util.ResultVOUtil;
import com.test.vo.AdminLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2026-08-17
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public ResultVO login(AdminLoginForm from) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", from.getUsername());
        Admin admin = this.adminService.getOne(queryWrapper);
        if (admin == null) throw new ShopException(ResponseEnum.ADMIN_USERNAME_NULL.getMsg());
        if (!admin.getPassword().equals(from.getPassword())) throw new ShopException(ResponseEnum.ADMIN_PASSWORD_ERROR.getMsg());
        String token = JwtUtil.createToken(admin.getAdminId(), admin.getUsername());
        AdminLoginVO vo = new AdminLoginVO();
        BeanUtils.copyProperties(admin, vo);
        vo.setToken(token);
        return ResultVOUtil.success(vo);
    }

    //token验证
    @GetMapping("/checkToken")
    public ResultVO checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean result = JwtUtil.checkToken(token);
        if (result) return ResultVOUtil.success(null);
        return ResultVOUtil.fail(ResponseEnum.ADMIN_TOKEN_ERROR.getMsg());
    }

}

