package com.yanfang.animabot;

import com.yanfang.animabot.bot.AnimaBot;
import com.yanfang.animabot.util.ReflectionUtil;
import com.yanfang.animabot.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AnimaBotApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(AnimaBotApplication.class, args);
        ReflectionUtil.setFunctionMethods();
        AnimaBot bot = SpringUtil.getBean(AnimaBot.class);
        bot.start();


    }

}
