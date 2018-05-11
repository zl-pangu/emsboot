package com.zhph.service.cf;

import com.zhph.model.cf.CfCardAbnormity;
import com.zhph.model.cf.dto.CfCardAbResult;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/2.
 */
public interface CfCardAbnormityService {
    void bulidInitAtr(HttpServletRequest req,Long id) throws Exception;

    List<Map<String, String>> queryEmpByQAndGetData(String q) throws Exception;

    void addCardAbnormity(String data) throws Exception;

    Grid<Map<String,String>> queryPageInfo(PageBean pageBean, CfCardAbnormity card) throws Exception;

    void importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

    CfCardAbResult buildDeptSelect(Long id) throws Exception;

    void exportExl(CfCardAbnormity cardAbnormity, HttpServletResponse res) throws Exception;

    void delele(Long id) throws Exception;

    void editCardAbnormity(String data) throws Exception;
}
