package cn.edu.sict.sict_smart_campus.controller;

import cn.edu.sict.sict_smart_campus.data.Admin;
import cn.edu.sict.sict_smart_campus.data.LoginForm;
import cn.edu.sict.sict_smart_campus.data.Student;
import cn.edu.sict.sict_smart_campus.data.Teacher;
import cn.edu.sict.sict_smart_campus.service.AdminService;
import cn.edu.sict.sict_smart_campus.service.StudentService;
import cn.edu.sict.sict_smart_campus.service.TeacherService;
import cn.edu.sict.sict_smart_campus.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    //修改密码
    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
        //判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            //为TRUE即过期
            return Result.fail().message("token已过期，请重新登录");
        }

        //获取用户的信息
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        //MD5加密
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);

                Admin admin = adminService.getOne(queryWrapper1);
                //如果原密码正确
                if(admin !=null){
                    //修改,将新密码设置后修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("旧密码输入错误");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                //如果原密码正确
                if(student !=null){
                    //修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("旧密码输入错误");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if(teacher !=null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("旧密码输入错误");
                }
                break;
        }
        return Result.ok();
    }
    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("上传头像") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ){
        //自动生成文件名前缀
        String uuid = UUID.randomUUID().toString().replace("-","").toLowerCase();
        //获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //从.开始截取后缀
        int i = originalFilename.lastIndexOf(".");
        ///组合新的文件名
        String newFileName = uuid.concat(originalFilename.substring(i));
        //保存文件  将文件发送到第三方/独立的图片服务器上
        String portraitPath = "D:\\SictSmartCampus\\target\\classes\\public\\upload\\".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //图片路径
        String path = "upload/"+newFileName;
        return Result.ok(path);
    }

    @ApiOperation("获取信息")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析用户的id和类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1: //管理员
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2: //学生
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3: //教师
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm form,HttpServletRequest hsr) {
        //验证码校验
        HttpSession hs = hsr.getSession();
        String sessionVerifiCode = (String) hs.getAttribute("verifiCode");
        String loginVerifiCode = form.getVerifiCode();

        if ("".equals(sessionVerifiCode)||null==sessionVerifiCode){
            return Result.fail().message("验证码已失效，请刷新");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("验证码有误，请检查后重新输入");
        }
        //从session域移除现有验证码
        hs.removeAttribute("verifiCode");
        //分用户类型检验

        //准备一个linkedhashmap存放响应数据
        Map<String,Object> map = new LinkedHashMap<>();
        switch (form.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(form);
                    if (null!=admin){
                        //用户的类型和id转换成一个密文,以token的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(),1);
                        map.put("token",token);
                    }else {
                        //手动抛异常
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(form);
                    if (null!=student){
                        //用户的类型和id转换成一个密文,以token的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(),2);
                        map.put("token",token);
                    }else {
                        //手动抛异常
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(form);
                    if (null!=teacher){
                        //用户的类型和id转换成一个密文,以token的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(),3);
                        map.put("token",token);
                    }else {
                        //手动抛异常
                        throw new RuntimeException("用户名或密码错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("用户不存在");
    }
    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiedCodeImage(HttpServletRequest hsr, HttpServletResponse response){
        //获取图片
        BufferedImage bufferedImage = CreateVerificationCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerificationCodeImage.getVerifiCode());
        //将验证码文本放入session域
        HttpSession httpSession = hsr.getSession();
        httpSession.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        try {
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(bufferedImage,"JPEG",sos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
