package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by zhouliang on 2018/2/2.
 * 百融不良信息返回总对象
 */
public class HqclcfEmpApvBrResult implements Serializable{
    @JsonProperty("product")
    private HqclcfEmpApvBrProduct product;
    @JsonProperty("swift_number")
    private String swiftNumber;
    @JsonProperty("flag")
    private HqclcfEmpApvBrFlag flag;
    @JsonProperty("code")
    private int code;

    public HqclcfEmpApvBrProduct getProduct() {
        return product;
    }

    public void setProduct(HqclcfEmpApvBrProduct product) {
        this.product = product;
    }

    public String getSwiftNumber() {
        return swiftNumber;
    }

    public void setSwiftNumber(String swiftNumber) {
        this.swiftNumber = swiftNumber;
    }

    public HqclcfEmpApvBrFlag getFlag() {
        return flag;
    }

    public void setFlag(HqclcfEmpApvBrFlag flag) {
        this.flag = flag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
