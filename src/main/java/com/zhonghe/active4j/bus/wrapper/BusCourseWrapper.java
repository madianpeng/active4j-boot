package com.zhonghe.active4j.bus.wrapper;

import com.zhonghe.active4j.bus.entity.BusCourseEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhonghe.active4j.common.Wrapper.BaseWrapper;

public class BusCourseWrapper extends BaseWrapper<BusCourseEntity> {
	public BusCourseWrapper(IPage<BusCourseEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
	}
	
	
	
}
