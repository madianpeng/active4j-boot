package com.zhonghe.active4j;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * 入口启动类
 * @author teli_
 *
 */
@SpringBootApplication
@MapperScan(value = "com.zhonghe.active4j.system.dao")
public class Active4jbootApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(Active4jbootApplication.class, args);
	}

}

