package org.fordes.quartz.example;

import cn.hutool.core.net.NetUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;

/**
 * @author fordes
 */
@SpringBootApplication
public class SpringBootQuartzExampleApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootQuartzExampleApplication.class);

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(SpringBootQuartzExampleApplication.class, args).getEnvironment();
        logger.info("{} 启动成功！", env.getProperty("spring.application.name"));
        logger.info("文档地址: http://{}:{}/doc.html", NetUtil.getLocalhostStr(), env.getProperty("server.port"));
    }

    /**
     * 配置Quartz独立数据源的配置
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.quartz")
    public DataSource quartzDataSource(){
        return new DruidDataSource();
    }

    /**
     * 主数据源
     * @return
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.business")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }
}
