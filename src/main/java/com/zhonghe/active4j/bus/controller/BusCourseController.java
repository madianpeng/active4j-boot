package com.zhonghe.active4j.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhonghe.active4j.bus.entity.BusCourseEntity;
import com.zhonghe.active4j.bus.service.BusCourseService;
import com.zhonghe.active4j.bus.wrapper.BusCourseWrapper;
import com.zhonghe.active4j.common.controller.BaseController;
import com.zhonghe.active4j.core.annotation.Log;
import com.zhonghe.active4j.core.model.AjaxJson;
import com.zhonghe.active4j.core.model.LogType;
import com.zhonghe.active4j.core.model.PageInfo;
import com.zhonghe.active4j.core.query.QueryUtils;
import com.zhonghe.active4j.core.util.MyBeanUtils;
import com.zhonghe.active4j.core.util.ResponseUtil;
import com.zhonghe.active4j.core.util.ShiroUtils;
import com.zhonghe.active4j.core.util.UUIDUtil;
import com.zhonghe.active4j.system.entity.SysFileEntity;
import com.zhonghe.active4j.system.entity.SysUserEntity;
import com.zhonghe.active4j.system.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 
 * @title SysUserController.java
 * @description 
		  课程管理
 * @time  2019年12月3日 上午10:14:28
 * @author chenxl
 * @version 1.0
 */
@Controller
@RequestMapping("/bus/course")
@Slf4j
public class BusCourseController extends BaseController {

	@Autowired
	private BusCourseService busCourseService;

	@Autowired
	private SysFileService sysFileService;
	

	/**
	 * 跳转到列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model,String type) {
		return "bus/course/course_list.html";
	}
	
	/**
	 *  获取表格数据 树形结构
	 * @param busCourseEntity
	 * @param request
	 * @return
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public void datagrid(BusCourseEntity busCourseEntity, PageInfo<BusCourseEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<BusCourseEntity> queryWrapper = QueryUtils.installQueryWrapper(busCourseEntity, request.getParameterMap());
		
		//执行查询
		IPage<BusCourseEntity> lstResult = busCourseService.page(page.getPageEntity(), queryWrapper);

		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BusCourseWrapper(lstResult).wrap());

	}
	
	/**
	 * 跳转到页面
	 * @param busCourseEntity
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(BusCourseEntity busCourseEntity, Model model) {
		String uuid = UUIDUtil.getUUID();
		model.addAttribute("linkId", uuid);
		return "bus/course/course_add.html";
	}
	
	
	/**
	 * 保存方法
	 * @param busCourseEntity
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public AjaxJson save(BusCourseEntity busCourseEntity)  {
		AjaxJson j = new AjaxJson();
		
		try{
			if (StringUtils.isEmpty(busCourseEntity.getId())) {
				//新增课程
				busCourseEntity.setTeacherId(ShiroUtils.getSessionUser().getId());
				busCourseEntity.setTeacherName(ShiroUtils.getSessionUser().getUserName());
				busCourseService.save(busCourseEntity);
			}else {
				//编辑课程
				BusCourseEntity temp = busCourseService.getById(busCourseEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(busCourseEntity, temp);
				busCourseService.updateById(temp);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}


	/**
	 * 跳转到编辑页面
	 * @param busCourseEntity
	 * @param model
	 * @return
	 */
	@RequestMapping("/update")
	public String update(BusCourseEntity busCourseEntity, Model model) {
		busCourseEntity = busCourseService.getById(busCourseEntity.getId());

		//视频文件信息
		QueryWrapper<SysFileEntity> wrapper = new QueryWrapper<>();
		//根据关联Id查询
		wrapper.eq("LINKID",busCourseEntity.getLinkId());
		List<SysFileEntity> fileList = sysFileService.list(wrapper);
		model.addAttribute("course", busCourseEntity);
		model.addAttribute("fileList", fileList);
		return "bus/course/course_update.html";
	}
	
	/**
	 *  删除操作
	 * @param busCourseEntity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public AjaxJson delete(BusCourseEntity busCourseEntity) {
		AjaxJson j = new AjaxJson();
		try {
			busCourseService.removeById(busCourseEntity.getId());
		}catch(Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
}
