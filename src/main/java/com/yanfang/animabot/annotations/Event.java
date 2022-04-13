package com.yanfang.animabot.annotations;

import com.yanfang.animabot.enums.EventEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Events.class)
public @interface Event
{
    EventEnum eventType() default EventEnum.NONE;
    boolean authority() default false;
    boolean judgeByMethod() default false;
    String message() default "";

}
