package com.pacee1;

import com.pacee1.pojo.Stu;
import io.swagger.models.auth.In;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>test</p>
 *
 * @author : Pace
 * @date : 2020-08-07 16:55
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Test
    public void createIndex(){
        Stu stu = new Stu();
        stu.setStuId(1001L);
        stu.setName("pace");
        stu.setAge(20);

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }

    @Test
    public void deleteIndex(){
        esTemplate.deleteIndex(Stu.class);
    }

    @Test
    public void updateDoc(){
        Map<String,Object> sourceMap = new HashMap<>();
        sourceMap.put("name","pacee1");
        sourceMap.put("age",23);

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(sourceMap);

        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1001")
                .withIndexRequest(indexRequest)
                .build();
        esTemplate.update(updateQuery);
    }

    @Test
    public void getDoc(){
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1001");
        Stu stu = esTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }

    @Test
    public void deleteDoc(){
       esTemplate.delete(Stu.class,"1001");
    }
}
