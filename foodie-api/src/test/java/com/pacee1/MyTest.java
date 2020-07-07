package com.pacee1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author pace
 * @version v1.0
 * @Type MyTest.java
 * @Desc
 * @date 2020/5/17 14:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("name","pace");

        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);

        redisTemplate.delete("name");
    }
}
