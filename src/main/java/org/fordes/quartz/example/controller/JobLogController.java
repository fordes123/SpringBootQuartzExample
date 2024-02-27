package org.fordes.quartz.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fordes.quartz.example.model.JobLog;
import org.fordes.quartz.example.service.JobLogService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 定时任务调度日志 控制器
 *
 * @author fordes on 2021/11/20
 */
@Api(tags = "定时任务调度日志")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/jobLog")
public class JobLogController {

    private final JobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @ApiOperation("调度日志列表")
    @GetMapping("/list")
    public List<?> list(JobLog jobLog) {
        return jobLogService.selectJobLogList(jobLog);
    }


    /**
     * 根据调度编号获取详细信息
     */
    @ApiOperation("日志详请")
    @GetMapping(value = "/{id}")
    public Object getInfo(@PathVariable @NotNull Long id) {
        return jobLogService.getById(id);
    }

    /**
     * 删除定时任务调度日志
     */
    @ApiOperation("批量删除")
    @DeleteMapping("/{jobLogIds}")
    public Object remove(@PathVariable @NotEmpty List<Long> jobLogIds) {
        return jobLogService.removeByIds(jobLogIds);
    }

    /**
     * 清空定时任务调度日志
     */
    @ApiOperation("清空日志")
    @DeleteMapping("/clean")
    public void clean() {
        jobLogService.cleanJobLog();
    }

    @ApiOperation(value = "测试调用", hidden = true)
    @GetMapping("/test")
    public String testTask() {
        log.info("测试Http调用，日志总条数：{}", jobLogService.count());
        return "HelloWord!";
    }
}
