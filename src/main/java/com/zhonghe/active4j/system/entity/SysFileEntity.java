package com.zhonghe.active4j.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhonghe.active4j.common.entity.BaseEntity;
import com.zhonghe.active4j.core.annotation.QueryField;
import com.zhonghe.active4j.core.model.QueryCondition;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 文件表
 * @author teli_
 *
 */
@TableName("sys_file")
@Getter
@Setter
public class SysFileEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7516587511068473316L;

	/**
	 * 文件名
	 */
	@TableField("FILENAME")
	private String fileName;
	
	/**
	 * 文件路径
	 */
	@TableField("FILEPATH")
	private String filePath;
	
	/**
	 * 关联Id
	 */
	@TableField("LINKID")
	@QueryField(condition=QueryCondition.eq, queryColumn="LINKID")
	private String linkId;

	/**
	 * 关联Id
	 */
	@TableField("FILESIZE")
	private String fileSize;
	
}
