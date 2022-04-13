package com.yanfang.animabot.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取bean,起bean工厂的作用
 */
@Component
public class SpringUtil implements ApplicationContextAware
{
    private static  ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException
    {
        if (SpringUtil.applicationContext == null)
        {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    // 通过name获取bean
    public static Object getBean(String name)
    {
        return getApplicationContext().getBean(name);
    }

    // 通过class获取bean
    public static <T> T getBean(Class<T> clazz)
    {
        return getApplicationContext().getBean(clazz);
    }

    // 通过name和class获取指定bean
    public static <T> T getBean(String name, Class<T> clazz)
    {
        return getApplicationContext().getBean(name, clazz);
    }

}
