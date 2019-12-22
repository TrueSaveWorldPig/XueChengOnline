package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

import java.util.List;

public interface PageService {

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 查询页面站点信息
     *
     * @return
     */
    public abstract List<CmsSite> findAll();

    /**
     * 查询模板列表信息
     *
     * @return
     */
    public abstract List<CmsTemplate> findTemplateList();

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    public abstract CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据页面Id查询页面信息
     *
     * @param id
     * @return
     */
    public abstract CmsPage getById(String id);

    /**
     * 修改页面
     *
     * @param id
     * @param cmsPage
     * @return
     */
    public abstract CmsPageResult update(String id, CmsPage cmsPage);

    /**
     * 根据页面Id删除页面信息
     *
     * @param id
     * @return
     */
    public abstract ResponseResult delete(String id);
}
