package com.xuecheng.manage_cms.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
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
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.PageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
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

    @Override
    public List<CmsSite> findAll() {
        return cmsSiteRepository.findAll();
    }

    @Override
    public List<CmsTemplate> findTemplateList() {
        return cmsTemplateRepository.findAll();
    }

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

    @Override
    public CmsPage getById(String id) {
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById(id);
        if (optionalCmsPage.isPresent()) {
            CmsPage cmsPage = optionalCmsPage.get();
            return cmsPage;
        }
        return null;
    }

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
            //更新dataUrl
            cmsPage1.setDataUrl(cmsPage.getDataUrl());

            //提交修改
            cmsPageRepository.save(cmsPage1);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

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

    @Override
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public String getPageHtml(String pageId) {
        // 获取数据模型
        Map model = getModelByPageId(pageId);
        if (null == model) {
            // 数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面的模板
        String template = getTemplateByPageId(pageId);
        if (null == template) {
            // 数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 执行静态化
        String html = generateHtml(model, template);
        if (Objects.isNull(html)) {
            // 静态化失败
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        return html;
    }

    private String generateHtml(Map model, String template) {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", template);
        // 向configuration中配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            // 获取模板
            Template template1 = configuration.getTemplate("template");
            // 调用API进行静态化
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTemplateByPageId(String pageId) {
        // 取出页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (Objects.isNull(cmsPage)) {
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 获取页面模板ID
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            // 页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            // 获取模板文件Id
            String templateFileId = cmsTemplate.getTemplateFileId();
            // 从GridFSFile中获取模板文件的内容
            // 根据文件Id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // 打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFSBResource对象,获取流
            GridFsResource fsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            //从流中获取数据
            try {
                String content = IOUtils.toString(fsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Map getModelByPageId(String pageId) {
        // 取出页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (Objects.isNull(cmsPage)) {
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        // 取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // 页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }


}
