package com.yeeiee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeeiee.entity.Role;
import com.yeeiee.service.RoleService;
import com.yeeiee.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/role")
@Tag(name = "角色表 控制器")
public class RoleController {
    private RoleService roleService;

    /**
     * 默认不会查询出admin角色
     */
    @Operation(summary = "查询所有")
    @GetMapping("")
    public R<List<Role>> getList() {
        val queryWrapper = new QueryWrapper<Role>().ne("role_name", "admin");
        val list = roleService.list(queryWrapper);
        return R.ok(list);
    }
}

