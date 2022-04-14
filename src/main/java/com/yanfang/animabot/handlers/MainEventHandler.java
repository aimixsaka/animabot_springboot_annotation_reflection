package com.yanfang.animabot.handlers;

import com.yanfang.animabot.annotations.Event;
import com.yanfang.animabot.annotations.Events;
import com.yanfang.animabot.enums.EventEnum;
import com.yanfang.animabot.util.ReflectionUtil;
import com.yanfang.animabot.util.SpringUtil;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 *修改了的事件监听类
 */
@Slf4j
public class MainEventHandler extends SimpleListenerHost
{
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception)
    {
        log.warn(exception.toString());
        exception.printStackTrace();
    }

    /**
     * 处理事件，进行方法执行的逻辑判断（筛选
     * @param event 监听到的事件
     * @return 监听状态
     * @throws Exception 异常
     */
    @NotNull
    @net.mamoe.mirai.event.EventHandler
    public ListeningStatus onMessage(@NotNull MessageEvent event) throws Exception
    {
        log.info("Receive an message: " + event.getMessage());
        for (Object[] objects : ReflectionUtil.getFunctionMethods())
        {
            Method method = (Method) objects[1];
            // 获取注解中Event注解的集合
            // 利用Event注解数和authority的布尔值来灵活分配权限
            Events events = method.getAnnotation(Events.class);
            for (Event eventAnnotation : events.value())
            {
                // 是否授权
                if (eventAnnotation.authority())
                {   // 有复杂逻辑就将judgeByMethod设为true， 交给方法进行复杂逻辑处理
                    if (eventAnnotation.judgeByMethod() || (eventAnnotation.message().equals(event.getMessage().contentToString())))
                    {
                        if (event.getSubject() instanceof Group && eventAnnotation.eventType() == EventEnum.GROUPEVENT)
                        {
                            invokeMethod((Class<?>) objects[0], method, event);
                            break;
                        }
                        else if (event.getSubject() instanceof Friend && eventAnnotation.eventType() == EventEnum.MASTEREVENT)
                        {
                            invokeMethod((Class<?>) objects[0], method, event);
                            break;
                        }
                        else if (event.getSubject() instanceof Member && eventAnnotation.eventType() == EventEnum.TEMPEVENT)
                        {
                            invokeMethod((Class<?>) objects[0], method, event);
                            break;
                        }
                    }

                }
                else {event.getSubject().sendMessage("申し訳ございませんが、アクセス許可がありません"); break;}
            }

        }
        return ListeningStatus.LISTENING;
    }

    /**
     * 执行具体的方法，传入event参数
     * @param clazz 执行方法所在类类
     * @param method 执行的方法
     * @param event 传入事件
     * @throws Exception 异常
     */
    private void invokeMethod(Class<?> clazz, Method method, MessageEvent event) throws Exception
    {
        // 方法的参数集合
        Parameter[] parameters = method.getParameters();
        final int length = parameters.length;
        if (length > 0)
        {
            // 创建与方法参数长度相同的数组，以传入参数执行方法
            Object[] args = new Object[length];
            for (int index = 0; index < length; index++)
            {
                Parameter parameter = parameters[index];
                Class<?> cls = (Class<?>) parameter.getParameterizedType();
                if (MessageEvent.class.isAssignableFrom(cls))
                    // 把handler的event参数传到具体的方法里
                    args[index] = event;
                else if (MessageChain.class.isAssignableFrom(cls))
                    // 如果需要消息进行处理，就传入消息
                    args[index] = event.getMessage();
                // 这里关于反射一点遗憾的是如果有不确定的参数就传不了实参进去，有点问题
                else args[index] = null;
            }
            method.invoke(SpringUtil.getBean(clazz), args);
        }
        else method.invoke(SpringUtil.getBean(clazz));
    }
}
