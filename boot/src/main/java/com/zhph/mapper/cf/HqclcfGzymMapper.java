package com.zhph.mapper.cf;

import com.zhph.model.hqclcf.HqclcfGzym;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create By lishuangjiang
 */
@Repository
public interface HqclcfGzymMapper {

    /**
     * 查询工资年月
     * @return
     * @throws Exception
     */
    public List<HqclcfGzym> queryXjGzym() throws Exception;

    /**
     * 查询当前工资年月
     * @return
     * @throws Exception
     */
    public HqclcfGzym queryCurrGzym() throws Exception;

    /**
     * 定时更新工资年月
     * @return
     * @throws Exception
     */
    public int updateGzym() throws Exception;

    /**
     * 根据主键删除
     * @param priNumber
     * @return
     */
	public int delete(Long priNumber);
}
