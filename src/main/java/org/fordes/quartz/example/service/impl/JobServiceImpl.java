package org.fordes.quartz.example.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.fordes.quartz.example.constants.Constants;
import org.fordes.quartz.example.enums.StatusEnum;
import org.fordes.quartz.example.mapper.JobMapper;
import org.fordes.quartz.example.model.JobEntity;
import org.fordes.quartz.example.service.JobService;
import org.fordes.quartz.example.utils.ScheduleUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author fordes on 2021/11/20
 */
@Service
@AllArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, JobEntity> implements JobService {

    private final Scheduler scheduler;

    @Override
    public List<JobEntity> selectJobList(JobEntity jobEntity) {
        QueryWrapper<JobEntity> wrapper = new QueryWrapper<>();
        wrapper.allEq(JSONUtil.parseObj(jobEntity).toBean(new TypeReference<Map<String, String>>() {
        }), false);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(JobEntity jobEntity) throws SchedulerException {
        if (this.updateById(jobEntity)) {
            scheduler.pauseJob(JobKey.jobKey(jobEntity.getJobId().toString(), jobEntity.getJobGroup()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeJob(JobEntity jobEntity) throws SchedulerException {
        if (updateById(jobEntity.setStatus(0))) {
            scheduler.resumeJob(JobKey.jobKey(jobEntity.getJobId().toString(), jobEntity.getJobGroup()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(JobEntity jobEntity) throws SchedulerException {
        String jobId = jobEntity.getJobId().toString();
        String jobGroup = jobEntity.getJobGroup();
        if (removeById(jobEntity.getJobId())) {
            //暂停、移除、删除
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobId, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobId, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobId, jobGroup));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            deleteJob(getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(JobEntity jobEntity) throws SchedulerException {
        Integer status = jobEntity.getStatus();
        if (StatusEnum.NORMAL.getValue().equals(status)) {
            resumeJob(jobEntity);
        } else if (StatusEnum.PAUSE.getValue().equals(status)) {
            pauseJob(jobEntity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(JobEntity jobEntity) throws SchedulerException {
        String jobId = jobEntity.getJobId().toString();
        String jobGroup = jobEntity.getJobGroup();
        JobEntity properties = getById(jobEntity.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(Constants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(JobKey.jobKey(jobId, jobGroup), dataMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertJob(JobEntity jobEntity) throws SchedulerException {
        jobEntity.setStatus(StatusEnum.PAUSE.getValue());
        if (save(jobEntity)) {
            ScheduleUtils.createScheduleJob(scheduler, jobEntity);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobEntity jobEntity) throws Exception {
        JobEntity properties = getById(jobEntity.getJobId());
        if (updateById(jobEntity)) {
            updateSchedulerJob(jobEntity, properties.getJobGroup());
        }
    }

    public void updateSchedulerJob(JobEntity jobEntity, String jobGroup) throws Exception {
        Long jobId = jobEntity.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, jobEntity);
    }

}
