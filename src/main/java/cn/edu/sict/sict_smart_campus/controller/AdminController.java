package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Admin;
import cn.edu.sict.sict_smart_campus.service.AdminService;
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
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("管理员名字") String adminName)
    {
        Page<Admin> adminPage = new Page<Admin>(pageNo,pageSize);
        IPage<Admin> iPage = adminService.getAdminByOpr(adminPage,adminName);
        return Result.ok(iPage);
    }

    @ApiOperation("添加或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("json格式的admin对象") @RequestBody Admin admin)
    {
        Integer id = admin.getId();
        if (id==null||0==id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }
    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("要删除的管理员的多个id的json集合") @RequestBody List<Integer> ids
    )
    {
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
