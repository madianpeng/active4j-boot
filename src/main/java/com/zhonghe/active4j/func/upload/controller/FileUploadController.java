package com.zhonghe.active4j.func.upload.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhonghe.active4j.common.controller.BaseController;
import com.zhonghe.active4j.core.model.AjaxJson;
import com.zhonghe.active4j.func.upload.util.FileUtil;
import com.zhonghe.active4j.system.entity.SysFileEntity;
import com.zhonghe.active4j.system.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guyp
 * @version 1.0
 * @title UploadController.java
 * @description 上传文件管理
 * @time 2019年12月16日 上午9:54:32
 */
@Controller
@RequestMapping("/func/upload")
@Slf4j
public class FileUploadController extends BaseController {

    @Autowired
    SysFileService sysFileService;


    private static final String prefix_page = "func/upload/";

    private static final String UPLOAD_PATH = "http://madianpeng.top:8080/group1/upload";

    /**
     * 上传的组合路径
     */
    private final String uploadPath = "\\Uploads\\";

    private static long cacheTime = 10 * 60l * 60l * 1000l;

    /**
     * @return String
     * @description 跳转上传下载页面
     * @params
     * @author guyp
     * @time 2019年12月16日 上午9:54:47
     */
    @RequestMapping("/upload")
    public String layedit(Model model) {
        return prefix_page + "upload.html";
    }

    /**
     * @return AjaxJson
     * @description 文件本地上传
     * @params
     * @author guyp
     * @time 2019年12月16日 上午10:36:22
     */
    @RequestMapping("/localupload")
    @ResponseBody
    public AjaxJson localUpload(MultipartHttpServletRequest request, String linkId, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            log.info("进入本地文件上传接口");
            Map<String, MultipartFile> fileMap = request.getFileMap();
            Map<String, Object> attributesMap = new HashMap<String, Object>();


            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                SysFileEntity sysFileEntity = new SysFileEntity();
                // 获取上传文件对象
                MultipartFile mf = entity.getValue();
                //上传文件
                String key  = FileUtil.uploadFile(mf,"/course");
                //关闭流
                attributesMap.put("key", key);
                sysFileEntity.setFileName(mf.getOriginalFilename());
                sysFileEntity.setFilePath(key);
                sysFileEntity.setLinkId(linkId);
                sysFileEntity.setFileSize(mf.getSize()/1024+"kb");
                sysFileService.save(sysFileEntity);
            }

            j.setAttributes(attributesMap);
        } catch (Exception e) {
            log.error("本地文件上传报错，错误信息：{}", e.getMessage());
            j.setSuccess(false);
            e.printStackTrace();
        }

        return j;
    }


    /**
     * @return ResponseEntity<InputStreamResource>
     * @description 本地下载
     * @params key 本地资源地址
     * @author guyp
     * @time 2019年12月16日 上午11:34:12
     */
    @RequestMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String filePath) {
        AjaxJson ajaxJson = new AjaxJson();

        try {
            QueryWrapper<SysFileEntity> sysFileEntityQueryWrapper = new QueryWrapper<>();
            sysFileEntityQueryWrapper.eq("FILEPATH", filePath);
            sysFileService.remove(sysFileEntityQueryWrapper);
        } catch (Exception e) {
            ajaxJson.setSuccess(false);
            e.printStackTrace();
        }

        return ajaxJson;
    }
}
