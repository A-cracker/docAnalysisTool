package com.wtb.javatool.config;

import com.fy.javanode.utils.ThreadVariable;
import com.fy.tre.system.SystemConstants;
import com.fy.wetoband.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Mybatis 配置
 *
 * Mybatis 和 SpringDataJPA 一样都是数据库持久层, 可根据需要选择两者之一进行使用, 或者两者共同使用
 */
@Configuration
@MapperScan("com.wtb.javatool.dao")
public class MybatisConfig {

    @Bean
    @Lazy
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        // DataSource
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        // 默认用注解 Mapper, 如要用 xml mapper 则去掉下面的注释
        Resource[] resources = getResources();
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.wtb.javatool");

        return sqlSessionFactoryBean;
    }

    private Resource[] getResources() throws IOException {
        if (SystemConstants.TOOL_JAR_PATH == null) {	//此字段为空则为本地开发
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            return resolver.getResources("classpath*:mappers/*Mapper.xml");
        }else {
            long startTime = System.currentTimeMillis();

            Long toolID = ThreadVariable.get(ThreadVariable.TOOL_ID_KEY);
            Long toolVersionId = ThreadVariable.get(ThreadVariable.TOOL_VERSION_ID_KEY);
            System.out.println("=======================================================");
            System.out.println(SystemConstants.TOOL_JAR_PATH + ":" + toolID + ":" + toolVersionId);
            System.out.println("=======================================================");
            File file = FileUtils.newAbsoluteFile(SystemConstants.TOOL_JAR_PATH, toolID, toolVersionId, toolVersionId + ".jar");

            try (JarFile jar = new JarFile(file)){
                ClassLoader loader = getClass().getClassLoader();

                return jar.stream()
                        .filter(e -> !e.isDirectory() && e.getName().endsWith("Mapper.xml"))
                        .map(e -> new InputStreamResource(loader.getResourceAsStream(e.getName()),e.getName()))
                        .toArray(Resource[]::new);
            }finally {
                long endTime = System.currentTimeMillis();
                System.out.println("解析mapper耗时 :"+(endTime-startTime)+" ms");
            }
        }
    }
}