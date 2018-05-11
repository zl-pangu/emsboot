package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HqclcfPostMapper {

    /**
     * 条件查询所有岗位记录
     * @param hqclcfPost
     * @return
     * @throws Exception
     */
    List<HqclcfPost> queryAll(HqclcfPost hqclcfPost) throws Exception;

    /**
     * 新增岗位
     * @param hqclcfPost
     * @throws Exception
     */
    void addPost(HqclcfPost hqclcfPost)throws Exception;

    /**
     * 编辑岗位
     * @param hqclcfPost
     * @throws Exception
     */
    void editPost(HqclcfPost hqclcfPost)throws Exception;

    /**
     * 删除岗位
     * @param id
     * @throws Exception
     */
    void delById(Long id) throws Exception;

    /**
     * 通过岗位编码获取单条岗位记录
     * @param postNo
     * @return
     * @throws Exception
     */
    HqclcfPost queryByPostNo(String postNo) throws Exception;

    /**
     * 通过岗位id查询上级岗位是否存在禁用状态的岗位
     * @param id
     * @return
     * @throws Exception
     */
    List<HqclcfPost> queryUpperPidById(@Param("pri_number") Long id) throws Exception;

    /**
     * 通过岗位id查询下级是否存在启用状态的岗位
     * @param id
     * @return
     * @throws Exception
     */
    List<HqclcfPost> queryLowerPidById(@Param("post_pid") Long id) throws Exception;

    /**
     * 获取当前传入postId的所有下级岗位信息
     * @param id
     * @return
     * @throws Exception
     */
    List<HqclcfPost> queryLowerPost(@Param("id") Long id) throws Exception;

    /**
     * 根据岗位ID查询岗位
     * @param postId
     * @return
     * @throws Exception
     */
    HqclcfPost queryByPostId(@Param("postId") Long postId,@Param("status")String status) throws Exception;
}
