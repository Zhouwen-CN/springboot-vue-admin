package com.yeeiee.scheduler.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.scheduler.domain.entity.JobLog;
import com.yeeiee.scheduler.domain.vo.JobLogVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 定时任务日志表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-10-15
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    IPage<JobLogVo> selectJobLogPage(
            Page<?> page,
            @Param("jobId") Long jobId,
            @Param("status") Integer status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}