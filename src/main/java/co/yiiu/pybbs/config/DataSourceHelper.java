package co.yiiu.pybbs.config;

import co.yiiu.pybbs.util.SpringContextUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Configuration
public class DataSourceHelper {

    @Resource
    private SiteConfig siteConfig;

    @PostConstruct
    public void init() {
        if (siteConfig == null) siteConfig = SpringContextUtil.getBean(SiteConfig.class);
        try {
            Class.forName(siteConfig.getDatasource_driver());
            @RUntainted URI uri = new URI(siteConfig.getDatasource_url().replace("jdbc:", ""));
            @RUntainted String host = uri.getHost();
            @RUntainted int port = uri.getPort();
            String path = uri.getPath();
            @RUntainted String query = uri.getQuery();
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "?" + query,
                    siteConfig.getDatasource_username(), siteConfig.getDatasource_password());
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + path.replace("/", "") + "` DEFAULT CHARACTER SET = "
                    + "" + "`utf8` COLLATE `utf8_general_ci`;");
            statement.close();
            connection.close();
        } catch (URISyntaxException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
