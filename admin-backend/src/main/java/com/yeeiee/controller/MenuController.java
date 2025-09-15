package com.yeeiee.controller;

import com.yeeiee.domain.entity.Menu;
import com.yeeiee.domain.entity.RoleMenu;
import com.yeeiee.domain.form.MenuForm;
import com.yeeiee.domain.validate.GroupingValidate;
import com.yeeiee.domain.vo.MenuVo;
import com.yeeiee.domain.vo.R;
import com.yeeiee.exception.VerifyTokenException;
import com.yeeiee.security.JwtTokenProvider;
import com.yeeiee.service.MenuService;
import com.yeeiee.service.RoleMenuService;
import com.yeeiee.utils.BeanUtil;
import com.yeeiee.utils.RequestObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-11
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单表 控制器")
public class MenuController {
    private final MenuService menuService;
    private final RoleMenuService roleMenuService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "查询菜单列表")
    @GetMapping
    public R<List<MenuVo>> getList(HttpServletRequest request) {
        val token = RequestObjectUtil.getTokenFromRequest(request);

        val payload = jwtTokenProvider.parseAccessToken(token)
                .orElseThrow(() -> new VerifyTokenException("token解析失败"))
                .getPayload();

        val roles = jwtTokenProvider.getRoles(payload);
        if(CollectionUtils.isEmpty(roles)){
            return R.ok(List.of());
        }
        val menuList = menuService.getMenuListByRoleNames(roles);
        return R.ok(menuList);
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    public R<Void> add(@Validated(GroupingValidate.Create.class) @RequestBody MenuForm menuForm) {
        menuService.addMenu(menuForm);
        return R.ok();
    }

    @Operation(summary = "更新菜单")
    @PutMapping
    public R<Void> modify(@Validated(GroupingValidate.Update.class) @RequestBody MenuForm menuForm) {
        val menu = BeanUtil.toBean(menuForm, Menu.class);
        menuService.updateById(menu);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    public R<Void> removeById(@PathVariable("id") @Parameter(description = "菜单id") Long id) {
        menuService.removeMenu(id);
        return R.ok();
    }

    @Operation(summary = "根据角色id，查询菜单id列表")
    @GetMapping("/{roleId}")
    public R<List<Long>> getIdsByRoleId(@PathVariable("roleId") @Parameter(description = "角色id") Long roleId) {
        val menuIds = roleMenuService.lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, roleId)
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .toList();
        return R.ok(menuIds);
    }
}

