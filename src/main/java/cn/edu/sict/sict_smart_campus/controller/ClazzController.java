package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Clazz;
import cn.edu.sict.sict_smart_campus.service.ClazzService;
import cn.edu.sict.sict_smart_campus.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;
    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzes= clazzService.getClazzs();
        return Result.ok(clazzes);
    }


    @ApiOperation("查询班级信息，分页")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页模糊查询的班级名称") Clazz clazzName
    ){
        //分页带条件查询
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        //通过service层
        IPage<Clazz> clazzIPage = clazzService.getClazzsByOpr(page,clazzName);

        return Result.ok(clazzIPage);
    }

    @ApiOperation("新增和修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON的clazz对象，有id属性的为修改，否则为新增")
            @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("要删除的多个班级的ID的JSON数组")
            @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }
}
