package com.pacee1.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * <p>Stu实体</p>
 *
 * @author : Pace
 * @date : 2020-08-07 16:56
 **/
@Document(indexName = "stu",type = "_doc")
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
