package com.zhph.mapper.cl;

import com.zhph.model.cl.ClAshBlackMenuModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by zhaoqixiang
 * Description:
 * 2018/1/17
 **/
public interface ClAshBlackMenuMapper {

    List<ClAshBlackMenuModel> queryAllAhsBlack(@Param("item") ClAshBlackMenuModel item);

    void importAhsBlackData(List<ClAshBlackMenuModel> list);
}
