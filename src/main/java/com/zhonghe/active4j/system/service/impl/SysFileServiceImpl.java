package com.zhonghe.active4j.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhonghe.active4j.system.dao.SysFileDao;
import com.zhonghe.active4j.system.dao.SysLogDao;
import com.zhonghe.active4j.system.entity.SysFileEntity;
import com.zhonghe.active4j.system.entity.SysLogEntity;
import com.zhonghe.active4j.system.service.SysFileService;
import com.zhonghe.active4j.system.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件service类
 * @author 38943
 *
 */
@Service("sysFileService")
@Transactional
public class SysFileServiceImpl extends ServiceImpl<SysFileDao, SysFileEntity> implements SysFileService {
	
}
