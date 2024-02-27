package org.fordes.quartz.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.fordes.quartz.example.model.JobLog;

@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

    @Update("truncate table job_log")
    void clearAll();
}