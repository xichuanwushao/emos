package com.xichuan.emos.config;

import cn.hutool.core.util.StrUtil;
import com.xichuan.emos.domain.SysConfig;
import com.xichuan.emos.mapper.SysConfigMapperCust;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
@EnableAsync
@Slf4j
@MapperScan("com.xichuan.emos.mapper")
@ComponentScan("com.xichuan")
@EnableSwagger2
@SpringBootApplication
public class EmosWxApiApplication {
    @Resource
    private SysConfigMapperCust sysConfigMapperCust;

    @Autowired
    private SystemConstants constants;
    public static void main(String[] args) {
        SpringApplication.run(EmosWxApiApplication.class, args);
    }

    @Value("${emos.image-folder}")
    private String imageFolder;

    @PostConstruct
    public void init(){
        List<SysConfig> list=sysConfigMapperCust.selectAllParam();
        list.forEach(one->{
            String key=one.getParamKey();
            key= StrUtil.toCamelCase(key);
            String value=one.getParamValue();
            try{
                Field field=constants.getClass().getDeclaredField(key);
                field.set(constants,value);
            }catch (Exception e){
                log.error("执行异常",e);
            }
        });
        //创建文件夹用来存放签到自拍照拍
        new File(imageFolder).mkdirs();
    }
}
