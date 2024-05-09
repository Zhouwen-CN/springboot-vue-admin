package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.FirstLevelMenu;
import com.yeeiee.entity.dto.MenuDto;

import java.util.List;

/**
 * <p>
 * 一级菜单表 服务类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface FirstLevelMenuService extends IService<FirstLevelMenu> {
    List<MenuDto> getMenus();
}
