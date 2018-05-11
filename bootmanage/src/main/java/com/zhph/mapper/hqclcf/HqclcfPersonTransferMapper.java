package com.zhph.mapper.hqclcf;


import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Repository;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Repository
public interface HqclcfPersonTransferMapper {

    /**
     * 删除
     * @param prinumber
     * @return
     */
    int deleteByPrimaryKey(long prinumber);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(HqclcfPersonTransfer record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(HqclcfPersonTransfer record);

    /**
     * 通过主键 标识查询对象
     * @param priNumber
     * @return
     */
    HqclcfPersonTransfer selectByPrimaryKey(@Param("priNumber") Long priNumber);

    /**
     * 选择性更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(HqclcfPersonTransfer record);

    /**
     * 整体更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(HqclcfPersonTransfer record);

    /**
     * 条件获取所有对象信息
     * @param record
     * @return
     */
    List<HqclcfPersonTransfer> getPersonTransferByCondition(HqclcfPersonTransfer record);

    /**
     * 导出
     * @param hqclcfPersonTransfer
     * @param req
     * @param rep
     */
    void exportExl(HqclcfPersonTransfer hqclcfPersonTransfer, HttpRequest req, HttpServletResponse rep);

    /**
     * 条件获取对象信息
     * @param params
     * @return
     */
    List<HqclcfPersonTransfer> queryAllHqclcfPersonTransfer(HqclcfPersonTransfer params);
}