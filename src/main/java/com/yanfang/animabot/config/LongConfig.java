package com.yanfang.animabot.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LongConfig
{

    // 欢迎语
    public static final String GROUP_WELCOME_MESSAGE = "おかえりなさい";

    // 配置调试者（masters）
    public static final HashSet<Long> mastersIDSet = new HashSet<Long>()
    {{
        add(3224918011L);
        add(2339168544L);
    }};
    // 翻译类型映射
    public static Map<String, String> getMap()
    {
        Map<String, String> fromToMap = new HashMap<>();
        fromToMap.put("英", "en");
        fromToMap.put("中", "zh-CHS");
        fromToMap.put("日", "ja");
        fromToMap.put("韩", "ko");
        fromToMap.put("法", "fr");
        fromToMap.put("西班牙", "es");
        fromToMap.put("葡萄牙", "pt");
        fromToMap.put("意", "it");
        fromToMap.put("俄", "ru");
        fromToMap.put("德", "de");
        fromToMap.put("粤", "yue");
        fromToMap.put("芬兰", "fi");
        fromToMap.put("希腊", "el");
        fromToMap.put("泰", "th");
        fromToMap.put("乌", "uk");
        return fromToMap;
    }

    public static final String HELP =
            "=====AnimaBot=====\n" +
                    "------Version:1.1-----\n"
                    +"------------------------------------------\n"
                    +"1. Help 获取help\n"
                    +"2. /吃什么 随机食物\n"
                    +"3. /喝什么 随机饮料\n"
                    +"4. /is 随机名\n"
                    +"5. /吃什么+空格+食物名 添加食物（多个食物之间以空格分割，其他同理）\n"
                    +"6. /目标语言名 内容 翻译为目标语言(用/languages获取可用语言)";

    // 类型提示
    public static final String typeMap =
            "/英 译为英文\n"
                    +"/中 译为中文\n"
                    +"/日 译为日文\n"
                    +"/韩 译为韩文\n"
                    +"/法 译为法语\n"
                    +"/西班牙 译为西班牙语\n"
                    +"/葡萄牙 译为葡萄牙语\n"
                    +"/意 译为意大利语\n"
                    +"/俄 译为俄语\n"
                    +"/德 译为德语\n"
                    +"/粤 译为粤语\n"
                    +"/芬兰 译为芬兰语\n"
                    +"/希腊 译为希腊语\n"
                    +"/泰 译为泰语\n"
                    +"/乌 译为乌克兰语";

    public static int getRandomNumber()
    {
        return (int) (Math.random() * 100 + 1);
    }
}
