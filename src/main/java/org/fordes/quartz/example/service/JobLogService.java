package org.fordes.quartz.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fordes.quartz.example.model.JobLog;

import java.util.List;

/**
 * @author fordes on 2021/11/20
 */
public interface JobLogService extends IService<JobLog> {

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<JobLog> selectJobLogList(JobLog jobLog);


    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
