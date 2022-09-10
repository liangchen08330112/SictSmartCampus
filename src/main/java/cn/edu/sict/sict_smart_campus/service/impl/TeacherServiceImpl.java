package cn.edu.sict.sict_smart_campus.service.impl;

import cn.edu.sict.sict_smart_campus.data.LoginForm;
import cn.edu.sict.sict_smart_campus.data.Teacher;
import cn.edu.sict.sict_smart_campus.mapper.TeacherMapper;
import cn.edu.sict.sict_smart_campus.service.TeacherService;
import cn.edu.sict.sict_smart_campus.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm form) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",form.getUsername());
        queryWrapper.eq("password", MD5.encrypt(form.getPassword()));
        Teacher teacher  = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage getTeacherByOpr(Page<Teacher> teacherPage, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getName())){
            queryWrapper.like("name",teacher.getName());
        }
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.like("clazz_name",teacher.getClazzName());
        }
        queryWrapper.orderByDesc("id");
        Page<Teacher> page = baseMapper.selectPage(teacherPage,queryWrapper);
        return page;
    }
}
