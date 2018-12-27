import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-solr.xml"})
public class SpringdataSolrDemo {
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 添加  修改
     * @throws Exception
     */
    @Test
    public void TestAdd()throws Exception{
        Item item=new Item();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));
        //添加
        solrTemplate.saveBean(item,1000);
    }

    @Test
    public void TestFindById()throws Exception{
        //查询
        Item item = solrTemplate.getById("1", Item.class);
        System.out.println(item);
    }

    /**
     * 根据主键删除
     * @throws Exception
     */
    @Test
    public void TestDeleteById()throws Exception{
        //删除
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }


    /**
     * 批量添加
     * @throws Exception
     */
    @Test
    public void TestAddList()throws Exception{
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Item item=new Item();
            item.setId(i+1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate"+i);
            item.setPrice(new BigDecimal(2000+i));
            items.add(item);
        }
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    /**
     * 分页查询
     * @throws Exception
     */
    @Test
    public void TestFindByPage()throws Exception {
        Query query = new SimpleQuery("*:*");
        //设置开始行
        query.setOffset(3);
        //设置没页数
        query.setRows(3);

        //执行查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);
        //总条数
        long totalElements = page.getTotalElements();
        //总页数
        int totalPages = page.getTotalPages();
        System.out.println("总条数:"+totalElements);
        System.out.println("总页数:"+totalPages);
        //结果集
        List<Item> list = page.getContent();
        for (Item item : list) {
            System.out.println("id"+item.getId());
            System.out.println("title"+item.getTitle());
        }
    }


    /**
     * 条件查询
     * @throws Exception
     */
    @Test
    public void TestFindByPageQuery()throws Exception {
        Criteria criteria = new Criteria("item_title").contains("1");
        Query query = new SimpleQuery(criteria);
        //设置开始行
        query.setOffset(3);
        //设置没页数
        query.setRows(3);

        //执行查询
        ScoredPage<Item> page = solrTemplate.queryForPage(query, Item.class);
        //总条数
        long totalElements = page.getTotalElements();
        //总页数
        int totalPages = page.getTotalPages();
        System.out.println("总条数:"+totalElements);
        System.out.println("总页数:"+totalPages);
        //结果集
        List<Item> list = page.getContent();
        for (Item item : list) {
            System.out.println("id"+item.getId());
            System.out.println("title"+item.getTitle());
        }
    }

    @Test
    public void TestDelete()throws Exception {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
