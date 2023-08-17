package co.yiiu.pybbs.config;

import edu.ucr.cs.riple.taint.ucrtainting.qual.RUntainted;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Configuration
@ConfigurationProperties(value = "site")
public class SiteConfig implements Serializable {

    private static final long serialVersionUID = -7632268193700036274L;

    private String datasource_driver;
    private String datasource_url;
    private @RUntainted String datasource_username;
    private @RUntainted String datasource_password;

    public String getDatasource_driver() {
        return datasource_driver;
    }

    public void setDatasource_driver(String datasource_driver) {
        this.datasource_driver = datasource_driver;
    }

    public String getDatasource_url() {
        return datasource_url;
    }

    public void setDatasource_url(String datasource_url) {
        this.datasource_url = datasource_url;
    }

    public @RUntainted String getDatasource_username() {
        return datasource_username;
    }

    public void setDatasource_username(@RUntainted String datasource_username) {
        this.datasource_username = datasource_username;
    }

    public @RUntainted String getDatasource_password() {
        return datasource_password;
    }

    public void setDatasource_password(@RUntainted String datasource_password) {
        this.datasource_password = datasource_password;
    }
}
