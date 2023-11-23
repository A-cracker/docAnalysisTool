package com.wtb.javatool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringDataJPA 配置
 *
 * SpringDataJPA 和 Mybatis 一样都是数据库持久层, 可根据需要选择两者之一进行使用, 或者两者共同使用;
 *
 * 最好不使用SpringDataJpa, 因为启用 SpringDataJpa 的工具容器初始化需要5秒, 而 Mybatis只需要1秒.
 */

/*@Configuration
@EnableJpaRepositories(basePackages = "com.wtb.javatool.dao") //dao所在的包名
@EnableTransactionManagement*/
public class SpringDataJpaConfig {


    /**
     * @param dataSource IDEA 等会提示找不到该类型 bean, 启动时已经手动添加Spring容器, 忽略提示即可.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");

        Map<String, Object> properties = new HashMap<>();
        //此配置属性不要删除, 否则会在spring 容器初始化时请求核心获取数据库设施, 拖慢启动进度.
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", Boolean.FALSE);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(properties);
        factory.setPackagesToScan("com.wtb.javatool.vo"); //数据库表实体所在的包名
        factory.setDataSource(dataSource);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
