package com.zgmz.ls.utils;

public class FamilyInfo {
    public String xzqhdm; //社区居委会12位行政区划代码
    public String sqrxm; //申请人姓名
    public String sqrsfzh; //申请人身份证号
    public String sqrq; //申请日期
    public String jzywlx; //救助业务类型
    public String zyzpyy;//主要致贫原因

    public int bzrks; //保障人口数
    public int jtyzsr; //家庭月总收入
    public String sfynrfp; //是否已纳入扶贫
    public String lxdh; //联系电话
    public String jzdz; //居住地址
    public String jzksny; //救助开始年月, 格式为yyyyMM
    public String Jzjzny; //救助截止年月, 格式为yyyyMM


    public String ToJSONString() {
        return String.format("\"xzqhdm\":\"%s\", \"sqrxm\":\"%s\", \"sqrsfzh\":\"%s\", \"sqrq\":\"%s\", \"jzywlx\":\"%s\", \"zyzpyy\":\"%s\", \"bzrks\":%d, \"jtyzsr\":%d, \"sfynrfp\":\"%s\", \"lxdh\":\"%s\", \"jzdz\":\"%s\", \"jzksny\":\"%s\", \"Jzjzny\":\"%s\"",
                xzqhdm,
                sqrxm,
                sqrsfzh,
                sqrq,
                jzywlx,
                zyzpyy,
                bzrks,
                jtyzsr,
                sfynrfp,
                lxdh,
                jzdz,
                jzksny,
                Jzjzny);
    }
}
