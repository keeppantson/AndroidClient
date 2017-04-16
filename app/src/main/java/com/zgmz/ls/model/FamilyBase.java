package com.zgmz.ls.model;

import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.db.TableTools;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by robert on 2017/2/15.
 */

public class FamilyBase {
    @Override
    public String toString() {
        return "FamilyBase{" +
                xzqhdm != null ?"xzqhdm='" + xzqhdm + '\'' : ""
                + sqrxm != null ? ", sqrxm='" + sqrxm + '\'' :""
                + sqrsfzh != null ? ", sqrsfzh='" + sqrsfzh + '\'' :""
                + sqrq != null ? ", sqrq=" + sqrq  :""
                + jzywlx != null ? ", jzywlx='" + jzywlx + '\'' :""
                + zyzpyy != null ? ", zyzpyy='" + zyzpyy + '\'' :""
                +  ", members=" + members
                +  ", attachments=" + attachments
                +  '}';
    }

    public class member {       // 成员信息

        public String getSfz_status() {
            return sfz_status;
        }

        public void setSfz_status(String sfz_status) {
            this.sfz_status = sfz_status;
        }

        public String getRyzt() {
            return ryzt;
        }

        public void setRyzt(String ryzt) {
            this.ryzt = ryzt;
        }


        private String father_id;

        public String getFather_id() {
            return father_id;
        }

        public void setFather_id(String father_id) {
            this.father_id = father_id;
        }


        public String getCheck_task_id() {
            return check_task_id;
        }

        public void setCheck_task_id(String check_task_id) {
            this.check_task_id = check_task_id;
        }

        public String getYsqrgx() {
            return ysqrgx;
        }

        public void setYsqrgx(String ysqrgx) {
            this.ysqrgx = ysqrgx;
        }

        public String getSfzzp() {
            return sfzzp;
        }

        public void setSfzzp(String sfzzp) {
            this.sfzzp = sfzzp;
        }

        public String getHcdxzp() {
            return hcdxzp;
        }

        public void setHcdxzp(String hcdxzp) {
            this.hcdxzp = hcdxzp;
        }

        public String getHcsfz() {
            return hcsfz;
        }

        public void setHcsfz(String hcsfz) {
            this.hcsfz = hcsfz;
        }

        public String getHchkb() {
            return hchkb;
        }

        public void setHchkb(String hchkb) {
            this.hchkb = hchkb;
        }

        public void setCyxm(String icyxm) {
            cyxm = icyxm;
        }

        public String getCyxm() {
            return cyxm;
        }
        public void setCysfzh(String icysfzh) {
            cysfzh = icysfzh;
        }

        public String getCysfzh() {
            return cysfzh;
        }
        public void setXb(String ixb) {
            xb = ixb;
        }

        public String getXb() {
            return xb;
        }
        public void setNl(String inl) {
            nl = inl;
        }

        public String getNl() {
            return nl;
        }
        public void setLdnl(String ildnl) {
            ldnl = ildnl;
        }

        public String getLdnl() {
            return ldnl;
        }
        public void setJkzk(String ijkzk) {
            jkzk = ijkzk;
        }

        public String getJkzk() {
            return jkzk;
        }
        public void setCjlb(String icjlb) {
            cjlb = icjlb;
        }

        public String getCjlb() {
            return cjlb;
        }
        public void setCjdj(String icjdj) {
            cjdj = icjdj;
        }

        public String getCjdj() {
            return cjdj;
        }

        public void setWhcd(String iwhcd) {
            whcd = iwhcd;
        }

        public String getWhcd() {
            return whcd;
        }

        public void setSfzx(String isfzx) {
            sfzx = isfzx;
        }

        public String getSfzx() {
            return sfzx;
        }

        @Override
        public String toString() {
            return "member{" +
                    "cyxm='" + cyxm + '\'' +
                    ", cysfzh='" + cysfzh + '\'' +
                    ", xb='" + xb + '\'' +
                    ", nl=" + nl +
                    ", ldnl='" + ldnl + '\'' +
                    ", jkzk='" + jkzk + '\'' +
                    ", cjlb='" + cjlb + '\'' +
                    ", cjdj='" + cjdj + '\'' +
                    ", whcd='" + whcd + '\'' +
                    ", sfzx='" + sfzx + '\'' +
                    ", sfzzp='" + sfzzp + '\'' +
                    ", hcdxzp='" + hcdxzp + '\'' +
                    ", hcsfz='" + hcsfz + '\'' +
                    ", hchkb='" + hchkb + '\'' +
                    ", ysqrgx='" + ysqrgx + '\'' +
                    '}';
        }

