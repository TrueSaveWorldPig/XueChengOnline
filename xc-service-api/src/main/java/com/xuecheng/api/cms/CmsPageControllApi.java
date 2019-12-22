package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllApi {
    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页 码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    //站点列表查询
    @ApiOperation("站点列表")
    @ApiImplicitParams({})
    public List<CmsSite> findSiteList();

    //模板列表查询
    @ApiOperation("模板列表")
    @ApiImplicitParams({})
    public List<CmsTemplate> findTemplateList();

    //新增页面
    @ApiOperation("新增页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPage", value = "新增的页面信息", required = true)
    })
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("根据页面ID查询页面")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "页面ID", required = true)
//    })
    public CmsPage findById(String id);

    @ApiOperation("修改页面")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "页面ID", required = true, paramType = "path", dataType = "String"),
//            @ApiImplicitParam(name = "cmsPage", value = "修改的页面信息", required = true, paramType = "path", dataType = "String")
//    })
    public CmsPageResult edit(String id, CmsPage cmsPage);

    /**
     * 根据ID删除页面
     *
     * @param id
     * @return
     */
    @ApiOperation("根据Id删除页面")
    public ResponseResult delete(String id);


}
