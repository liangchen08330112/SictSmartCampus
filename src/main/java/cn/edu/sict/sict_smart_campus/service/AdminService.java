package cn.edu.sict.sict_smart_campus.service;

import cn.edu.sict.sict_smart_campus.data.Admin;
import cn.edu.sict.sict_smart_campus.data.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm form);

    Admin getAdminById(Long userId);

    IPage<Admin> getAdminByOpr(Page<Admin> adminPage, String adminName);
}
