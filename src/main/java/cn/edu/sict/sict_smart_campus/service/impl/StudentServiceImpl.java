package cn.edu.sict.sict_smart_campus.service.impl;

import cn.edu.sict.sict_smart_campus.data.LoginForm;
import cn.edu.sict.sict_smart_campus.data.Student;
import cn.edu.sict.sict_smart_campus.mapper.StudentMapper;
import cn.edu.sict.sict_smart_campus.service.StudentService;
import cn.edu.sict.sict_smart_campus.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm form) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",form.getUsername());
        queryWrapper.eq("password", MD5.encrypt(form.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<Student>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getName())){
            queryWrapper.like("name",student.getName());
        }
        if (!StringUtils.isEmpty(student.getClazzName())){
            queryWrapper.like("clazz_name",student.getClazzName());
        }
        queryWrapper.orderByDesc("id");
        Page<Student> studentPage = baseMapper.selectPage(page,queryWrapper);
        return studentPage;
    }
}
