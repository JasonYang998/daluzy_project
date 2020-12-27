package com.daluzy.daluzy_project.daluzy_project.tools;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonTool {
    /**
     * json转对象
     * @param str
     * @param tClass
     * @param <T>
     * @return
     */
    public static  <T> T  jsonToObject(String str, Class<T> tClass){
        try{
            if (str != null && !str.isEmpty()){
                T data = JSONArray.parseObject(str,tClass);
                return data;
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("jsonException", e.getMessage());
            return null;
        }
    }

    /**
     * json 转集合
     * @param str
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String str, Class<T> tClass){
        try{
            if (str != null && !str.isEmpty()){
                List<T> list = JSONArray.parseArray(str,tClass);
                return list;
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("jsonException", e.getMessage());
            return null;
        }
    }

    public static <T> String objectToString(T t){
        return JSONObject.toJSONString(t);
    }
}
