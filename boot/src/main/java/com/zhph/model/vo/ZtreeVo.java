package com.zhph.model.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */
public class ZtreeVo implements Serializable{
    private Long id;
    private Long pid;
    private String name;
    private boolean checked=false;
    private boolean open=false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
