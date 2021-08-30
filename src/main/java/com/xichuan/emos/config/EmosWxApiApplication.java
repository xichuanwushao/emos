package com.xichuan.emos.config;

import cn.hutool.core.util.StrUtil;
import com.xichuan.emos.domain.SysConfig;
import com.xichuan.emos.mapper.SysConfigMapperCust;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
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

    }
}
