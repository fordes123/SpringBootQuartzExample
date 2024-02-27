package org.fordes.quartz.example.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 调用测试
 *
 * @author fordes on 2021/11/21
 */
@Slf4j
@Component("testTask")
public class TestTask {

    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i);
    }

    public void params(String params) {
        log.info("执行有参方法：" + params);
    }

    public void noParams() {
        log.info("执行无参方法");
    }

}
