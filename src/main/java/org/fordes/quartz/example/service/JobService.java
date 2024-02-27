package org.fordes.quartz.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fordes.quartz.example.model.JobEntity;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author fordes on 2021/11/20
 */
public interface JobService extends IService<JobEntity> {

    /**
     * 获取quartz调度器的计划任务
     *
     * @param jobEntity 调度信息
     * @return 调度任务集合
     */
    List<JobEntity> selectJobList(JobEntity jobEntity);



    /**
     * 暂停任务
     *
     * @param jobEntity 调度信息
     */
    void pauseJob(JobEntity jobEntity) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param jobEntity 调度信息
     */
    void resumeJob(JobEntity jobEntity) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param jobEntity 调度信息
     */
    void deleteJob(JobEntity jobEntity) throws SchedulerException;

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     */
    void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任务调度状态修改
     *
     * @param jobEntity 调度信息
     */
    void changeStatus(JobEntity jobEntity) throws SchedulerException;

    /**
     * 立即运行任务
     *
     * @param jobEntity 调度信息
     */
    void run(JobEntity jobEntity) throws SchedulerException;

    /**
     * 新增任务
     *
     * @param jobEntity 调度信息
     */
    void insertJob(JobEntity jobEntity) throws SchedulerException;

    /**
     * 更新任务
     *
     * @param jobEntity 调度信息
     */
    void updateJob(JobEntity jobEntity) throws Exception;

}
