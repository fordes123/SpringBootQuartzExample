package org.fordes.quartz.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志
 */

@Data
@Accessors(chain = true)
@TableName(value = "job_log")
@ApiModel(value = "定时任务调度日志")
public class JobLog implements Serializable {
    /**
     * 任务日志ID
     */
    @TableId(value = "job_log_id", type = IdType.AUTO)
    @ApiModelProperty(value = "任务日志ID")
    private Long jobLogId;

    /**
     * 任务名称
     */
    @TableField(value = "job_name")
    @ApiModelProperty(value = "任务名称")
    private String jobName;

    /**
     * 任务组名
     */
    @TableField(value = "job_group")
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @TableField(value = "invoke_target")
    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    /**
     * 日志信息
     */
    @TableField(value = "job_message")
    @ApiModelProperty(value = "日志信息")
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "执行状态（0正常 1失败）")
    private Integer status;

    /**
     * 异常信息
     */
    @TableField(value = "exception_info")
    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 开始时间
     */
    @JsonIgnore
    @TableField(exist = false)
    private Date startTime;

    /**
     * 停止时间
     */
    @JsonIgnore
    @TableField(exist = false)
    private Date stopTime;

    private static final long serialVersionUID = 1L;
}