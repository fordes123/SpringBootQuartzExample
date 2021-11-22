package org.fordes.quartz.example.enums;

/**
 * 任务状态枚举
 *
 * @author fordes on 2021/11/21
 */
public enum StatusEnum {

    NORMAL(0),
    PAUSE(1);

    StatusEnum(Integer value) {
        this.value = value;
    }

    private final Integer value;

    public Integer getValue() {
        return value;
    }
}