package com.zhph.controller.sys;

import com.github.pagehelper.Page;
import com.zhph.controller.base.BaseController;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysRoles;
import com.zhph.model.sys.SysUser;
import com.zhph.model.vo.BsgridVo;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysRolesService;
import com.zhph.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */
@Controller
@RequestMapping("/sys/roles")
public class SysRolesController extends BaseController {
    @Autowired
    private SysRolesService sysRolesService;

    @Autowired
    private SysConfigTypeService sysConfigTypeService;

    /**
     * 数据列表初始化页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/init")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        SysUser onlineUser = (SysUser) session.getAttribute("onlineUser");
        Long id=onlineUser.getId();
        if (null!=id&&id.equals(new Long(1))) {
            request.setAttribute("isAdmin","true");
        }else{
            request.setAttribute("isAdmin","false");
        }
        return "/pages/sys/roles/sysRolesList";
    }

    /**
     * 数据信息查询
     *
     * @param pageSize
     * @param curPage
     * @param req
     * @return
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object selectByRoleCodeAndRoleName(Integer pageSize, Integer curPage, SysRoles sysRoles, HttpServletRequest req) {
        BsgridVo<SysRoles> bsgridVo = new BsgridVo<SysRoles>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("roleCode", sysRoles.getRoleCode());
        map.put("roleName", sysRoles.getRoleName());
        try {
            // 后台数据取得
            Page<SysRoles> page = this.sysRolesService.selectByRoleCodeAndRoleName(pageSize, curPage, map);
            bsgridVo.setCurPage(curPage.longValue());
            bsgridVo.setData(page);
            bsgridVo.setSuccess(true);
            bsgridVo.setTotalRows(page.getTotal());
        } catch (Exception e) {
            logger.error("系统角色数据查询异常" + e.getMessage());
            e.printStackTrace();
            bsgridVo.setSuccess(false);
        }
        return bsgridVo;
    }

    /**
     * 跳转新增页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addInit")
    public String addInit(HttpServletRequest request, HttpServletResponse response) {
        List<SysConfigType> sysConfigTypeList = sysConfigTypeService.getConfigTypesByPSysCode("system_type");
        request.setAttribute("sysTypeList", sysConfigTypeList);
        return "/pages/sys/roles/sysRolesAdd";
    }

    /**
     * 新增页面操作
     *
     * @param req
     * @param response
     * @param sysRoles
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public Object add(
            HttpServletRequest req, HttpServletResponse response, SysRoles sysRoles, @RequestParam("nodes[]") String[] nodes) {
        ResultVo resultVo = new ResultVo();

        try {
            HttpSession session = req.getSession();
            SysUser onlineUser = (SysUser) session.getAttribute("onlineUser");
            if (null != onlineUser) {
                sysRoles.setCreateName(onlineUser.getFullName());
            }
            sysRoles.setCreateDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            sysRolesService.insertAll(sysRoles, nodes);
            resultVo.setStatus(1);
            resultVo.setInfo("保存成功！");
            logger.info("系统角色保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.setStatus(0);
            resultVo.setInfo("保存异常！");
            logger.error("系统角色保存异常!:\t" + e.getMessage());
        }
        return resultVo;
    }

    /**
     * 修改页面初始化
     *
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("/updateInit")
    public String updateInit(Long roleId, HttpServletRequest request) {
        SysRoles sysRoles = sysRolesService.selectByPrimaryKey(roleId);
        request.setAttribute("obj", sysRoles);
        List<SysConfigType> sysConfigTypeList=sysConfigTypeService.getConfigTypesByPSysCode("system_type");
        request.setAttribute("sysTypeList",sysConfigTypeList);

        List<Long> nodeIds = sysRolesService.selectNodeIds(roleId);
        request.setAttribute("nodeIds", nodeIds);//查询 资源节点 id 集合
        return "/pages/sys/roles/sysRolesUpdate";
    }

    /**
     * 修改页面 修改操作
     *
     * @param req
     * @param response
     * @param sysRoles
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Object update(
            HttpServletRequest req, HttpServletResponse response, SysRoles sysRoles, @RequestParam("nodes[]") String[] nodes) {
        ResultVo resultVo = new ResultVo();
        try {
            HttpSession session = req.getSession();
            SysUser onlineUser = (SysUser) session.getAttribute("onlineUser");
            if (null != onlineUser) {
                sysRoles.setUpdateName(onlineUser.getFullName());
            }
            sysRoles.setUpdateDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            sysRolesService.delResourceByRoleId(sysRoles.getRoleId());//删除资源 根据角色id
            sysRolesService.updateAll(sysRoles, nodes);
            resultVo.setStatus(1);
            resultVo.setInfo("修改成功！");
            logger.info("系统角色修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.setStatus(0);
            resultVo.setInfo("修改异常！");
            logger.info("系统角色修改异常:\t" + e.getMessage());
        }
        return resultVo;
    }

    /**
     * 根据角色ID删除 系统角色
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public Object del(Long roleId) {
        ResultVo resultVo = new ResultVo();
        //删除前 校验 是否改角色是否引用资源
        List<Long> nodeIds = sysRolesService.selectNodeIds(roleId);
        if (null != nodeIds && nodeIds.size() > 0) {
            resultVo.setStatus(0);
            resultVo.setInfo("该角色引用了资源，不能删除！");
            logger.info("该角色引用了资源，不能删除！");
            return resultVo;
        }
        try {
            if (null != roleId && !"".equals(roleId)) {
                sysRolesService.deleteByPrimaryKey(roleId);
            }
            resultVo.setStatus(1);
            resultVo.setInfo("删除成功！");
            logger.info("删除系统角色成功！");
        } catch (Exception e) {
            e.printStackTrace();
            resultVo.setStatus(0);
            resultVo.setInfo("删除异常！");
            logger.error("系统角色删除异常:\t" + e.getMessage());
        }
        return resultVo;
    }


    /**
     * 修改页面初始化
     *
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("/detailInit")
    public String detailInit(Long roleId, HttpServletRequest request) {
        SysRoles sysRoles = sysRolesService.selectByPrimaryKey(roleId);
        request.setAttribute("obj", sysRoles);
        return "/pages/sys/roles/sysRolesDetail";
    }

    /**
     * 校验 角色编码唯一性
     *
     * @param roleCode
     * @return
     */
    @RequestMapping("checkRoleCode")
    @ResponseBody
    public Object checkRoleCode(String roleCode) {
        logger.debug("验证角色编码");
        ResultVo resultVo = new ResultVo();
        Map<String, String> map = new HashMap<String, String>();
        map.put("roleCode", roleCode);
        try {
            List<SysRoles> list = sysRolesService.checkRoleCode(map);
            if (0 == list.size()) {
                resultVo.setStatus(1);
                resultVo.setInfo("验证通过");
            } else {
                resultVo.setStatus(0);
                resultVo.setInfo("角色编码已经存在");
            }
        } catch (Exception e) {
            logger.error("角色编码校验失败");
            logger.error(e.getMessage());
        }
        return resultVo;
    }
    /**
     * 校验 角色名称唯一性
     *
     * @param roleName
     * @return
     */
    @RequestMapping("checkRoleName")
    @ResponseBody
    public Object checkRoleName(String roleName) {
        logger.debug("验证角色名称唯一性");
        ResultVo resultVo = new ResultVo();
        Map<String, String> map = new HashMap<String, String>();
        map.put("roleName", roleName);
        try {
            List<SysRoles> list = sysRolesService.checkRoleName(map);
            if (0 == list.size()) {
                resultVo.setStatus(1);
                resultVo.setInfo("验证通过");
            } else {
                resultVo.setStatus(0);
                resultVo.setInfo("角色名称已经存在");
            }
        } catch (Exception e) {
            logger.error("角色名称校验失败");
            logger.error(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 跳转到树
     *
     * @return
     */
    @RequestMapping("/sysRolesTree")
    public String sysRolesTree() {
        return "/pages/sys/roles/sysRolesTree";
    }
}
