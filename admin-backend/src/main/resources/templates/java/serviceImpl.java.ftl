<#--@formatter:off-->
package ${package.serviceImpl};

import ${package.entity}.${table.className};
import ${package.mapper}.${table.className}Mapper;
import ${package.service}.${table.className}Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.tableComment} 服务实现类
 * </p>
 *
 * @author ${table.author}
 * @since ${.now?string('yyyy-MM-dd')}
 */
@Service
public class ${table.className}ServiceImpl extends ServiceImpl<${table.className}Mapper, ${table.className}> implements ${table.className}Service {

}