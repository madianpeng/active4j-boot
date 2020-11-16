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
import com.zhonghe.active4j.system.entity.SysUserEntity;
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
		model.addAttribute("id", uuid);
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
			busCourseService.save(busCourseEntity);
		}catch(Exception e) {
			log.error("保存用户信息报错，错误信息:" + e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
	
	
	/**
	 *  删除操作
	 * @param busCourseEntity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除用户信息", memo = "删除了用户信息")
	public AjaxJson delete(BusCourseEntity busCourseEntity) {
		AjaxJson j = new AjaxJson();
		try {
			busCourseService.removeById(busCourseEntity.getId());
		}catch(Exception e) {
			log.error("删除用户信息出错，错误信息:" + e.getMessage() );
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
}
