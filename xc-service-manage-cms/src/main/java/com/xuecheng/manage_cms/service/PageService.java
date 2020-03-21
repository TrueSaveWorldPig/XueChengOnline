package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

import java.io.IOException;
import java.util.List;

public interface PageService {
    /**
     * 页面查询方法
     *
     * @param page             页码从1开始记录
     * @param size             每页记录数
     * @param queryPageRequest
     * @return
     */
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 查询所有站点列表
     *
     * @return
     */
    List<CmsSite> findAll();

    /**
     * 查询模板列表
     *
     * @return
     */
    List<CmsTemplate> findTemplateList();

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据ID查询单个页面信息
     *
     * @param id
     * @return
     */
    CmsPage getById(String id);

    /**
     * 修改页面信息
     *
     * @param id
     * @param cmsPage
     * @return
     */
    CmsPageResult update(String id, CmsPage cmsPage);

    /**
     * 根据页面ID删除页面信息
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 根据Id查询CmsConfig
     *
     * @param id
     * @return CmsConfig
     */
    CmsConfig getConfigById(String id);

    /**
     *  页面静态化方法
     * @param pageId
     * @return
     */
    String getPageHtml(String pageId) throws IOException;
}
