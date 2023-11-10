package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddTagDto;
import org.example.domain.dto.EditTagDto;
import org.example.domain.dto.TagListDto;

import org.example.domain.entity.Tag;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.TagVo;
import org.example.service.TagService;
import org.example.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签",description = "标签相关接口")
public class TagController {
    @Autowired
    private TagService tagService;


    /**
     * 查询标签列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param tagListDto 标签名+备注
     * @return
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    /**
     * 新增Tag
     * @param tagdto
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增Tag",notes = "新增Tag")
    public ResponseResult add(@RequestBody AddTagDto tagdto){
        Tag tag = BeanCopyUtils.copyBean(tagdto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 批量删除Tag
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除Tag",notes = "删除Tag")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "待删除id"),
    })
    public ResponseResult delete(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }




    /**
     * 修改Tag
     * @param tagDto
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改Tag",notes = "修改Tag")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "待删除id"),
    })
    public ResponseResult edit(@RequestBody EditTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 给修改tag方法提供的根据tagId查询方法
     * @param id 标签id
     * @return 标签
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询Tag",notes = "根据tagId查询tag")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    /**
     * 查询所有Tag标签
     * @return
     */
    @GetMapping("/listAllTag")
    @ApiOperation(value = "查询所有标签",notes = "")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }



}
