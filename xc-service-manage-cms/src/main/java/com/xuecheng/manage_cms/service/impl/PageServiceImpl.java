package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    //页面查询

    /**
     * 页面查询方法
     *
     * @param page             页码从1开始记录
     * @param size             每页记录数
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义查询条件
        //定义条件匹配器
        CmsPage cmsPage = new CmsPage();
        //封装条件对象,用页面别名作为条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //封装条件对象,用页面站点ID作为条件
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        ExampleMatcher matcher = ExampleMatcher.matching();
        //封装条件对象,用页面别名作为条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
            matcher = matcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        }

        if (page < 1) {
            page = 1;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, size);
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);


        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getContent());//数据列表
        queryResult.setTotal(pages.getTotalElements());//总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 查询所有站点列表
     *
     * @return
     */
    @Override
    public List<CmsSite> findAll() {
        return cmsSiteRepository.findAll();
    }

    /**
     * 查询模板列表
     *
     * @return
     */
    @Override
    public List<CmsTemplate> findTemplateList() {
        return cmsTemplateRepository.findAll();
    }

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        //校验页面名称,站点Id,页面webPath的唯一性
        //根据页面名称,站点Id,页面webPath去查询,如果查询到了,就说明此页面已经存在,如果查询不到,就继续添加;
        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            //页面已经存在,抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }


        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        //返回结果
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;

    }

    /**
     * 根据ID查询单个页面信息
     *
     * @param id
     * @return
     */
    @Override
    public CmsPage getById(String id) {
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(id);
        if (optionalCmsPage.isPresent()) {
            CmsPage cmsPage = optionalCmsPage.get();
            return cmsPage;
        }
        return null;
    }

    /**
     * 修改页面信息
     *
     * @param id
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //新更具ID查询出来页面信息
        CmsPage cmsPage1 = getById(id);
        if (cmsPage1 != null) {
            //准备更新页面信息
            //更新模板id
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cmsPage1.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPage1.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());

            //提交修改
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 根据页面ID删除页面信息
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delete(String id) {
        //删除之前新查询一下
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(id);
        if (optionalCmsPage.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }


}
