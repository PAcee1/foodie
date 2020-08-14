package com.pacee1.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>文件相关配置</p>
 *
 * @author : Pace
 * @date : 2020-08-14 16:57
 **/
@Component
@PropertySource("classpath:file.properties")
@ConfigurationProperties(prefix = "file")
public class FileResource {

    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
