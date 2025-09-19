package com.yeeiee.mapper;

import com.yeeiee.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-19
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<MenuVo> selectMenusByRoleNames(@Param("roles") List<String> roles);
}
