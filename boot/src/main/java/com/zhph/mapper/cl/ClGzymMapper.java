package com.zhph.mapper.cl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zhph.model.cl.ClGzym;

/**
 *  Create By sunweiyu
 *
 */
@Repository
public interface ClGzymMapper {
	/**
     * 查询工资年月
     * @return
     * @throws Exception
     */
    public List<ClGzym> queryClGzym() throws Exception;

    /**
     * 查询当前工资年月
     * @return
     * @throws Exception
     */
    public ClGzym queryCurrGzym() throws Exception;

    /**
     * 定时更新工资年月
     * @return
     * @throws Exception
     */
    public int updateGzym() throws Exception;
    

}
