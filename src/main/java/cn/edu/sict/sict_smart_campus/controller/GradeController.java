package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Grade;
import cn.edu.sict.sict_smart_campus.service.GradeService;
import cn.edu.sict.sict_smart_campus.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {
    @Autowired
    private GradeService gradeService;
    //新增和修改
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@RequestBody Grade grade){
        //接收参数
        gradeService.saveOrUpdate(grade);
        //调用服务方法实现添加和修改
        return Result.ok();
    }
    //查询
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrade(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            String gradeName) {
        //分页带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        //通过service层查询
        IPage<Grade> pageRes = gradeService.getGradeByOpr(page,gradeName);

        //返回查询结果
        return Result.ok(pageRes);
    }
    //删除
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }
    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> gradeList = gradeService.getGrades();
        return Result.ok(gradeList);
    }
}
