package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Teacher;
import cn.edu.sict.sict_smart_campus.service.TeacherService;
import cn.edu.sict.sict_smart_campus.util.MD5;
import cn.edu.sict.sict_smart_campus.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    @ApiOperation("分页带条件获取教师信息")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            Teacher teacher
    )
    {
        Page<Teacher> teacherPage = new Page<>(pageNo,pageSize);
        IPage iPage = teacherService.getTeacherByOpr(teacherPage,teacher);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("保存或修改的json格式的教师对象") @RequestBody Teacher teacher)
    {
        Integer id = teacher.getId();
        if (id==null||0==id){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除老师的信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("要删除的教师的JSON集合")
            @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
