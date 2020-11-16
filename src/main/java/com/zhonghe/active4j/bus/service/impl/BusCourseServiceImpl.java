package com.zhonghe.active4j.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhonghe.active4j.bus.dao.BusCourseDao;
import com.zhonghe.active4j.bus.entity.BusCourseEntity;
import com.zhonghe.active4j.bus.service.BusCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件service类
 * @author 38943
 *
 */
@Service("busCourseService")
@Transactional
public class BusCourseServiceImpl extends ServiceImpl<BusCourseDao, BusCourseEntity> implements BusCourseService {
	
}
