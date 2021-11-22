package org.fordes.quartz.example.execution;

import org.fordes.quartz.example.model.JobEntity;
import org.fordes.quartz.example.utils.JobInvokeUtil;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理 （允许并发执行）
 *
 * @author fordes on 2021/11/20
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, JobEntity job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
