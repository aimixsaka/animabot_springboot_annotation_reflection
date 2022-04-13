package com.yanfang.animabot.functions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanfang.animabot.config.LongConfig;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SendImage
{
    // url
    private static final String URL = "https://rt.huashi6.com/search/all";

    public static void sendImgFromUrl(Contact contact, long recallTime) throws IOException
    {
        String Result = null;
        Map<String, String> params = new HashMap<>();
        params.put("word", "萝莉");
        params.put("index", Integer.toString(LongConfig.getRandomNumber()));
        try
        {
            String url = getUrl(params);
            java.net.URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            InputStream in = connection.getInputStream();
            ExternalResource resource = ExternalResource.Companion.create(in);
            Image image = ExternalResource.uploadAsImage(resource, contact);
            if (recallTime != 0)
            {
                contact.sendMessage(image).recallIn(recallTime);
                resource.close();
            }
            else
            {
                contact.sendMessage(image);
                resource.close();
            }
        }
        catch (IOException e)
        {
            log.info("IOException: " + e.getMessage());
        }
    }

    private static String getUrl(Map<String, String> params) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(URL);
        // 参数数组
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String json = EntityUtils.toString(httpEntity, "UTF-8");
        EntityUtils.consume(httpEntity);
        httpResponse.close();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = (JSONArray) (((JSONObject)jsonObject.get("data")).get("works"));
        int index = (int) (Math.random() * jsonArray.size());
        JSONObject jsonObject1 = (JSONObject) (((JSONObject) jsonArray.get(index)).get("coverImage"));
        return "https://img2.huashi6.com/" + jsonObject1.get("path");
    }
}
