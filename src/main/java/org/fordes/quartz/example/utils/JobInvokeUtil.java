package org.fordes.quartz.example.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.fordes.quartz.example.enums.MethodEnum;
import org.fordes.quartz.example.model.JobEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务执行工具
 *
 * @author fordes on 2021/11/20
 */
public class JobInvokeUtil {
    /**
     * 执行方法
     *
     * @param job 系统任务
     */
    public static void invokeMethod(JobEntity job) throws Exception {
        String invokeTarget = job.getInvokeTarget();
        MethodEnum methodEnum = MethodEnum.parse(invokeTarget);
        switch (methodEnum) {
            case HTTP:
                HttpUtil.get(invokeTarget);
                break;
            case REFLECT:
                String beanName = getBeanName(invokeTarget);
                invokeMethod(BeanUtils.getBean(beanName), getMethodName(invokeTarget), getMethodParams(invokeTarget));
                break;
        }

    }


    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    public static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (CollUtil.isNotEmpty(methodParams) && methodParams.size() > 0) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }


    /**
     * 获取bean名称
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
        List<String> methodParams = StrUtil.split(methodStr, StrUtil.C_COMMA, true, true);
        return methodParams.stream().map(item -> {
            String str = StrUtil.trimToEmpty(item);
            // String字符串类型，包含'
            if (StrUtil.contains(str, "'")) {
                return new Object[]{StrUtil.replace(str, "'", StrUtil.EMPTY), String.class};
            }
            // boolean布尔类型，等于true或者false
            else if (StrUtil.equals(str, "true") || StrUtil.equalsIgnoreCase(str, "false")) {
                return new Object[]{Boolean.valueOf(str), Boolean.class};
            }
            // long长整形，包含L
            else if (StrUtil.containsIgnoreCase(str, "L")) {
                return new Object[]{Long.valueOf(StrUtil.replaceIgnoreCase(str, "L", "")), Long.class};
            }
            // double浮点类型，包含D
            else if (StrUtil.containsIgnoreCase(str, "D")) {
                return new Object[]{Double.valueOf(StrUtil.replaceIgnoreCase(str, "D", "")), Double.class};
            }
            // 其他类型归类为整形
            else {
                return new Object[]{Integer.valueOf(str), Integer.class};
            }
        }).collect(Collectors.toList());

    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        return methodParams.stream().map(e -> (Class<?>) e[1]).toArray(Class<?>[]::new);
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        return methodParams.stream().map(e -> e[0]).toArray(Object[]::new);
    }
}
