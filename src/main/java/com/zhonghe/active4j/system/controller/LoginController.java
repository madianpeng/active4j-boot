package com.zhonghe.active4j.system.controller;

import com.zhonghe.active4j.core.util.MyBeanUtils;
import com.zhonghe.active4j.system.entity.SysUserEntity;
import com.zhonghe.active4j.system.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhonghe.active4j.common.constant.GlobalConstant;
import com.zhonghe.active4j.core.annotation.Log;
import com.zhonghe.active4j.core.model.AjaxJson;
import com.zhonghe.active4j.core.model.LogType;
import com.zhonghe.active4j.core.util.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title LoginController.java
 * @description 
		  登录功能
		 系统首页等默认页面的跳转
 * @time 2019年12月3日 上午10:10:11
 * @author chenxl
 * @version 1.0
 */
@Controller
@Slf4j
public class LoginController {

	@Autowired
	SysUserService sysUserService;
	
	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "index.html";
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index2() {
		return "index.html";
	}

	/**
	 * 没有授权错误跳转页面
	 * @return
	 */
	@RequestMapping("/403")
	public String noAuth() {
		return "403.html";
	}
	
	/**
	 * 异常错误，跳转页面
	 * @return
	 */
	@RequestMapping("/500")
	public String error() {
		return "500.html";
	}
	
	/**
	 * 跳转到首页主内容
	 * 
	 * @return
	 */
	@RequestMapping("/home")
	public String home() {
		return "home/console.html";
	}
	
	/**
	 * 跳转登录页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {

		return "login/login.html";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register() {
		return "login/register.html";
	}
	
	/**
	 * 登出
	 * @return
	 */
	@RequestMapping("/logout")
	@Log(type = LogType.logout, name = "用户登出", memo = "用户已登出")
	public String logout() {
		ShiroUtils.logout();
		return "redirect:/login";
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	@Log(type = LogType.login, name = "用户登录", memo = "用户成功登录")
	public AjaxJson loginAction(String username, String password, String vercode) {
		AjaxJson j = new AjaxJson();
		
		try {
			// 后端校验
			if (StringUtils.isEmpty(username)) {
				j.setSuccess(false);
				j.setMsg("用户名不能为空");
				return j;
			}

			if (StringUtils.isEmpty(password)) {
				j.setSuccess(false);
				j.setMsg("密码不能为空");
				return j;
			}

			if (StringUtils.isEmpty(vercode)) {
				j.setSuccess(false);
				j.setMsg("验证码不能为空");
				return j;
			}

			// 验证码的校验
			if (!StringUtils.equalsIgnoreCase(vercode, ShiroUtils.getSessionValue(GlobalConstant.SESSION_KEY_OF_RAND_CODE))) {
				j.setSuccess(false);
				j.setMsg("验证码填写错误");
				return j;
			}

			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			ShiroUtils.getSubject().login(token);

		} catch (IncorrectCredentialsException e) {
			j.setSuccess(false);
			j.setMsg("用户名或密码填写错误");
		} catch (UnknownAccountException e) {
			j.setSuccess(false);
			j.setMsg("用户名或密码填写错误");
		} catch (LockedAccountException e) {
			j.setSuccess(false);
			j.setMsg("当前账户已锁定，请联系管理员");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统错误，请联系管理员");
		}

		return j;

	}

	/**
	 * 登录
	 *
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson register(SysUserEntity sysUserEntity) {
		AjaxJson j = new AjaxJson();
		try{
			//学生角色Id
			String roleIds = "23ea7b27e135b9f852942828ebef2bd3";
			//后端校验
			if(StringUtils.isEmpty(sysUserEntity.getUserName())) {
				j.setSuccess(false);
				j.setMsg("用户名不能为空");
				return j;
			}

			if(StringUtils.isEmpty(sysUserEntity.getRealName())) {
				j.setSuccess(false);
				j.setMsg("用户姓名不能为空");
				return j;
			}

			if(StringUtils.isEmpty(sysUserEntity.getTelNo())) {
				j.setSuccess(false);
				j.setMsg("用户手机号不能为空");
				return j;
			}


			//新增保存
			if(StringUtils.isEmpty(sysUserEntity.getId())) {
				//设置salt盐
				sysUserEntity.setSalt(ShiroUtils.getRandomSalt());
				//设置加密密码
				sysUserEntity.setPassword(ShiroUtils.md5(sysUserEntity.getPassword(), sysUserEntity.getSalt()));
				//设置用户状态为正常
				sysUserEntity.setStatus("1");
				sysUserEntity.setType(SysUserEntity.TYPE_STU);
				sysUserService.saveUser(sysUserEntity, roleIds);
			}

		}catch(Exception e) {
			log.error("保存用户信息报错，错误信息:" + e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
		}

		return j;
	}

}
