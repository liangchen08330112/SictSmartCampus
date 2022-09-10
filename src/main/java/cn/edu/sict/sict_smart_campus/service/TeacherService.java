package cn.edu.sict.sict_smart_campus.service;

import cn.edu.sict.sict_smart_campus.dao.LoginForm;
import cn.edu.sict.sict_smart_campus.dao.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm form);

    Teacher getTeacherById(Long userId);
}
