package cn.edu.sict.sict_smart_campus.service;

import cn.edu.sict.sict_smart_campus.data.LoginForm;
import cn.edu.sict.sict_smart_campus.data.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm form);

    Teacher getTeacherById(Long userId);

    IPage getTeacherByOpr(Page<Teacher> teacherPage, Teacher teacher);
}
