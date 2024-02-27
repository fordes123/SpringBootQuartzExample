package org.fordes.quartz.example.enums;

import cn.hutool.core.util.ReUtil;

/**
 * 支持的方法枚举
 *
 * @author fordes on 2021/11/21
 */
public enum MethodEnum {

    HTTP("^http[s]?://.*", "Http调用"),
    REFLECT("^[\\w,_,$][\\w,_,\\d,$,.,\\(,\\),\\\\,',\\s]+", "反射调用");

    private final String pattern;

    private final String remark;

    MethodEnum(String pattern, String remark) {
        this.pattern = pattern;
        this.remark = remark;
    }

    /**
     * 从调用字符串获得调用类型
     * @param target 调用字符串
     * @return 调用类型
     * @throws Exception 不支持的调用方式
     */
    public static MethodEnum parse(String target) throws Exception {
        if (ReUtil.isMatch(HTTP.pattern, target)) {
            return HTTP;
        }else if (ReUtil.isMatch(REFLECT.pattern, target)) {
            return REFLECT;
        }
        throw new Exception("不支持的调用方式！");
    }

    /**
     * 验证调用字符串是否合法
     * @param target 调用字符串
     * @return false-合法，true-不合法
     */
    public static boolean verify(String target) {
        try {
            parse(target);
            return false;
        }catch (Exception ignored) {
            return true;
        }
    }
}