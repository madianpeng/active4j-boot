package com.zhonghe.active4j.func.upload.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhonghe.active4j.core.util.UUIDUtil;
import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public class FileUtil {
    public static String uploadFile(MultipartFile file,String path) {
        String result = "";
        try {
            String domain = "http://madianpeng.top:8080";
            String group = "group1";
            //获取文件后缀名
            String extName = FileUploadUtils.getExtension(file);
            //uuid创建文件名
            String fileName = UUIDUtil.getUUID() + "." + extName;

            OkHttpClient httpClient = new OkHttpClient();
            MultipartBody multipartBody = new MultipartBody.Builder().
                    setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"),
                                    file.getBytes()))
                    .addFormDataPart("output", "json")
                    .addFormDataPart("path", path)
                    .build();

            Request request = new Request.Builder()
                    .url(domain + File.separator + group + File.separator + "upload")
                    .post(multipartBody)
                    .build();

            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                    Map<String,Object> maps = (Map)JSON.parse(result);
                    result =domain+(String) maps.get("path");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static Response deleteFile(String domain, String group, String path) {
        try {
            OkHttpClient httpClient = new OkHttpClient();
            MultipartBody multipartBody = new MultipartBody.Builder().
                    setType(MultipartBody.FORM)
                    .addFormDataPart("path", path)
                    .build();

            Request request = new Request.Builder()
                    .url(domain + File.separator + group + File.separator + "delete")
                    .post(multipartBody)
                    .build();

            return httpClient.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}