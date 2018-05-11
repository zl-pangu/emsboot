package com.zhph.mapper.cf;

import com.zhph.model.cf.CfCardAbnormity;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.Object;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/2.
 */
@Repository
public interface CfCardAbnormityMapper {

    List<CfCardAbnormity> queryAll(CfCardAbnormity cardAbnormity);

    List<Map<String,String>> queryList(CfCardAbnormity cardAbnormity);

    CfCardAbnormity queryAbnByEmpNo(@Param("empNo") String empNo,@Param("gzym") String gzym);

    List<Map<String,String>> queryEmpByQAndGetData(HashMap<String, String> queryMap);

    void addCfCardAb(CfCardAbnormity cfCardAbnormity);

    void batchInsertList(List<CfCardAbnormity> list);

    void delById(Long id);

    void updateByCardId(CfCardAbnormity card);
}
