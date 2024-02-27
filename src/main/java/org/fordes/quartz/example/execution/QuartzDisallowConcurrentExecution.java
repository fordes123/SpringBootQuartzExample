package org.fordes.quartz.example.execution;

import org.fordes.quartz.example.model.JobEntity;
import org.fordes.quartz.example.utils.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author fordes on 2021/11/20
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, JobEntity job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
