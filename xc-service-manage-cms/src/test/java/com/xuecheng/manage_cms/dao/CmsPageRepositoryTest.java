package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> cmsPageList = cmsPageRepository.findAll();
        for (CmsPage cmsPage : cmsPageList) {
            System.out.println(cmsPage);
        }
    }

    //分页查询
    @Test
    public void testFindPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CmsPage> pages = cmsPageRepository.findAll(pageable);
        System.out.println(pages);
    }

    //插入
    @Test
    public void testInsert() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("此为测试页面");
        cmsPage.setPageAliase("test005");
        cmsPage.setPageCreateTime(new Date());
        CmsPage page = cmsPageRepository.insert(cmsPage);
        System.out.println(page);
    }

    //修改
    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5db2e220e371863d14262886");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("====此为测试页面====");
            cmsPage.setPageCreateTime(new Date());
            CmsPage page = cmsPageRepository.save(cmsPage);
            System.out.println(page);
        }
    }

    //修改
    @Test
    public void test() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("此为测试页面");
        System.out.println(cmsPage);
    }

    /**
     * 条件查询
     */
    @Test
    public void testSelectByCondition() {
        Pageable pageable = PageRequest.of(0, 10);

        //条件添加
        CmsPage cmsPage = new CmsPage();
//        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
//        cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
        cmsPage.setPageAliase("播");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CmsPage> example = Example.of(cmsPage, matcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> cmsPages = all.getContent();
        int i = 0;
        for (CmsPage page : cmsPages) {
            i++;
            System.out.println(page);
        }
        System.out.println(i);
    }

}
