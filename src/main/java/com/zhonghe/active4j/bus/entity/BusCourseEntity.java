package com.zhonghe.active4j.bus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhonghe.active4j.common.entity.BaseEntity;
import com.zhonghe.active4j.core.annotation.QueryField;
import com.zhonghe.active4j.core.model.QueryCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 课程表
 * @author teli_
 *
 */
@TableName("bus_course")
@Getter
@Setter
public class BusCourseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7516587511068473316L;

	/**
	 * 标题
	 */
	@TableField("TITLE")
	@QueryField(condition=QueryCondition.like, queryColumn="TITLE")
	private String title;
	
	/**
	 * 课时
	 */
	@TableField("CLASS")
	private Integer classes;
	
	/**
	 * 老师Id
	 */
	@TableField("TEACHER_ID")
	@QueryField(condition=QueryCondition.eq, queryColumn="TEACHER_ID")
	private String teacherId;

	/**
	 * 老师Name
	 */
	@TableField("TEACHER_NAME")
	@QueryField(condition=QueryCondition.eq, queryColumn="TEACHER_NAME")
	private String teacherName;
	
}
