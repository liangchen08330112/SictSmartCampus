package cn.edu.sict.sict_smart_campus.service;

import cn.edu.sict.sict_smart_campus.dao.Admin;
import cn.edu.sict.sict_smart_campus.dao.LoginForm;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm form);

    Admin getAdminById(Long userId);
}
