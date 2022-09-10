package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Student;
import cn.edu.sict.sict_smart_campus.service.StudentService;
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
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("添加或修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("要添加或修改的学生的json") @RequestBody Student student)
    {
        //是否需要将密码转为密文？
        Integer stuId = student.getId();
        if (null==stuId||0==stuId){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询的条件") Student student)
    {
        //分页信息
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> stuPage = studentService.getStudentByOpr(page,student);
        //返回result
        return Result.ok(stuPage);
    }

    @ApiOperation("删除学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(@ApiParam("要删除的学生编号的json数组") @RequestBody List<Integer> id){
        studentService.removeByIds(id);
        return Result.ok();
    }
}
