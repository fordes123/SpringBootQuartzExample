package org.fordes.quartz.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fordes.quartz.example.model.JobEntity;

@Mapper
public interface JobMapper extends BaseMapper<JobEntity> {
}