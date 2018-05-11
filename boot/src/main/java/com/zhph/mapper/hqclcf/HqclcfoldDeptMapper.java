package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.hqclcf.HqclcfoldDept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HqclcfoldDeptMapper {
    List<HqclcfoldDept> queryAll() throws Exception;

    HqclcfoldDept queryById(Long id) throws Exception;
}
