package com.daluzy.daluzy_project.daluzy_project.domain;

import java.util.List;

public class NewsResult {
    private String stat;
    private List<News> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
