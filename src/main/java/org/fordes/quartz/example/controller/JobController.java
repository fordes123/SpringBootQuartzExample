package org.fordes.quartz.example.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fordes.quartz.example.enums.MethodEnum;
import org.fordes.quartz.example.model.JobEntity;
import org.fordes.quartz.example.service.JobService;
import org.fordes.quartz.example.utils.CronUtils;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 定时任务调度 控制器
 *
 * @author fordes on 2021/11/20
 */
@Api(tags = "定时任务调度")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    /**
     * 查询定时任务列表
     */
    @ApiOperation("任务列表")
    @GetMapping("/list")
    public List<?> list(JobEntity sysJob) {
        return jobService.selectJobList(sysJob);
    }


    /**
     * 获取定时任务详细信息
     */
    @ApiOperation("任务详请")
    @GetMapping(value = "/{jobId}")
    public Object getInfo(@PathVariable("jobId") Long jobId) {
        return jobService.getById(jobId);
    }

    /**
     * 新增定时任务
     */
    @ApiOperation("新增任务")
    @PostMapping("/add")
    public String add(@RequestBody @Valid JobEntity job) throws SchedulerException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (MethodEnum.verify(job.getInvokeTarget())) {
            return ("新增任务'" + job.getJobName() + "'失败，不支持的调用方式");
        }
        jobService.insertJob(job);
        return StrUtil.EMPTY;
    }

    /**
     * 修改定时任务
     */
    @ApiOperation("修改任务")
    @PutMapping("/edit")
    public String edit(@RequestBody @Valid JobEntity job) throws Exception {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return ("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (MethodEnum.verify(job.getInvokeTarget())) {
            return ("修改任务'" + job.getJobName() + "'失败，不支持的调用方式");
        }
        jobService.updateJob(job);
        return StrUtil.EMPTY;
    }

    /**
     * 定时任务状态修改
     */
    @ApiOperation("状态修改")
    @PutMapping("/changeStatus")
    public void changeStatus(@RequestBody JobEntity job) throws SchedulerException {
        JobEntity newJob = jobService.getById(job.getJobId());
        newJob.setStatus(job.getStatus());
        jobService.changeStatus(newJob);
    }

    /**
     * 定时任务立即执行一次
     */
    @ApiOperation("立即执行一次")
    @GetMapping("/run")
    public void run(@RequestParam @NotEmpty @ApiParam("任务ID") Long jobId) throws SchedulerException {
        jobService.run(jobService.getById(jobId));
    }

    /**
     * 删除定时任务
     */
    @ApiOperation("删除任务")
    @DeleteMapping("/{jobIds}")
    public void remove(@PathVariable Long[] jobIds) throws SchedulerException {
        jobService.deleteJobByIds(jobIds);
    }
}
