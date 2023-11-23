package com.wtb.javatool.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fy.wetoband.tool.AbstractTool;
import com.wtb.javatool.constant.Constants;
import com.wtb.javatool.service.DocumentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {
    //获取FileServer的URL
    @Override
    public String getFileServerByFacilityTypeID(String facilityTypeID, String defaultUrl, AbstractTool tool) throws Exception {
        String fileServerUrl = null;

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("format", "json");
        params.put("access_token", tool.getAccessToken());
        params.put("aid", Constants.AID);
        params.put("gid", tool.getFacilityBandID());

        String url = "https://www.wetoband.com/core//core/v4/facility";
        JSONObject query = new JSONObject();
        query.put("facilityBandID", tool.getFacilityBandID());
        query.put("facilityTypeID", facilityTypeID);
        JSONObject extend = new JSONObject();
        extend.put("facilityProperties", new JSONArray());
        params.put("query", query.toString());
        params.put("extend", extend.toString());

        HttpResponse response = HttpRequest.get(url)
                .header("wtbauthc-trust", "true")
                .form(params)
                .execute();
        int status = response.getStatus();
        if(status != 200){
            System.out.printf("请求核心出错,错误信息为[%s]%n", response.body());
            return defaultUrl;

        }
        //开始解析返回结果
        JSONObject obj = JSONObject.parseObject(response.body());
        int total = obj.getInteger("total");
        if(total < 1){
            System.out.println((String.format("获取到的设施区[%s]的文件缓存服务器设施为空，使用默认的文件缓存服务器[%s]", tool.getFacilityBandID(), Constants.PUBLIC_FILE_SERVER_URL)));
            return defaultUrl;
        }
        JSONArray array = obj.getJSONArray("rows");
        JSONObject data = array.getJSONObject(0);
        JSONArray facilityProperties = data.getJSONArray("facilityProperties");
        for(Object propertyObj : facilityProperties){
            JSONObject property = (JSONObject)propertyObj;
            String facilityPropertyName = property.getString("facilityPropertyName");
            if(Constants.FACILITY_PROPERTY_NAME.equals(facilityPropertyName)){
                fileServerUrl = property.getString("facilityPropertyValue");
            }
        }
        return fileServerUrl;
    }
}
