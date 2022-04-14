package com.yanfang.animabot.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis操作
 */
@Component
public class RedisUtil
{
    @NotNull
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 获取随机值
     *
     * @param key 键值
     * @return 随机值
     */
    public String getRandom(String key)
    {
        return stringRedisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 添加value
     *
     * @param arr 实际添加的内容
     * @param key 添加的类型
     * @return 是否添加成功
     */
    public boolean add(String key, String[] arr)
    {
        Long originLen = stringRedisTemplate.opsForSet().size(key);
        System.out.println(originLen);
        for (int i = 1; i < arr.length; i++)
            stringRedisTemplate.opsForSet().add(key, arr[i]);
        Long nowLen = stringRedisTemplate.opsForSet().size(key);
        System.out.println(nowLen);
        if (nowLen > originLen) return true;
        else return false;
    }

}
