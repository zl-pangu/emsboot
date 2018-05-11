package com.zhph.service.hqclcf;

import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.vo.ResultVo;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

import java.util.List;

public interface HqclcfBusinessService {
    /**
     * 新增职务
     *
     * @param hqclcfBusiness
     * @return
     */
    public ResultVo insert(HqclcfBusiness hqclcfBusiness) throws Exception;

    /**
     * 删除职务
     *
     * @return
     */
    public ResultVo deleteByPrimaryKey(HqclcfBusiness hqclcfBusiness) throws Exception;

    /**
     * 条件分页查询职务
     *
     * @param pageBean
     * @param hqclcfBusiness
     * @return
     */
    public Grid<HqclcfBusiness> getBusinessByCondition(PageBean pageBean, HqclcfBusiness hqclcfBusiness) throws Exception;

    /**
     * 修改职务
     *
     * @param hqclcfBusiness
     * @return
     */
    public ResultVo updateByPrimaryKeySelective(HqclcfBusiness hqclcfBusiness) throws Exception;

    /**
     * 生成职位编码
     *
     * @return
     * @throws Exception
     */
    public String getRankNoIdByUUId() throws Exception;

    /**
     * 获取所有职级
     */
    public List<HqclcfRank> getRankList() throws Exception;

    /**
     * 通过职务编码获取职级code
     */
    public HqclcfBusiness queryBusinessByNo(String no) throws Exception;

    /**
     * 判断当前职务是否正在被使用
     * @return
     * @throws Exception
     */
    public ResultVo judgeStatus(String posCode,String value) throws Exception;

    /**
     * 判断当前职务名是否被使用
     * @return
     * @throws Exception
     */
    public ResultVo checkPosName(String posName,String posNo) throws Exception;


}
