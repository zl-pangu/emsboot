package com.zhph.mapper.sys;

import com.zhph.model.sys.SysWorkplaceset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */
public interface SysWorkplacesetMapper {
    public List<SysWorkplaceset> queryAllWorkplaceset(SysWorkplaceset params);
    public List<String> queryAllProvince(@Param("status") String param);
    void delById(@Param("id")Long id);
    void addByObj(SysWorkplaceset params);
    void updateById(SysWorkplaceset params);
}