        private String cyxm;    // 成员姓名
        private String cysfzh;  // 成员身份证
        private String xb;      // 成员性别
        private String nl;      // 成员年龄
        private String ldnl;    // 成员劳动能力
        private String jkzk;    // 成员健康状况
        private String cjlb;    // 成员残疾类别
        private String cjdj;    // 成员残疾等级
        private String whcd;    // 成员文化程度
        private String sfzx;    // 成员是否在校
        private String sfzzp;   // 是否有成员身份证头像照片
        private String hcdxzp;  // 是否已核查照片
        private String hcsfz;   // 是否已核查身份证
        private String hchkb;   // 是否已核查户口本
        private String ysqrgx;  // 成员与申请人关系
        private String ryzt;  // 人员状态
        private String sfz_status;  // 身份证状态
        private String check_task_id;
        public String ToJSONString() {
            String str_ldnl = ldnl != null ? String.format("\"ldnl\":\"%s\"", ldnl) : "";
            String str_jkzk = jkzk != null ? String.format("\"jkzk\":\"%s\"", jkzk) : "";
            String str_cjlb = cjlb != null && !cjlb.equals("")? String.format("\"cjlb\":\"%s\"", cjlb) : "";
            String str_cjdj = cjdj != null && !cjdj.equals("") ? String.format("\"cjdj\":\"%s\"", cjdj) : "";
            String str_whcd = whcd != null ? String.format("\"whcd\":\"%s\"", whcd) : "";
            String str_sfzx = sfzx != null ? String.format("\"sfzx\":\"%s\"", sfzx) : "";
            String str_sfzzp = hcsfz != null && !hcsfz.equals("") ? String.format("\"sfzzp\":\"%s\"", hcsfz == "01" ? "1" : "0") : "";
            //TODO
            String str_hcdxzp = hcsfz != null && !hcsfz.equals("") ? String.format("\"hcdxzp\":\"%s\"", hcsfz) : "";
            String str_hcsfz = hcsfz != null && !hcsfz.equals("") ? String.format("\"hcsfz\":\"%s\"", hcsfz) : "";
            String str_hchkb = hchkb != null && !hchkb.equals("") ? String.format("\"hchkb\":\"%s\"", hchkb) : "";
            String str_sfzlx = sfz_status != null && !sfz_status.equals("") ? String.format("\"sfz_status\":\"%s\"", sfz_status) : "";
            String res =  String.format("{\"cyxm\":\"%s\", \"cysfzh\":\"%s\", \"xb\":\"%s\", \"nl\":%s, "
                            + "\"ysqrgx\":\"%s\","
                            + "\"ryzt\":\"%s\"",
                    cyxm,
                    cysfzh,
                    xb,
                    nl,
                    ysqrgx,
                    ryzt);
            if (!str_ldnl.equals("")) {
                res += "," + str_ldnl;
            }
            if (!str_jkzk.equals("")) {
                res += "," + str_jkzk;
            }
            if (!str_cjlb.equals("")) {
                res += "," + str_cjlb;
            }
            if (!str_cjdj.equals("")) {
                res += "," + str_cjdj;
            }
            if (!str_whcd.equals("")) {
                res += "," + str_whcd;
            }
            if (!str_sfzx.equals("")) {
                res += "," + str_sfzx;
            }
            if (!str_sfzzp.equals("")) {
                res += "," + str_sfzzp;
            }
            if (!str_hcdxzp.equals("")) {
                res += "," + str_hcdxzp;
            }
            if (!str_hcsfz.equals("")) {
                res += "," + str_hcsfz;
            }
            if (!str_hchkb.equals("")) {
                res += "," + str_hchkb;
            }
            if (!str_sfzlx.equals("")) {
                res += "," + str_sfzlx;
            }
            res += "}";
            return res;
        }
    }

