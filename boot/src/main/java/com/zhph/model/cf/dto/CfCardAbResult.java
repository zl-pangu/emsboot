package com.zhph.model.cf.dto;


import java.io.Serializable;

/**
 * Created by zhouliang on 2018/1/18.
 */
public class CfCardAbResult implements Serializable{
    private Long areaId;
    private Long orgId;
    private Long saleId;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
}
