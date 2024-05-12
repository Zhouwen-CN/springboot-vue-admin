package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.Menu;
import com.yeeiee.entity.Role;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.UserDto;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.mapper.UserMapper;
import com.yeeiee.service.UserService;
import com.yeeiee.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@AllArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private AuthenticationProvider authenticationProvider;

    private UserMapper userMapper;
    private MenuMapper menuMapper;

    @Override
    public String login(LoginDto loginDto) {
        val authenticate = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return JwtTokenUtil.generateToken(authenticate);
    }

    @Override
    public UserDto getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val userDto = userMapper.selectByUserName(userDetails.getUsername());
        val roleIds = userDto.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        val menuList = menuMapper.selectMenusByRoleIds(roleIds);
        val menuTree = convertToMenuTree(menuList);
        userDto.setPassword(null);
        userDto.setMenus(menuTree);
        return userDto;
    }

    /**
     * 将menus列表转换成树形结构，递归sql性能不太好，使用代码处理
     */
    private List<Menu> convertToMenuTree(List<Menu> menuList) {
        val menuItemMap = new HashMap<Long, Menu>(8);
        val menuTree = new ArrayList<Menu>();
        for (Menu menu : menuList) {
            val id = menu.getId();
            val pid = menu.getPid();

            if (!menuItemMap.containsKey(id)) {
                menuItemMap.put(id, new Menu().setChildren(new ArrayList<>()));
            }

            val item = menuItemMap.get(id);
            if (item.getId() == null) {
                Menu.mergeMenu(item, menu);
            }

            if (pid == 0) {
                menuTree.add(item);
            } else {
                if (!menuItemMap.containsKey(pid)) {
                    menuItemMap.put(pid, new Menu().setChildren(new ArrayList<>()));
                }
                val children = menuItemMap.get(pid).getChildren();
                children.add(item);
            }
        }

        return menuTree;
    }
}
