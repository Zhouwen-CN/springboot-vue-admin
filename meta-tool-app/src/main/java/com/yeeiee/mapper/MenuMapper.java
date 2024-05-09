package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.entity.Menu;
import com.yeeiee.entity.dto.MenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 二级菜单表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuDto> selectMenusByRoleId(@Param("roleId") Integer roleId);
}
