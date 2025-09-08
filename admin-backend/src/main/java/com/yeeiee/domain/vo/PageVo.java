package com.yeeiee.domain.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import java.util.List;

/**
 * <p>
 * 分页视图
 * </p>
 *
 * @author chen
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
@Schema(name = "PageVo", description = "分页视图")
public class PageVo<T> {
    @Schema(description = "数据列表")
    private List<T> records;
    @Schema(description = "总页数")
    private Long total;
    @Schema(description = "每页条数")
    private Long size;
    @Schema(description = "当前页码")
    private Long current;

    public static <T> PageVo<T> fromPage(IPage<T> page) {
        val pageVo = new PageVo<T>();
        pageVo.records = page.getRecords();
        pageVo.total = page.getTotal();
        pageVo.size = page.getSize();
        pageVo.current = page.getCurrent();
        return pageVo;
    }
}
