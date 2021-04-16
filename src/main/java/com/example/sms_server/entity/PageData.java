package com.example.sms_server.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

// 经过分页后的数据格式
public class PageData<T> {
    private Long total;
    @JSONField(name = "items")
    private List<T> items = new ArrayList<T>();

    public PageData(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
    public PageData(){}

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }
}
