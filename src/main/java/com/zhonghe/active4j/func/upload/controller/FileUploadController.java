package com.zhonghe.active4j.func.upload.controller;

import com.zhonghe.active4j.common.controller.BaseController;
import com.zhonghe.active4j.core.model.AjaxJson;
import com.zhonghe.active4j.core.util.DateUtils;
import com.zhonghe.active4j.core.util.UUIDUtil;
import com.zhonghe.active4j.func.upload.util.FileUploadUtils;
import com.zhonghe.active4j.system.entity.SysFileEntity;
import com.zhonghe.active4j.system.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @title UploadController.java
 * @description 
		上传文件管理
 * @time  2019年12月16日 上午9:54:32
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/upload")
@Slf4j
public class FileUploadController extends BaseController {

	@Autowired
	SysFileService sysFileService;
	

	private static final String prefix_page = "func/upload/";
	
	/**
	 * 上传的组合路径
	 */
	private final String uploadPath = "\\Uploads\\";
	
	private static long cacheTime = 10 * 60l * 60l * 1000l;
	
	/**
	 * 
	 * @description
	 *  	跳转上传下载页面
	 * @params
	 * @return String
	 * @author guyp
	 * @time 2019年12月16日 上午9:54:47
	 */
	@RequestMapping("/upload")
	public String layedit(Model model) {
		return prefix_page + "upload.html";
	}
	
	/**
	 * 
	 * @description
	 *  	文件本地上传
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月16日 上午10:36:22
	 */
	@RequestMapping("/localupload")
	@ResponseBody
	public AjaxJson localUpload(MultipartHttpServletRequest request,String linkId, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			log.info("进入本地文件上传接口");
			Map<String, MultipartFile> fileMap = request.getFileMap();
			Map<String, Object> attributesMap = new HashMap<String, Object>();
			String key = "";
			
			for(Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				SysFileEntity sysFileEntity = new SysFileEntity();
				// 获取上传文件对象
				MultipartFile mf = entity.getValue();
				//获取文件后缀名
				String extName = FileUploadUtils.getExtension(mf);

				//uuid创建文件名
				String fileName = UUIDUtil.getUUID() + "." + extName;
				//获取项目根目录
				String filePath = System.getProperty("user.dir");
				//获得文件输入流
				InputStream inputStream = mf.getInputStream();
				//保存文件
				File f = new File(filePath + uploadPath + DateUtils.getDateYYYY_MM_DD(), fileName);
                if(!f.exists()) {
                	f.getParentFile().mkdirs();
                	FileCopyUtils.copy(inputStream, new FileOutputStream(f));//把文件写入磁盘
                }
                String tmpKey = filePath + uploadPath + DateUtils.getDateYYYY_MM_DD() + "\\" + fileName;
                //替换"\"
                key = tmpKey.replace("\\", "/");
                log.info("创建本地文件：{}", key);
                //关闭流
                inputStream.close();
                attributesMap.put("key", key);

				sysFileEntity.setFileName(mf.getOriginalFilename());
				sysFileEntity.setFilePath(key);
				sysFileEntity.setLinkId(linkId);
				sysFileService.save(sysFileEntity);
			}
			
			j.setAttributes(attributesMap);
		}catch(Exception e) {
			log.error("本地文件上传报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	本地下载
	 * @params
	 *      key 本地资源地址
	 * @return ResponseEntity<InputStreamResource>
	 * @author guyp
	 * @time 2019年12月16日 上午11:34:12
	 */
	@RequestMapping("/localdownload")
	@ResponseBody
    public ResponseEntity<InputStreamResource> localDownload(String key) {
		try {
			log.info("进入本地文件下载接口");
		    FileSystemResource file = new FileSystemResource(key);
		    //设置头信息
		    HttpHeaders headers = new HttpHeaders();
		    //缓存头Cache-Control
		    //no-cache：不直接使用缓存，要求向服务器发起（新鲜度校验）请求
		    //no-store：所有内容都不会被保存到缓存或Internet临时文件中
		    //must-revalidate：当前资源一定是向原服务器发去验证请求的，若请求失败会返回504（而非代理服务器上的缓存）
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    //设置下载的附件(filename必须处理中文名称)
		    headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
		    //不要网页存于缓存之中
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");
  
			return ResponseEntity  
			        .ok()  
			        .headers(headers)
			        .contentLength(file.contentLength())
			        .contentType(MediaType.APPLICATION_OCTET_STREAM)  
			        .body(new InputStreamResource(file.getInputStream()));
		} catch (IOException e) {
			log.error("本地文件下载报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return null;
    }
}
