package com.zgmz.ls.utils;

// 核查信息
public class CheckInfo {
    public String nd; //核查(抽查)年度
    public String rq; //核查(抽查)日期
    public String fzr; //核查(抽查)负责人
    public String cyry; //核查(抽查)相关参与人员
    public String lx; //核查(抽查)类型

    public String ToJSONString() {
        String a = new String();
        return String.format("\"nd\":\"%s\", \"rq\":\"%s\", \"fzr\":\"%s\", \"cyry\":\"%s\", \"lx\":\"%s\"", nd, rq, fzr, cyry, lx);
    }
}
