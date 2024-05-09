package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.entity.FirstLevelMenu;
import com.yeeiee.entity.dto.MenuDto;

import java.util.List;

/**
 * <p>
 * 一级菜单表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface FirstLevelMenuMapper extends BaseMapper<FirstLevelMenu> {
    List<MenuDto> selectMenus();
}
