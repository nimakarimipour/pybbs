package co.yiiu.pybbs.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Configuration
@MapperScan("co.yiiu.pybbs.mapper")
public class MybatisPlusConfig {

    // aaaaaa
    //  @Bean
    //  public PaginationInterceptor paginationInterceptor() {
    //    return new PaginationInterceptor();
    //  }

    @Bean("mybatisSqlSession")
    @DependsOn("flywayConfig")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        //*aaMap aaaaaa*
        configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());

        // aaaaa，aaaaaaaaaaaa，aaaaaa，aaa。。。
        sqlSessionFactory.setDataSource(dataSource);

        sqlSessionFactory.setConfiguration(configuration);

        // aaaaaa，aaaaa
        // aaaaaMap aaaaaaaaaaaa，aaaaaaaaaaaaaaa，aaaaaaaaa，aaaaaa
        // aaaa，aa，aaaaaaaa，aaaaaaaaaaa，MybatisSqlSessionFactoryBean，MybatisConfiguration
        // aaaaaaaaaaaaaaaa，aaaaaa Interceptor
        // aaaaaaaaaaaa，aaaMybatisSqlSessionFactoryBeanaaaaa，aapluginsaaa，aaaa Interceptor
        // aaaaaaaaaaaaa，aaaa，aaaa
        // aaaaaaa，aaaa！！！！！！
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setSqlParser(new JsqlParserCountOptimize());
        sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor});

        return sqlSessionFactory.getObject();
    }

  /*
  aaaaaaaaaaaaaa，aaaaaaaaaaaaaaa，aaaa，aaaaa！

  @Resource
  private MybatisPlusProperties mybatisPlusProperties;

  @Bean
  public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
  }

  @PostConstruct
  public void mybatisConfigration() {
    MybatisConfiguration configuration = mybatisPlusProperties.getConfiguration();
    // aaaaaaaa
    configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
  }
  */
}
