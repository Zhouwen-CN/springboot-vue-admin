package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.FirstLevelMenu;
import com.yeeiee.entity.dto.MenuDto;
import com.yeeiee.mapper.FirstLevelMenuMapper;
import com.yeeiee.service.FirstLevelMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 一级菜单表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Service
@AllArgsConstructor
public class FirstLevelMenuServiceImpl extends ServiceImpl<FirstLevelMenuMapper, FirstLevelMenu> implements FirstLevelMenuService {

    private FirstLevelMenuMapper firstLevelMenuMapper;

    @Override
    public List<MenuDto> getMenus() {
        return firstLevelMenuMapper.selectMenus();
    }
}
