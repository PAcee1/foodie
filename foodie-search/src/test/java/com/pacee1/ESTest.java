package com.pacee1;

import com.pacee1.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Test
    public void searchPage(){
        // 分页条件
        Pageable pageable = PageRequest.of(0, 5);

        // 搜索查询条件
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name","pacee1"))
                .withPageable(pageable)
                .build();

        // 搜索
        AggregatedPage<Stu> stus = esTemplate.queryForPage(searchQuery, Stu.class);
        List<Stu> stuList = stus.getContent();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }

    @Test
    public void searchPageHighlight(){
        // 分页条件
        Pageable pageable = PageRequest.of(0, 5);
        // 高亮
        HighlightBuilder.Field name = new HighlightBuilder.Field("name")
                .preTags("<em style='color:red'>")
                .postTags("</em>")
                .fragmentSize(100);
        // 排序
        SortBuilder sortBuilder = new FieldSortBuilder("age")
                .order(SortOrder.DESC);

        // 搜索查询条件
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name","pacee1"))
                .withHighlightFields(name) // 高亮
                .withPageable(pageable) // 分页
                .withSort(sortBuilder) // 排序
                .build();

        // 搜索
        AggregatedPage<Stu> stus = esTemplate.queryForPage(searchQuery, Stu.class,new SearchResultMapper(){

            // 组装结果数据
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<T> result = new ArrayList<>();
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    Map<String, HighlightField> hmap = hit.getHighlightFields();
                    Map<String, Object> smap = hit.getSourceAsMap();
                    result.add((T)createEsDoc(smap,hmap));
                }

                AggregatedPage<T> aggregatedPage = new AggregatedPageImpl<T>(result,pageable,
                        searchResponse.getHits().getTotalHits());

                return aggregatedPage;
            }
        });

        List<Stu> stuList = stus.getContent();
        for (Stu stu : stuList) {
            System.out.println(stu);
        }
    }

    private Stu createEsDoc(Map<String, Object> smap,Map<String, HighlightField> hmap){
        Stu stu = new Stu();
        if (smap.get("stuId") != null)
            stu.setStuId(Long.parseLong(smap.get("stuId").toString()));
        if (hmap.get("name") != null)
            stu.setName(hmap.get("name").fragments()[0].toString());
        if(smap.get("age") != null)
            stu.setAge((Integer) smap.get("age"));
        return stu;
    }

}
