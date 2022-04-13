package com.yanfang.animabot.util;

/**
 * 核查工具
 */
public class CheckUtil
{
    /**
     * 判断是否是要添加
     * @param message 获得的消息
     * @return 是否匹配
     */
    public static boolean check(String message)
    {
        if (message.matches("(/吃什么|/喝什么|/是什么) \\S.*"))
        {System.out.println();
            return true;}
        else
            return false;
    }

    /**
     * 判断是否是在语言表里
     * @param message 获得的消息
     * @return 是否匹配
     */
    public static boolean checkLan(String message)
    {
        if (message.matches("(/英|/中|/日|/韩|/法|/西班牙|/葡萄牙|/意|/俄|/德|/粤|/芬兰|/希腊|/泰|/乌) \\S.*"))
        {
            return true;
        }
        else
            return false;
    }
}