    private class attachment{
        public String getClmc() {
            return clmc;
        }

        public void setClmc(String clmc) {
            this.clmc = clmc;
        }

        public String getCllx() {
            return cllx;
        }

        public void setCllx(String cllx) {
            this.cllx = cllx;
        }

        private String clmc;     // 材料名称
        private String cllx;     // 材料类型

        @Override
        public String toString() {
            return "attachment{" +
                    "clmc='" + clmc + '\'' +
                    ", cllx='" + cllx + '\'' +
                    '}';
        }
    }

    public String getXzqhdm() {
        return xzqhdm;
    }

    public void setXzqhdm(String xzqhdm) {
        this.xzqhdm = xzqhdm;
    }

    public String getSqrxm() {
        return sqrxm;
    }

    public void setSqrxm(String sqrxm) {
        this.sqrxm = sqrxm;
    }

    public String getSqrsfzh() {
        return sqrsfzh;
    }

    public void setSqrsfzh(String sqrsfzh) {
        this.sqrsfzh = sqrsfzh;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getJzywlx() {
        return jzywlx;
    }

    public void setJzywlx(String jzywlx) {
        this.jzywlx = jzywlx;
    }

    public String getZyzpyy() {
        return zyzpyy;
    }

    public void setZyzpyy(String zyzpyy) {
        this.zyzpyy = zyzpyy;
    }

    public List<member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<member> members) {
        this.members = members;
    }

    public List<FamilyBase.attachment> getAttachment() {
        return attachments;
    }

    public void setAttachment(ArrayList<FamilyBase.attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean insertMember(member member_item) {
       return members.add(member_item);
    }

    public boolean removeMember(member member_item) {
        return members.remove(member_item);
    }

    public boolean insertAttachment(attachment attachment_item) {
        return attachments.add(attachment_item);
    }

    public boolean removeAttachment(attachment attachment_item) {
        return attachments.remove(attachment_item);
    }

    private String xzqhdm;      // 行政区代码
    private String sqrxm;       // 申请人姓名
    private String sqrsfzh;     // 申请人身份证号码
    private String sqrq;          // 申请时间
    private String jzywlx;      // 困难救助类型
    private String zyzpyy;      // 致贫原因
    private String reqid;       //上传任务的requestid

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    private String isChecked;

    public String getCheck_task_id() {
        return check_task_id;
    }

    public void setCheck_task_id(String check_task_id) {
        this.check_task_id = check_task_id;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    private String check_task_id;
    private List<member> members;
    private List<attachment> attachments;

    public String ToJSONString() {

        CheckTask task = DBHelper.getInstance().get_check_task(check_task_id);

        String checkTaskJsonStr = task.ToJSONString();

        List<member> members = DBHelper.getInstance().getAllMembers(check_task_id);
        String memJsonStr = "\"cyxx\":[";
        for (int i = 0; i < members.size(); ) {
            memJsonStr = memJsonStr + members.get(i).ToJSONString();
            i++;
            if (i != members.size()) {
                memJsonStr = memJsonStr + ",";
            }
        }
        memJsonStr = memJsonStr + "],";

        List<Attachment> atts = DBHelper.getInstance().getAllAttachments(sqrsfzh, check_task_id);
        String attJsonStr = "\"zmclxx\":[";
        for (int idx = 0; idx < atts.size(); ) {
            attJsonStr = attJsonStr + atts.get(idx).ToJSONString();
            idx++;
            if (idx != atts.size()) {
                attJsonStr = memJsonStr + ",";
            }
        }
        attJsonStr = attJsonStr + "]";
        String report = String.format("{%s,\"jtxx\":[{\"xzqhdm\":\"%s\", \"sqrxm\":\"%s\", \"sqrsfzh\":\"%s\", "+
                        "\"sqrq\":\"%s\", \"jzywlx\":\"%s\", \"zyzpyy\":\"%s\", %s %s }]}",
                checkTaskJsonStr,
                xzqhdm,
                sqrxm,
                sqrsfzh,
                sqrq,
                jzywlx,
                zyzpyy,
                memJsonStr,
                attJsonStr);
        return report;
    }
}
