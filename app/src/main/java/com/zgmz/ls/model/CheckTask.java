package com.zgmz.ls.model;

import java.util.Date;

/**
 * Created by robert on 2017/2/15.
 */

public class CheckTask {
    public static final String STATUS_DOWNLOADING = "DOWNLOADING";
    public static final String STATUS_NEW_ADDED = "NEW_ADDED";
    public int id;
    private FamilyBase family;
    private String nd;          // 抽查年度
    private String date;        // 抽查日期
    private String fzr;         // 抽查负责人
    private String cyry;        // 抽查相关人
    private String lx;          // 抽查类型， 01-核查 02-抽查 03-新增
    private String server;
    private String rpc;
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRpc() {
        return rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public String ToJSONString() {
        return String.format("\"nd\":\"%s\", \"rq\":\"%s\", \"fzr\":\"%s\", "+
                        "\"lx\":\"%s\"",
                nd, date, fzr, lx);
    }
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    private String target;      // 抽查对象
    private String check_task_id;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCheck_task_id() {
        return check_task_id;
    }

    public void setCheck_task_id(String check_task_id) {
        this.check_task_id = check_task_id;
    }

    public void setId(int iid) {
        id = iid;
    }
    public int getId() {
        return id;
    }
    public void setNd(String ind) {
        nd = ind;
    }
    public String getNd() {
        return nd;
    }
    public void setDate(String idate) {
        date = idate;
    }
    public String getDate() {
        return date;
    }
    public void setFzr(String ifzr) {
        fzr = ifzr;
    }
    public String getFzr() {
        return fzr;
    }
    public void setCyry(String icyry) {
        cyry = icyry;
    }
    public String getCyry() {
        return cyry;
    }
    public void setLx(String ilx) {
        lx = ilx;
    }
    public String getLx() {
        return lx;
    }
    public void setFamiy(FamilyBase ifamily) {
        family = ifamily;
    }
    public FamilyBase getFamiy() {
        return family;
    }
}