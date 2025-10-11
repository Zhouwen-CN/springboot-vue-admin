package com.yeeiee.service.impl;

import com.yeeiee.domain.entity.Job;
import com.yeeiee.mapper.JobMapper;
import com.yeeiee.service.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

}