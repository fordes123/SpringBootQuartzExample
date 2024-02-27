package org.fordes.quartz.example.execution;


import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fordes.quartz.example.constants.Constants;
import org.fordes.quartz.example.model.JobEntity;
import org.fordes.quartz.example.model.JobLog;
import org.fordes.quartz.example.service.JobLogService;
import org.fordes.quartz.example.utils.BeanUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author fordes on 2021/11/20
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        JobEntity job =  MapUtil.get(context.getMergedJobDataMap(), Constants.TASK_PROPERTIES, JobEntity.class);
        try {
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, job, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param JobEntity  系统计划任务
     */
    protected void before(JobExecutionContext context, JobEntity JobEntity) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param JobEntity  系统计划任务
     */
    protected void after(JobExecutionContext context, JobEntity JobEntity, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final JobLog log = new JobLog()
                .setJobName(JobEntity.getJobName())
                .setJobGroup(JobEntity.getJobGroup())
                .setInvokeTarget(JobEntity.getInvokeTarget())
                .setStartTime(startTime)
                .setStopTime(new Date());
        long runMs = log.getStopTime().getTime() - log.getStartTime().getTime();
        log.setJobMessage(log.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            log.setStatus(1);
            String errorMsg = StringUtils.substring(ExceptionUtil.getMessage(e), 0, 2000);
            log.setExceptionInfo(errorMsg);
        } else {
            log.setStatus(0);
        }

        // 记录日志
        BeanUtils.getBean(JobLogService.class).save(log);

    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param JobEntity  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, JobEntity JobEntity) throws Exception;
}
