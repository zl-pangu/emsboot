package com.zhph.mapper.cf;

import com.zhph.model.cf.CfTimeLock;
import org.springframework.stereotype.Repository;

/**
 * Create By lishuangjiang
 */
@Repository
public interface CfTimeLockMapper {

    /**
     * 检测当前工资年月是否上锁
     * @param cfTimeLock
     * @return
     */
    CfTimeLock queryIsLock(CfTimeLock cfTimeLock);

    /**
     * 锁定/解锁
     * @param cfTimeLock
     * @return
     */
    int updateLock(CfTimeLock cfTimeLock);

    /**
     * 上锁
     * @param cfTimeLock
     * @return
     */
    int insertLock(CfTimeLock cfTimeLock);

}
