package cn.xuben99.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    public static String getJsonStrValueByKey(String json,String key){
        return (String) getJsonObjValueByKey(json,key);
    }

    public static int getJsonIntValueByKey(String json,String key){
        return (int)getJsonObjValueByKey(json,key);
    }

    public static Object getJsonObjValueByKey(String json,String key){
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.get(key);
    }
}
