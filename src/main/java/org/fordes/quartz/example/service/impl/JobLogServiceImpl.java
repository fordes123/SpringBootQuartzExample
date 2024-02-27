package org.fordes.quartz.example.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.fordes.quartz.example.mapper.JobLogMapper;
import org.fordes.quartz.example.model.JobLog;
import org.fordes.quartz.example.service.JobLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author fordes on 2021/11/20
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    @Override
    public List<JobLog> selectJobLogList(JobLog jobLog) {
        QueryWrapper<JobLog> wrapper = new QueryWrapper<>();
        wrapper.allEq(JSONUtil.parseObj(jobLog).toBean(new TypeReference<Map<String, String>>() {
        }), false);
        return list(wrapper);
    }

    @Override
    public void cleanJobLog() {
        baseMapper.clearAll();
    }

}
