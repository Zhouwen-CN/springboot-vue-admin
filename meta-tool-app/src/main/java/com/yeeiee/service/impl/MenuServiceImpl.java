package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.Menu;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@AllArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}
