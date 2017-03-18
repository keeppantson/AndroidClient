package com.zgmz.ls.utils;

public class FamilyMember {
    public String cyxm; //成员姓名
    public String cysfzh; //成员身份证号
    public String xb; //性别
    public int nl; //年龄,数字类型
    public String ldnl; //劳动能力
    public String jkzk; //健康状况
    public String cjlb; //残疾类别
    public String whcd; //文化程度
    public String sfzx; //是否在校
    public String ysqrgx; //与申请人关系
    public String ryzt; //人员状态
    public String sfzzp; //是否有该人员身份证头像照片
    public String hcdxzp; //是否已核查对象照片
    public String hcsfz; //是否已核查身份证
    public String hchkb; //是否已核查户口本

    public String ToJSONString() {
        return String.format("\"cyxm\":\"%s\", \"cysfzh\":\"%s\", \"xb\":\"%s\", \"nl\":%d, \"ldnl\":\"%s\", \"jkzk\":\"%s\", \"cjlb\":\"%s\", \"whcd\":\"%s\", \"sfzx\":\"%s\", \"ysqrgx\":\"%s\", \"ryzt\":\"%s\", \"sfzzp\":\"%s\", \"hcdxzp\":\"%s\", \"hcsfz\":\"%s\", \"hchkb\":\"%s\"",
                cyxm,
                cysfzh,
                xb,
                nl,
                ldnl,
                jkzk,
                cjlb,
                whcd,
                sfzx,
                ysqrgx,
                ryzt,
                sfzzp,
                hcdxzp,
                hcsfz,
                hchkb);
    }
}
