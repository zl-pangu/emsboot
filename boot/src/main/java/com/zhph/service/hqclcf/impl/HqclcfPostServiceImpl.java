package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.vo.ZtreeVo;
import com.zhph.service.hqclcf.HqclcfPostService;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.Json;
import com.zhph.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfPostServiceImpl implements HqclcfPostService {

    @Resource
    private HqclcfPostMapper hqclcfPostMapper;
    @Autowired
    private SysConfigTypeMapper sysConfigTypeMapper;

    @Resource
    private HqclcfDeptMapper hqclcfDeptMapper;

    @Resource
    private HqclcfBusinessMapper hqclcfBusinessMapper;

    @Resource
    private HqclcfEmpMapper hqclcfEmpMapper;

    @Override
    public JSONArray buildPostTree(Long id) throws Exception {
        JSONArray jsonArray = new JSONArray();
        HqclcfPost hqclcfPost = new HqclcfPost();
        boolean flag = true;
        if (id != null && id > 0) {
            flag = false;
        }
        List<HqclcfPost> posts = hqclcfPostMapper.queryAll(hqclcfPost);
        for (HqclcfPost post : posts) {
            ZtreeVo tree = new ZtreeVo();
            tree.setOpen(flag);
            tree.setId(post.getPriNumber());
            tree.setPid(post.getPostPid());
            tree.setName(post.getPostName());
            jsonArray.add(tree);
        }
        return jsonArray;
    }

    @Override
    public JSONArray buildDeptTree() throws Exception {
        JSONArray jsonArray = new JSONArray();
        HqclcfDept hqclcfDept = new HqclcfDept();
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(hqclcfDept);
        for (int i = 0; i < depts.size(); i++) {
            if (depts.get(i).getStatus().equals("1")) {
                ZtreeVo tree = new ZtreeVo();
                tree.setPid(depts.get(i).getPid());
                tree.setId(depts.get(i).getId());
                tree.setName(depts.get(i).getDeptName());
                jsonArray.add(tree);
                continue;
            }
        }
        return jsonArray;
    }

    @Override
    public HqclcfDept getBusinessLine(Long id) throws Exception {
        HqclcfDept hqclcfDept = new HqclcfDept();
        hqclcfDept.setId(id);
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(hqclcfDept);
        if (depts.size() > 0) {
            return depts.get(0);
        }
        return new HqclcfDept();
    }

    @Override
    public List<HqclcfBusiness> queryAllBusiness() throws Exception {
        List<HqclcfBusiness> businesses = hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness());
        /**
         * 去除已被禁用的职务
         */
        for (int i = 0; i < businesses.size(); i++) {
            if (businesses.get(i).getStatus().equals("0")) {
                businesses.remove(i);
                continue;
            }
        }
        return businesses;
    }

    @Override
    public List<HqclcfDept> queryAllDepts() throws Exception {
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(new HqclcfDept());
        /**
         * 去除已被禁用的部门
         */
        for (int i = 0; i < depts.size(); i++) {
            if (depts.get(i).getStatus().equals("0")) {
                depts.remove(i);
            }
        }
        return depts;
    }

    @Override
    public JSONObject judgeStatus(String code, int value) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfPost post = hqclcfPostMapper.queryByPostNo(code);

        List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("", "", new ArrayList<Integer>());
        for (HqclcfEmp emp : emps) {
            if (emp.getPost() != null) {
                if (emp.getPost().equals(code)) {
                    json.put("code", "503");
                    return json;
                }
            }

        }

        switch (value) {
            case 1:
                List<HqclcfPost> postUpper = hqclcfPostMapper.queryUpperPidById(post.getPriNumber());
                for (HqclcfPost p : postUpper) {
                    if (p.getStatus().equals("0")) {
                        json.put("code", "501");
                        return json;
                    }
                }
                break;
            case 0:
                List<HqclcfPost> postLower = hqclcfPostMapper.queryLowerPidById(post.getPriNumber());
                for (HqclcfPost p : postLower) {
                    if (p.getStatus().equals("1")) {
                        json.put("code", "502");
                        return json;
                    }
                }
                break;
        }

        return json;
    }

    @Override
    public HqclcfPost queryPost(Long id) throws Exception {
        HqclcfPost hqclcfPost = new HqclcfPost();
        hqclcfPost.setPriNumber(id);
        List<HqclcfPost> hqclcfPosts = hqclcfPostMapper.queryAll(hqclcfPost);
        if (hqclcfPosts.size() > 1)
            throw new AppException("选择岗位Id不正确");
        HqclcfPost hqclcfPost1 = hqclcfPosts.size() == 1 ? hqclcfPosts.get(0) : hqclcfPost;
        return hqclcfPost1;
    }

    @Override
    public List<SysConfigType> buildPostTypeByBl(Integer businessLine) throws Exception {
        List<SysConfigType> list = new ArrayList<>();
        switch (businessLine) {
            // 总部-
            case 1:
                break;
            // 信贷-
            case 3:
                list = sysConfigTypeMapper.getConfigByPSysCode(Constant.CL_DEPT_TYPE);
                break;
            // 消分-
            case 2:
                list = sysConfigTypeMapper.getConfigByPSysCode(Constant.XF_DEPT_TYPE);
                break;

        }
        return list;
    }

    @Override
    public Json addPost(String data) throws Exception {
        Json json = new Json();
        try {
            ObjectMapper mapper = new ObjectMapper();
            HqclcfPost post = mapper.readValue(data.getBytes(), HqclcfPost.class);
            HqclcfPost postOfCheck = new HqclcfPost();
            postOfCheck.setPriNumber(post.getPostPid());
            if (hqclcfPostMapper.queryAll(postOfCheck).get(0).getStatus().equals(Constant.DISABLE)) {
                json.setMsg("你的上级岗位已被禁用！");
                return json;
            }
            if (judgeStatus(hqclcfPostMapper.queryAll(postOfCheck).get(0).getPostNo(), 1).get("code") == "501") {
                json.setMsg("请检查上级岗位是否被禁用！");
                return json;
            }

            if (null == post.getPostName() || post.getPostName().trim().equals("")) {
                json.setMsg("职位名称禁止空格！");
                return json;
            }

            post.setCreateTime(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd"));
            post.setCreateBy(CommonUtil.getOnlineFullName());

            List<HqclcfBusiness> businesses = queryAllBusiness();
            for (HqclcfBusiness b : businesses) {
                if (b.getPosCode().equals(post.getRankNo())) {
                    if (b.getStatus().equals("0")) {
                        json.setSuccess(false);
                        json.setMsg("职务可能已被禁用,请检查后重试！");
                        return json;
                    }
                }
            }
            hqclcfPostMapper.addPost(post);
            json.setObj(post);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public List<SysConfigType> buildPostTypeById(Long id) throws Exception {
        HqclcfPost post = queryPost(id);
        return buildPostTypeByBl(post.getBusinessLine());
    }

    @Override
    public HqclcfPost querySuperPost(Long id) throws Exception {
        /**
         * 查询fNodes
         */
        HqclcfPost hqclcfPost = queryPost(queryPost(id).getPriNumber());
        return hqclcfPost;
    }

    @Override
    public Json editPost(String data) throws Exception {
        Json json = new Json();
        try {
            ObjectMapper mapper = new ObjectMapper();
            HqclcfPost hqclcfPost = mapper.readValue(data.getBytes(), HqclcfPost.class);
            List<HqclcfPost> posts = hqclcfPostMapper.queryAll(hqclcfPost);

            if (hqclcfPost.getPostNo().equals("GW0001")) {
                json.setMsg("岗位结构不可更改！");
                return json;
            }

            if (null == hqclcfPost.getPostName() || hqclcfPost.getPostName().trim().equals("")) {
                json.setMsg("职位名称禁止空格！");
                return json;
            }

            HqclcfPost postTrue = hqclcfPostMapper.queryByPostNo(hqclcfPost.getPostNo());

            if (hqclcfPost.getPostPid().equals(postTrue.getPriNumber())) {
                json.setMsg("不可以设自己为上级岗位！");
                return json;
            }

            hqclcfPostMapper.editPost(hqclcfPost);
            json.setObj(hqclcfPostMapper.queryByPostNo(hqclcfPost.getPostNo()));
            json.setSuccess(true);
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public Json del(Long id) throws Exception {
        Json json = new Json();

        try {
            HqclcfPost post = queryPost(id);
            List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("", "", new ArrayList<Integer>());
            for (HqclcfEmp emp : emps) {
                if (emp.getPost() != null) {
                    if (emp.getPost().equals(post.getPostNo())) {
                        json.setSuccess(false);
                        json.setMsg("岗位正在被其他员工使用中！");
                        return json;
                    }
                }
            }
            if (id == 1) {
                json.setSuccess(false);
                json.setMsg("岗位结构不可删除！");
                return json;
            } else {

                List<HqclcfPost> posts = hqclcfPostMapper.queryLowerPost(id);

                for (HqclcfPost p : posts) {
                    if (p != null) {
                        if (p.getStatus().equals(Constant.ENABLE)) {
                            json.setSuccess(false);
                            json.setMsg("下级岗位存在正在使用的岗位无法删除！");
                            return json;
                        }
                    }
                }

                json.setSuccess(true);
                hqclcfPostMapper.delById(id);
            }
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public JSONObject detail(Long id) throws Exception {
        /**
         * 返回的json对象
         */
        JSONObject obj = new JSONObject();
        HqclcfPost post = queryPost(id);

        /**
         * 业务线
         */
        obj.put("blName", "");
        for (SysConfigType type : sysConfigTypeMapper.getConfigByPSysCode(Constant.BUSINESS_LINE)) {
            if ((null != post.getBusinessLine() ? post.getBusinessLine() : "").equals(type.getSysValue())) {
                obj.put("blName", type.getSysName());
                break;
            }
        }

        /**
         * 上级岗位
         */
        obj.put("pdName", null != queryPost(post.getPostPid()).getPostName() ? queryPost(post.getPostPid()).getPostName() : "");

        switch (post.getStatus()) {
            case Constant.ENABLE:
                post.setStatus("启用 ");
                break;
            case Constant.DISABLE:
                post.setStatus("禁用 ");
                break;
        }
        obj.put("post", post);
        return obj;
    }

    /**
     * 生成6位随机数
     *
     * @return
     */
    public String getOrderIdByUUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            // 有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 12 代表长度为12
        // d 代表参数为正数型
        return "GW" + Md5Util.encode(String.format("%12d", hashCodeV)).substring(0, 4);
    }

    @Override
    public HqclcfPost getHqclcfPostBypostCode(String postCode) throws Exception {
        return hqclcfPostMapper.queryByPostNo(postCode);
    }

    @Override
    public JSONObject checkprePost(String postId) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        HqclcfPost post = hqclcfPostMapper.queryByPostId(Long.valueOf(postId), "1");
        Integer currcount = hqclcfEmpMapper.queryEmpOrganizatPost(post.getPostNo());
        if (currcount + 1 > post.getOrganizat()) {
            obj.put("success",false);
            obj.put("msg", "当前岗位已超编! 当前岗位在编人员:" + currcount + " 改岗位编制：" + post.getOrganizat());
        }
        return obj;
    }

}
