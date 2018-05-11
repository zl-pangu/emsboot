package com.zhph.service.hqclcf.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpApvMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.mapper.sys.SysUserMapper;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.model.vo.ZtreeVo;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.Json;
import com.zhph.util.StringUtil;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhouliang on 2017/11/29.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfDeptServiceImpl implements HqclcfDeptService {
    @Resource
    private HqclcfDeptMapper hqclcfDeptMapper;
    @Autowired
    private SysConfigTypeMapper sysConfigTypeMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private HqclcfEmpApvMapper empApvMapper;

    @Override
    public JSONArray buildDeptTree(String data, Long userId) throws Exception {
        JSONArray jsonArray = new JSONArray();
        HqclcfDept hqclcfDept = new HqclcfDept();
        if (data != null && !"".equals(data)) {
            ObjectMapper mapper = new ObjectMapper();
            hqclcfDept = mapper.readValue(data.getBytes("utf-8"), HqclcfDept.class);
        }
        Map<Long, Long> deptMap = new HashMap<>();
        if (userId != null) {
            List<Long> list = sysUserMapper.queryDeptIdByUserId(userId);
            for (Long deptId : list) {
                deptMap.put(deptId, deptId);
            }
        }
        List<HqclcfDept> depts = hqclcfDeptMapper.queryTreeByParams(hqclcfDept);
        for (HqclcfDept dept : depts) {
            ZtreeVo tree = new ZtreeVo();
            tree.setId(dept.getId());
            tree.setChecked(null != deptMap.get(dept.getId()) ? true : false);
            tree.setPid(dept.getPid());
            tree.setName(dept.getDeptName());
            jsonArray.add(tree);
        }
        return jsonArray;
    }

    @Override
    public HqclcfDept queryDept(Long id) throws Exception {
        HqclcfDept hqclcfDept = new HqclcfDept();
        hqclcfDept.setId(id);
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(hqclcfDept);
        if (depts.size() > 1)
            throw new AppException("一个部门只能有一个ID");
        HqclcfDept dept = depts.size() == 1 ? depts.get(0) : hqclcfDept;
        return dept;
    }


    @Override
    public List<SysConfigType> buildDeptTypeByBl(Integer businessLine) throws Exception {
        List<SysConfigType> list = new ArrayList<>();
        switch (businessLine) {
            //总部
            case 1:
                break;
            //消分
            case 2:
                list = sysConfigTypeMapper.getConfigByPSysCode(Constant.XF_DEPT_TYPE);

                break;
            //信贷
            case 3:
                list = sysConfigTypeMapper.getConfigByPSysCode(Constant.CL_DEPT_TYPE);
                break;
        }
        return list;
    }

    @Override
    public HqclcfDept addDept(String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HqclcfDept dept = mapper.readValue(data.getBytes("utf-8"), HqclcfDept.class);
        dept.setCreateTime(new Date());
        dept.setCreator(CommonUtil.getOnlineFullName());
        //同一业务条线和上级部门的部门不允许新增
        HqclcfDept checkQuery = new HqclcfDept();
        checkQuery.setBusinessLine(dept.getBusinessLine());
        checkQuery.setPid(dept.getPid());
        checkQuery.setDeptName(dept.getDeptName());
        List<HqclcfDept> depts = queryAll(checkQuery);
        if (depts.size() > 0)
            throw new AppException("不能新增同一业务条线下同上级部门且部门名字相同的部门");
        //上级部门被禁用不允许新增下级部门
        HqclcfDept pDept = queryDept(dept.getPid());
        if (pDept.getStatus().equals("0"))
            throw new AppException("上级部门被禁用不允许新增下级部门");
        hqclcfDeptMapper.addDept(dept);
        return dept;
    }

    @Override
    public List<SysConfigType> buildDeptTypeById(Long id) throws Exception {
        HqclcfDept dept = queryDept(id);
        return buildDeptTypeByBl(dept.getBusinessLine());
    }

    @Override
    public HqclcfDept querySuperDept(Long id) throws Exception {
        HqclcfDept superDept = queryDept(queryDept(id).getPid());
        return superDept;
    }

    @Override
    public JSONObject editDept(String data) throws Exception {
        JSONObject json = new JSONObject();
        json.put("code", 200);
        try {
            ObjectMapper mapper = new ObjectMapper();
            HqclcfDept hqclcfDept = mapper.readValue(data.getBytes("utf-8"), HqclcfDept.class);
            //修改自己的信息
            hqclcfDeptMapper.editEdit(hqclcfDept);
            //修改下级的信息
            HqclcfDept childDept = new HqclcfDept();
            childDept.setPid(hqclcfDept.getId());
            List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(childDept);
            if (depts.size() > 0) {
                for (int i = 0; i < depts.size(); i++) {
                    HqclcfDept dept = depts.get(i);
                    dept.setStatus(hqclcfDept.getStatus());
                }
                hqclcfDeptMapper.updateDeptByPdept(depts);
            }
            json = buildResultData(hqclcfDept, json);
        } catch (Exception e) {
            json.put("code", 500);
            json.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 构建返回前台的data
     *
     * @param dept
     * @param obj
     */
    private JSONObject buildResultData(HqclcfDept dept, JSONObject obj) throws Exception {
        obj.put("blName", "");
        for (SysConfigType type : sysConfigTypeMapper.getConfigByPSysCode(Constant.BUSINESS_LINE)) {
            if ((null != dept.getBusinessLine() ? dept.getBusinessLine() : "").equals(type.getSysValue())) {
                obj.put("blName", type.getSysName());
                break;
            }
        }
        obj.put("pdName", null != queryDept(dept.getPid()).getDeptName() ? queryDept(dept.getPid()).getDeptName() : "");
        switch (dept.getStatus()) {
            case Constant.ENABLE:
                dept.setStatus("启用");
                break;
            case Constant.DISABLE:
                dept.setStatus("禁用");
                break;
        }
        String creator = dept.getCreator();
        Date createTime = dept.getCreateTime();
        HqclcfDept deptEdit = queryDept(dept.getId());
        if ("".equals(creator)) {
            obj.put("creator", deptEdit.getCreator());
        }
        if (null == createTime) {
            String createTimes = DateUtil.parseDateFormat(deptEdit.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            obj.put("createTime", createTimes);
        }
        obj.put("dept", dept);
        return obj;
    }


    @Override
    public Json del(Long id) throws Exception {
        Json json = new Json();
        try {
            HqclcfDept dept = new HqclcfDept();
            dept.setPid(id);
            dept.setStatus(Constant.ENABLE);
            List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(dept);
            if (depts.size() > 0) {
                json.setMsg("下级存在正在使用的部门，无法删除！");
            } else {
                json.setSuccess(true);
                hqclcfDeptMapper.delById(id);
            }
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public JSONObject detail(Long id) throws Exception {
        JSONObject obj = new JSONObject();
        HqclcfDept dept = queryDept(id);
        obj = buildResultData(dept, obj);
        return obj;
    }

    @Override
    public JSONObject checkDeptPrepare(Long id, String type) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        switch (type) {
            case "add":
                HqclcfDept dept = queryDept(id);
                Integer organizat = dept.getOrganizat();
                String deptCode = dept.getDeptCode();
                HqclcfEmp emp = new HqclcfEmp();
                emp.setDeptNo(deptCode);
                List<HqclcfEmp> hqclcfEmps = empApvMapper.queryEmpByStutas(emp, "1");
                int empCount = hqclcfEmps.size();
                if (empCount + 1 > organizat) {
                    obj.put("success", false);
                    obj.put("msg", "当前员工除开离职人数：" + empCount + "部门编制：" + organizat + ";此部门超编!");
                }
                break;
        }
        return obj;
    }

    @Override
    public JSONArray buildTreeByUserbL(List<Integer> bl) throws Exception {
        JSONArray jsonArray = new JSONArray();
        List<HqclcfDept> depts = hqclcfDeptMapper.queryTreeByOnlineUserBl(bl);
        for (HqclcfDept dept : depts) {
            ZtreeVo tree = new ZtreeVo();
            tree.setId(dept.getId());
            tree.setPid(dept.getPid());
            tree.setName(dept.getDeptName());
            jsonArray.add(tree);
        }
        return jsonArray;
    }

    @Override
    public JSONObject checkDeptEnable(Long treeId, String sl) throws Exception {
        JSONObject object = new JSONObject();
        object.put("success", true);
        switch (sl) {
            case "1"://启用
                Map<String, Object> map = new HashMap<>();
                map.put("type", "NotOwnAndMaxP");
                map.put("id", treeId);
                List<HqclcfDept> depts = hqclcfDeptMapper.queryPidObjById(map);
                boolean flag = true;
                for (HqclcfDept dept : depts) {
                    flag = "0".equals(dept.getStatus()) ? false : true;
                    break;
                }
                if (!flag) {
                    object.put("success", false);
                    object.put("msg", "上级有禁用的部门，不能选择启用！");
                }
                break;
            case "0"://禁用
                HqclcfDept deptEnble = new HqclcfDept();
                deptEnble.setStatus("1");
                deptEnble.setPid(queryDept(treeId).getId());
                //查询下级有没有启用状态的部门
                if (hqclcfDeptMapper.queryAll(deptEnble).size() > 0 ? true : false) {
                    object.put("success", false);
                    object.put("msg", "下级有启用的部门，不能选择禁用！");
                }
                break;
        }
        return object;
    }

    @Override
    public JSONArray queryDeptTreeByisShowChild(String isShow) throws Exception {
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> params = new HashMap<>();
        SysUser onlineUser = CommonUtil.getOnlineUser();
        params.put("userId", onlineUser.getUserId());
        List<HqclcfDept> depts = queryChildDepts(params);
        if (depts.size() > 0) {
            if (Constant.ON_SHOW_CHILD_DEPT.equals(isShow)) {
                //获取下级部门树
                for (HqclcfDept dept : depts) {
                    ZtreeVo tree = new ZtreeVo();
                    tree.setId(dept.getId());
                    tree.setPid(dept.getPid());
                    tree.setName(dept.getDeptName());
                    jsonArray.add(tree);
                }
            } else if (Constant.OFF_SHOW_CHILD_DEPT.equals(isShow)) {
                if ((onlineUser.getCurrentDept() != null)) {
                    //获取当前部门树
                    for (HqclcfDept dept : depts) {
                        if (dept.getDeptCode().equals(onlineUser.getCurrentDept().getDeptCode())) {//判断是否是当前部门
                            ZtreeVo tree = new ZtreeVo();
                            tree.setId(dept.getId());
                            tree.setPid(dept.getPid());
                            tree.setName(dept.getDeptName());
                            jsonArray.add(tree);
                            break;
                        }
                        continue;
                    }
                } else {
                    //当前登录用户没有指定相关部门
                    throw new Exception("当前用户没有本部门数据!");
                }
            }
        }
        return jsonArray;
    }

    @Override
    public JSONArray buildPostTreeByDeptId(Long id) throws Exception {
        JSONArray array = new JSONArray();
        List<HqclcfPost> hqclcfPosts = hqclcfDeptMapper.queryPostByDeptId(id, "1");
        for (HqclcfPost post : hqclcfPosts) {
            ZtreeVo tree = new ZtreeVo();
            tree.setId(post.getPriNumber());
            tree.setPid(post.getPostPid());
            tree.setName(post.getPostName());
            array.add(tree);
        }
        return array;
    }

    /**
     * 根据条件查询部门信息（初次设计目的是为了查询某业务线的部门）
     *
     * @param hqclcfDept
     * @return
     * @throws Exception
     */
    @Override
    public List<HqclcfDept> queryAll(HqclcfDept hqclcfDept) throws Exception {
        return hqclcfDeptMapper.queryAll(hqclcfDept);
    }


    @Override
    public HqclcfDept queryDeptByDeptCode(String deptCode) throws Exception {
        HqclcfDept hqclcfDept = new HqclcfDept();
        hqclcfDept.setDeptCode(deptCode);
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(hqclcfDept);
        if (depts.size() > 1)
            throw new AppException("一个部门只能有一个ID");
        HqclcfDept dept = depts.size() == 1 ? depts.get(0) : hqclcfDept;
        return dept;
    }

    /**
     * 根据userId查询所在部门及所有上级部门节点
     *
     * @param /deptId/deptCode
     * @return
     * @throws Exception
     */
    @Override
    public List<HqclcfDept> queryParentDepts(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("type", "up");
        List<HqclcfDept> depts = null;
        try {
            depts = hqclcfDeptMapper.queryParentOrSubDepts(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return depts;
    }

    /**
     * 根据userId查询所在部门及所有下级部门节点
     *
     * @param /deptId/deptCode
     * @return
     * @throws Exception
     */
    @Override
    public List<HqclcfDept> queryChildDepts(Map<String, Object> map) throws Exception {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("type", "down");
        return hqclcfDeptMapper.queryParentOrSubDepts(map);
    }

    /**
     * 查询用户拥有权限的部门s
     *
     * @param userId
     */
    public List<HqclcfDept> queryAuthedDeptsByUserId(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return null;
        }
        return hqclcfDeptMapper.queryAuthedDeptsByUserId(userId);
    }

    @Override
    public List<HqclcfDept> queryOrgParams() throws Exception {
        return hqclcfDeptMapper.queryOrgParams();
    }

    @Override
    public List<HqclcfDept> queryDeptUpOrDown(String type, Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("deptId", id);
        return hqclcfDeptMapper.queryChildNodeBydeptId(map);
    }

	@Override
	public HqclcfDept queryDeptByCode(String deptCode) throws Exception {
		return hqclcfDeptMapper.queryDeptNameByCode(deptCode);
	}
	
	@Override
	public HqclcfDept queryDeptChildNameByCode(String deptName,String deptCode) throws Exception {
		HqclcfDept hqclcfDept = new HqclcfDept();
		hqclcfDept.setDeptName(deptName);
		hqclcfDept.setDeptCode(deptCode);
		return hqclcfDeptMapper.queryDeptChildNameByCode(hqclcfDept);
	}
}

