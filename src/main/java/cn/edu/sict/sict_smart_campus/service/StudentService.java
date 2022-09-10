package cn.edu.sict.sict_smart_campus.service;

import cn.edu.sict.sict_smart_campus.data.LoginForm;
import cn.edu.sict.sict_smart_campus.data.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    Student login(LoginForm form);

    Student getStudentById(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
