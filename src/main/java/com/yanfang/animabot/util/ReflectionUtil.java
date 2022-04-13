package com.yanfang.animabot.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class ReflectionUtil
{
    private static List<Object[]> functionMethods;

    /**
     * 扫描所有带有@Function自定义注解的类，并获取它们的所有方法
     */
    public static void setFunctionMethods()
    {
        log.info("Get Methods...");
        functionMethods = new ArrayList<>();
        // 通过getBeansWithAnnotation获取被自定义注解标注的bean
        Map<String, Object> functions = SpringUtil.getApplicationContext().getBeansWithAnnotation(Controller.class);
        for (Map.Entry<String, Object> entry : functions.entrySet())
        {
            Object value = entry.getValue();
            Class<?> cl = AopUtils.getTargetClass(value);
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods)
            {
                log.info("Find a function method： " + method.getName() + "()");
                functionMethods.add(new Object[] {cl, method});
            }

        }
    }

    /**
     * 获取方法和类型列表
     * @return 方法类型列表
     */
    public static List<Object[]> getFunctionMethods()
    {
        if (functionMethods.size() == 0)
            log.error("反射调用所得方法列表为空");
        return functionMethods;
    }
}
