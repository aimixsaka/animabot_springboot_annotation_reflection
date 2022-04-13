package com.yanfang.animabot.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有道接口
 */
@Slf4j
public class Translate
{
    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    private static final String APP_KEY = "1e1568342ff07df0";

    private static final String APP_SECRET = "ee9xZ5uJr717tINczxVS4YZWWhgZrwsK";

    private static String RESULT = null;

    private static CloseableHttpResponse httpResponse;

    /**
     * @param q 待翻译语言
     * @param to 翻译目标类型
     * @return 翻译结果
     */
    public static String getResult(String q, String to)
    {
        String Result = null;
        Map<String, String> params = new HashMap<>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "auto");
        params.put("to", to);
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        try
        {
            setHttpResponse(YOUDAO_URL, params);
            Result = getTranslation();
        }
        catch (IOException e)
        {
            log.warn("IOException: " + e.getMessage());
        }
        return Result;
    }

    /**
     * 发请求，获取HttpResponse响应对象
     */
    private static void setHttpResponse(String url, Map<String, String> params) throws IOException
    {
        // 创建HttpClient，这是必须的第一步
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // HttpPost,用于发送post请求（如果发送get，则创建HttpGet对象
        HttpPost httpPost = new HttpPost(url);
        // 请求参数列表，用于作为UrlEncodedFormEntity的参数，以构造符合特定条件的Entity
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        // 用setEntity方法包装Post请求参数（且只能用于Post。setParams方法可以用于Post和Get
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        // 用httpClient的execute方法发送Post请求，返回一个CloseableHttpResponse类型的响应
        httpResponse = httpClient.execute(httpPost);
    }

    private static String getTranslation() throws IOException
    {
        HttpEntity httpEntity = httpResponse.getEntity();
        String json = EntityUtils.toString(httpEntity, "UTF-8");
        EntityUtils.consume(httpEntity);
        Translate.RESULT = json;
        try
        {
            if (httpResponse != null)
            {
                httpResponse.close();
            }
        }
        catch (IOException e)
        {
            log.warn("Can't close the httpResponse");
        }
        return Translate.RESULT;
    }


    /**
     * @param string 待加密字段
     * @return 加密后字符串
     */
    private static String getDigest(String string)
    {
        if (string == null)
        {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try
        {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md)
            {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    /**
     * 截取字符串
     * @param q 输入的查询
     * @return 截取后的结果
     */
    private static String truncate (String q)
    {
        if (q == null)
        {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
