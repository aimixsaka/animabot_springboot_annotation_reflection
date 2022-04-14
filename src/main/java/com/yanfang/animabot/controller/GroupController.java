package com.yanfang.animabot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanfang.animabot.annotations.Event;
import com.yanfang.animabot.annotations.Events;
import com.yanfang.animabot.config.LongConfig;
import com.yanfang.animabot.enums.EventEnum;
import com.yanfang.animabot.functions.SendImage;
import com.yanfang.animabot.functions.Translate;
import com.yanfang.animabot.util.CheckUtil;
import com.yanfang.animabot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class GroupController
{
    @Autowired
    RedisUtil redisUtil;
    /**
     * 有道翻译
     * @param event 传入的事件
     * @param messages 消息链
     */
    @Events({@Event(eventType = EventEnum.GROUPEVENT, authority = true, judgeByMethod = true),
            @Event(eventType = EventEnum.MASTEREVENT, authority = true, judgeByMethod = true)})
    public void translate(MessageEvent event, MessageChain messages)
    {
        String message = messages.contentToString();
        if (CheckUtil.checkLan(message))
        {
            String pattern = "/(.*?) (.*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(message);
            if (m.find())
            {
                String msg = m.group(2);
                String lan = m.group(1);
                Map<String, String> map = LongConfig.getMap();
                String json = Translate.getResult(msg, map.get(lan));
                JSONObject obj = JSONObject.parseObject(json);
                String result = ((JSONArray) obj.get("translation")).get(0).toString();
                log.info("翻译结果：===》" + result);
                event.getSubject().sendMessage(result);
            }
            else event.getSubject().sendMessage("抱歉没有匹配:<");
        }
    }

    /*help页*/
    @Events({@Event(eventType = EventEnum.GROUPEVENT, authority = true, message = "Help"),
            @Event(eventType = EventEnum.MASTEREVENT, authority = true, message = "Help")})
    public void sendHelp(MessageEvent event, MessageChain messages)
    {
        event.getSubject().sendMessage(LongConfig.HELP);
    }

    /*发送食物饮料*/
    @Events({@Event(eventType = EventEnum.GROUPEVENT, authority = true, judgeByMethod = true),
            @Event(eventType = EventEnum.MASTEREVENT, authority = true, judgeByMethod = true)})
    public void sendDrinkAndFood(MessageEvent event, MessageChain messages)
    {
        String message = messages.contentToString();
        // 获取随机食物
        if (message.equals("/吃什么"))
        {
            event.getSubject().sendMessage(redisUtil.getRandom("foodList"));
        }
        // 获取随机饮料
        else if (message.equals("/喝什么"))
        {
            event.getSubject().sendMessage(redisUtil.getRandom("drinkList"));
        }
        // 获取随机形象
        else if (message.equals("/is"))
        {
            event.getSubject().sendMessage(redisUtil.getRandom("randomList"));
        }
    }

    /*添加食物饮料*/
    @Events({@Event(eventType = EventEnum.GROUPEVENT, authority = true, judgeByMethod = true),
            @Event(eventType = EventEnum.MASTEREVENT, authority = true, judgeByMethod = true)})
    public void addDrinkAndFood(MessageEvent event, MessageChain messages)
    {
        String message = messages.contentToString();
        // 添加形象
        if(CheckUtil.check(message) && message.contains("/is"))
        {
            String regex = "\\s+";
            String[] arr = message.split(regex);
            if (redisUtil.add("randomList", arr))
                event.getSubject().sendMessage("添加成功！");
            else
                event.getSubject().sendMessage("添加失败呜呜呜");
        }
        // 添加食物
        else if (CheckUtil.check(message) && message.contains("/吃什么"))
        {
            String regex = "\\s+";
            String[] arr = message.split(regex);
            if (redisUtil.add("foodList", arr))
                event.getSubject().sendMessage("添加成功！");
            else
                event.getSubject().sendMessage("添加失败呜呜呜");

        }
        // 添加饮料
        else if (CheckUtil.check(message) && message.contains("/喝什么"))
        {
            String regex = "\\s+";
            String[] arr = message.split(regex);
            if (redisUtil.add("drinkList", arr))
                event.getSubject().sendMessage("添加成功！");
            else
                event.getSubject().sendMessage("添加失败呜呜呜");
        }
    }

    /*发送lolita*/
    @Events({@Event(eventType = EventEnum.GROUPEVENT, authority = true, message = "/lolita"),
            @Event(eventType = EventEnum.MASTEREVENT, authority = true, message = "/lolita")})
    public void sendPhoto(MessageEvent event, MessageChain messages)
    {
        String message = messages.contentToString();
        if (message.equals("/lolita"))
        {
            try
            {
                SendImage.sendImgFromUrl(event.getSubject(), 0);
            }
            catch (IOException e)
            {
                log.warn("IOException: " + e.getMessage());
            }

        }
    }

    /*群临时会话*/
    @Events(@Event(eventType = EventEnum.TEMPEVENT, authority = true, judgeByMethod = true))
    public void tempTalking(MessageEvent event)
    {
        event.getSender().sendMessage("I am sorry, I only takes orders from Groups or Masters");
    }
}
