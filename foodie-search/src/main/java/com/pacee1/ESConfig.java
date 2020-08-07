package com.pacee1;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>解决ES启动Netty问题</p>
 *
 * @author : Pace
 * @date : 2020-08-07 15:47
 **/
@Configuration
public class ESConfig {
    /**
     * 解决netty引起的issue
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
