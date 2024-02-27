package org.fordes.quartz.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.fordes.quartz.example.utils.CronUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度
 */

@Data
@Accessors(chain = true)
@TableName(value = "job")
@ApiModel(value = "定时任务调度")
public class JobEntity implements Serializable {

    /**
     * 任务ID
     */
    @TableId(value = "job_id", type = IdType.AUTO)
    @ApiModelProperty(value = "任务ID")
    private Long jobId;

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
     * cron执行表达式
     */
    @TableField(value = "cron_expression")
    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;

    /**
     * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    @TableField(value = "misfire_policy")
    @ApiModelProperty(value = "计划执行错误策略（1立即执行 2执行一次 3放弃执行）")
    private Integer misfirePolicy;

    /**
     * 是否并发执行（1允许 0禁止）
     */
    @TableField(value = "is_concurrent")
    @ApiModelProperty(value = "是否并发执行（1允许 0禁止）")
    private Integer isConcurrent;

    /**
     * 状态（0正常 1暂停）
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态（0正常 1暂停）")
    private Integer status;

    /**
     * 备注信息
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注信息")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "下次执行时间", hidden = true)
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextTime;

    public Date getNextTime() {
        try {
            return CronUtils.getNextExecution(this.cronExpression);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static final long serialVersionUID = 1L;
}