package com.yeeiee.scheduler;

import com.yeeiee.scheduler.handler.JobHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 作业执行器持有者
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Component
public class JobHandlerHolder {

    private final Map<String, JobHandler> jobHandlerMap;

    public JobHandlerHolder(ObjectProvider<JobHandler> jobHandlers){
        jobHandlerMap = jobHandlers.orderedStream().collect(Collectors.toMap(JobHandler::name, jobHandler -> jobHandler));
    }

    public JobHandler getJobHandler(String name){
        return jobHandlerMap.get(name);
    }

    public List<String> getJobHandlerNames(){
        return jobHandlerMap.keySet().stream().toList();
    }
}
