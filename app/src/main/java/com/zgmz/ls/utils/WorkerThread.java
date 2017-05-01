package com.zgmz.ls.utils;

import android.content.Context;


import com.zgmz.ls.AppContext;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.QuHuaMa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.zgmz.ls.db.TableTools.TABLE_QUHUAMA;
import static com.zgmz.ls.model.Attachment.TYPE_BEFORE_CHECKED_IMAGE;
import static com.zgmz.ls.model.CheckTask.STATUS_DOWNLOADING;
import static com.zgmz.ls.utils.BitmapUtils.getBitmapFromByte;

public class WorkerThread extends Thread {

    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int TIME_INETRVAL_MS = 10000;

    private boolean mReady = true;

    private static final long update_quhuama_interval_mills = 7 * 24 * 3600 * 1000; // one week

    private static final String hunan_qu_hua_ma = "430000000000";
    private static final int status_ok = 200;
    public boolean initialized = false;
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(TIME_INETRVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!mReady) {
                return;
            }
            if (initialized == false) {
                continue;
            }
            check_qu_hua_ma();
            check_download_task();
            try {
                check_upload_task();
            } catch (org.json.JSONException exp) {
                continue;
            }
        }

    }

    void download_quhuama(DownloadTask task ) {
        int number = 1;
        int page_id = Integer.valueOf(task.getPage_id());
        try {
            String quhuama = AppContext.getAppContext().getTaijiClient().getZoneCode();
            String apptype = "110";
            // TODO
            // RestResult restResult = AppContext.getAppContext().getTaijiClient().BatchGetFamily(task.getQu_hua_ma(),
            //       task.getApply_type(), number, page_id);
            RestResult restResult = AppContext.getAppContext().getTaijiClient().BatchGetFamily(quhuama,
                    apptype, number, page_id);
            if (restResult.statusCode == status_ok) {
                page_id++;
                task.setPage_id(String.valueOf(page_id));
                Long time = System.currentTimeMillis();
                String checkTaskId = Long.toString(time);
                while (DBHelper.getInstance().hasCheckTask(checkTaskId)) {
                    time += 1;
                    checkTaskId = Long.toString(time);
                }
                try {
                    JSONObject obj = restResult.body;
                    task.setTotal_number(obj.getString("total"));
                    JSONArray objdataArray = obj.getJSONArray("data");
                    for (int familyidx = 0; familyidx < objdataArray.length(); familyidx++) {

                        JSONObject familyObj = objdataArray.getJSONObject(familyidx);
                        JSONArray jsonArrayAttachment = familyObj.getJSONArray("zmclxx");
                        JSONArray jsonArrayCY = familyObj.getJSONArray("cyxx");
                        String operator = PreferencesUtils.getInstance().getUsername();
                        CheckTask checkTask = new CheckTask();
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date curDate = new Date(time);//获取当前时间
                        String str = formatter.format(curDate);
                        checkTask.setLx(familyObj.getString("jzywlx"));
                        checkTask.setFzr(operator);
                        checkTask.setNd(Integer.toString(year));
                        checkTask.setDate(str);
                        checkTask.setCheck_task_id(Long.toString(time));
                        checkTask.setStatus(STATUS_DOWNLOADING);

                        DBHelper.getInstance().insertOrUpdateCheckTask(checkTask);
                        String familyid = null;
                        FamilyBase family = new FamilyBase();
                        familyid = familyObj.getString("jtid");
                        family.setCheck_task_id(Long.toString(time));
                        if (familyObj.has("xzqhdm")) {
                            family.setXzqhdm(familyObj.getString("xzqhdm"));
                        }
                        if (familyObj.has("sqrxm")) {
                            family.setSqrxm(familyObj.getString("sqrxm"));
                        }
                        if (familyObj.has("sqrsfzh")) {
                            family.setSqrsfzh(familyObj.getString("sqrsfzh"));
                        }
                        if (familyObj.has("jzywlx")) {
                            family.setJzywlx(familyObj.getString("jzywlx"));
                        }
                        if (familyObj.has("zyzpyy")) {
                            family.setZyzpyy(familyObj.getString("zyzpyy"));
                        }
                        if (familyObj.has("sqrq")) {
                            family.setSqrq(familyObj.getString("sqrq"));
                        }
                        family.setIsChecked("0");

                        byte[] bitmapBuf = null;
                        for (int idx = 0; idx < jsonArrayCY.length(); idx++) {
                            JSONObject cyobj = jsonArrayCY.getJSONObject(idx);
                            FamilyBase.member member = family.new member();
                            member.setCheck_task_id(Long.toString(time));
                            if (cyobj.has("cyxm")) {
                                member.setCyxm(cyobj.getString("cyxm"));
                            }
                            if (cyobj.has("cysfzh")) {
                                member.setCysfzh(cyobj.getString("cysfzh"));
                            }
                            if (cyobj.has("xb")) {
                                member.setXb(cyobj.getString("xb"));
                            }
                            if (cyobj.has("nl")) {
                                member.setNl(cyobj.getString("nl"));
                            }
                            if (cyobj.has("ldnl")) {
                                member.setLdnl(cyobj.getString("ldnl"));
                            }
                            if (cyobj.has("jkzk")) {
                                member.setJkzk(cyobj.getString("jkzk"));
                            }
                            if (cyobj.has("cjlb")) {
                                member.setCjlb(cyobj.getString("cjlb"));
                            }
                            if (cyobj.has("cjdj")) {
                                member.setCjdj(cyobj.getString("cjdj"));
                            }
                            if (cyobj.has("whcd")) {
                                member.setWhcd(cyobj.getString("whcd"));
                            }
                            if (cyobj.has("sfzx")) {
                                member.setSfzx(cyobj.getString("sfzx"));
                            }
                            if (cyobj.has("ryzt")) {
                                member.setRyzt(cyobj.getString("ryzt"));
                            }
                            if (cyobj.has("ysqrgx")) {
                                member.setYsqrgx(cyobj.getString("ysqrgx"));
                            }
                            member.setFather_id(family.getSqrsfzh());
                            DBHelper.getInstance().insertOrUpdateMember(member);
                        }
                        for (int idxx = 0; idxx < jsonArrayAttachment.length(); idxx++) {
                            JSONObject attobj = jsonArrayAttachment.getJSONObject(idxx);
                            // 年月日/低保家庭所在12位行政区划编码/户主身份证/材料类型/材料ID
                            String path = attobj.getString("sqrq") + "/" + familyObj.getString("xzqhdm") +
                                    "/" + familyObj.getString("sqrsfzh") + "/" + attobj.getString("cllx") +
                                    "/" + attobj.getString("clid");
                            byte[] content = AppContext.getAppContext().getTaijiClient().GetMaterial(familyid, family.getXzqhdm(), attobj.getString("clid"));
                            Attachment attachment = new Attachment();
                            attachment.setName(attobj.getString("clmc"));
                            attachment.setPath(path);
                            attachment.setContent(getBitmapFromByte(content));
                            attachment.setType(TYPE_BEFORE_CHECKED_IMAGE);
                            attachment.setCheck_task_id(Long.toString(time));
                            // TODO: Just set a fake time
                            attachment.setTime("2017-04");
                            String card_id = familyObj.getString("sqrsfzh");
                            String id = attobj.getString("clid");
                            if (id.indexOf("-") != -1) {
                                String maybe_card = id.substring(0, id.indexOf("-"));

                                if (maybe_card.equals(family.getSqrsfzh()) && bitmapBuf == null) {
                                    bitmapBuf = content;
                                }
                                FamilyBase base = new FamilyBase();
                                FamilyBase.member info = base.new member();
                                info.setCysfzh(maybe_card);
                                info.setCheck_task_id(Long.toString(time));
                                if (DBHelper.getInstance().hasMember(info)) {
                                    card_id = maybe_card;
                                }
                            }
                            // if no member match ,just set the attachment back to the father
                            attachment.setCard_id(card_id);
                            DBHelper.getInstance().insertOrUpdateAttachment(attachment);

                            FamilyBase.member member = family.new member();
                            member.setCheck_task_id(Long.toString(time));
                            member.setCysfzh(card_id);
                            DBHelper.getInstance().insertOrUpdateMember(member, BitmapUtils.getBitmapFromByte(content));

                        }

                        if (bitmapBuf == null) {
                            DBHelper.getInstance().insertOrUpdateFamilyBase(family);
                        } else {
                            DBHelper.getInstance().insertOrUpdateFamilyBase(family, BitmapUtils.getBitmapFromByte(bitmapBuf));
                        }
                    }
                } catch (org.json.JSONException exp) {
                    DBHelper.getInstance().deleteAllTasks(checkTaskId);
                    return;
                }
                if (Integer.valueOf(task.getTotal_number()) <= Integer.valueOf(task.getPage_id())) {
                    DBHelper.getInstance().deleteDownloadTask(task);
                } else {
                    DBHelper.getInstance().insertOrUpdateDownloadTask(task);

                }
            }

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void download_one_item(DownloadTask task) {

        RestResult restResult = null;
        try {
            restResult = AppContext.getAppContext().getTaijiClient().GetFamily(task.getNow_work_target(),
                    task.getApply_type(), task.getNow_work_target_apply_time());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (restResult.statusCode == status_ok) {
            Long time = System.currentTimeMillis();
            String checkTaskId = Long.toString(time);
            while (DBHelper.getInstance().hasCheckTask(checkTaskId)) {
                time += 1;
                checkTaskId = Long.toString(time);
            }
            try {
                JSONObject obj = restResult.body;
                JSONArray jsonArrayCY = obj.getJSONArray("cyxx");
                JSONArray jsonArrayAttachment = obj.getJSONArray("zmclxx");
                String operator = PreferencesUtils.getInstance().getUsername();
                CheckTask checkTask = new CheckTask();
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date(time);//获取当前时间
                String str = formatter.format(curDate);
                checkTask.setLx(obj.getString("jzywlx"));
                checkTask.setFzr(operator);
                checkTask.setNd(Integer.toString(year));
                checkTask.setDate(str);
                checkTask.setCheck_task_id(Long.toString(time));
                checkTask.setStatus(STATUS_DOWNLOADING);

                DBHelper.getInstance().insertOrUpdateCheckTask(checkTask);

                FamilyBase family = new FamilyBase();
                String familyid = null;
                familyid = obj.getString("jtid");
                family.setCheck_task_id(Long.toString(time));
                family.setXzqhdm(obj.getString("xzqhdm"));
                family.setSqrxm(obj.getString("sqrxm"));
                family.setSqrsfzh(obj.getString("sqrsfzh"));
                family.setSqrq(obj.getString("sqrq"));
                family.setZyzpyy(obj.getString("zyzpyy"));
                family.setJzywlx(obj.getString("jzywlx"));
                family.setIsChecked("0");

                byte[] bitmapBuf = null;
                for (int idx = 0; idx < jsonArrayCY.length(); idx++) {
                    JSONObject cyobj = jsonArrayCY.getJSONObject(idx);
                    FamilyBase.member member = family.new member();
                    member.setCheck_task_id(Long.toString(time));
                    member.setCyxm(cyobj.getString("cyxm"));
                    member.setCysfzh(cyobj.getString("cysfzh"));
                    member.setXb(cyobj.getString("xb"));
                    member.setNl(cyobj.getString("nl"));
                    if (cyobj.has("ldnl")) {
                        member.setLdnl(cyobj.getString("ldnl"));
                    }
                    if (cyobj.has("jkzk")) {
                        member.setJkzk(cyobj.getString("jkzk"));
                    }
                    if (cyobj.has("cjlb")) {
                        member.setCjlb(cyobj.getString("cjlb"));
                    }
                    if (cyobj.has("cjdj")) {
                        member.setCjdj(cyobj.getString("cjdj"));
                    }
                    if (cyobj.has("whcd")) {
                        member.setWhcd(cyobj.getString("whcd"));
                    }
                    if (cyobj.has("sfzx")) {
                        member.setSfzx(cyobj.getString("sfzx"));
                    }
                    member.setFather_id(family.getSqrsfzh());
                    member.setYsqrgx(cyobj.getString("ysqrgx"));
                    member.setRyzt(cyobj.getString("ryzt"));
                    DBHelper.getInstance().insertOrUpdateMember(member);
                }
                for (int idxx = 0; idxx < jsonArrayAttachment.length(); idxx++) {
                    JSONObject attobj = jsonArrayAttachment.getJSONObject(idxx);
                    // 年月日/低保家庭所在12位行政区划编码/户主身份证/材料类型/材料ID
                    String path = attobj.getString("sqrq") + "/" + obj.getString("xzqhdm") +
                            "/" + obj.getString("sqrsfzh") + "/" + attobj.getString("cllx") +
                            "/" + attobj.getString("clid");
                    byte[] content = new byte[0];
                    try {
                        content = AppContext.getAppContext().getTaijiClient().GetMaterial(familyid, family.getXzqhdm(), attobj.getString("clid"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Attachment attachment = new Attachment();
                    attachment.setName(attobj.getString("clmc"));
                    attachment.setPath(path);
                    attachment.setContent(BitmapUtils.getBitmapFromByte(content));
                    attachment.setType(TYPE_BEFORE_CHECKED_IMAGE);
                    attachment.setCheck_task_id(Long.toString(time));
                    // TODO: Just set a fake time
                    attachment.setTime("2017-04");
                    String card_id = obj.getString("sqrsfzh");
                    String id = attobj.getString("clid");
                    if (id.indexOf("-") != -1) {
                        String maybe_card = id.substring(0, id.indexOf("-"));
                        if (maybe_card.equals(family.getSqrsfzh()) && bitmapBuf == null) {
                            bitmapBuf = content;
                        }
                        FamilyBase base = new FamilyBase();
                        FamilyBase.member info = base.new member();
                        info.setCysfzh(maybe_card);
                        info.setCheck_task_id(Long.toString(time));
                        if (DBHelper.getInstance().hasMember(info)) {
                            card_id = maybe_card;
                        }
                    }
                    // if no member match ,just set the attachment back to the father
                    attachment.setCard_id(card_id);
                    DBHelper.getInstance().insertOrUpdateAttachment(attachment);

                    FamilyBase.member member = family.new member();
                    member.setCheck_task_id(Long.toString(time));
                    member.setCysfzh(card_id);
                    DBHelper.getInstance().insertOrUpdateMember(member, BitmapUtils.getBitmapFromByte(content));

                }

                if (bitmapBuf == null) {
                    DBHelper.getInstance().insertOrUpdateFamilyBase(family);
                } else {
                    DBHelper.getInstance().insertOrUpdateFamilyBase(family, BitmapUtils.getBitmapFromByte(bitmapBuf));
                }

            } catch (org.json.JSONException exp) {
                DBHelper.getInstance().deleteAllTasks(checkTaskId);
                return;
            }

            DBHelper.getInstance().deleteDownloadTask(task);
        }

        return;
    }

    private boolean check_download_task() {
        List<DownloadTask> list =  DBHelper.getInstance().getDownloadTask(10000);
        for(int i = 0; i < list.size(); i++) {
            DownloadTask task = list.get(i);
            if (task.getQu_hua_ma() != null) {
                download_quhuama(task);
            } else if(task.getNow_work_target() != null) {
                download_one_item(task);
            }
        }
        return  true;
    }
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }
    private boolean check_upload_task() throws JSONException {
        List<FamilyBase> familyBases = DBHelper.getInstance().getAllUploadingamily(1000);
        for (int i = 0; i < familyBases.size(); i++) {
            FamilyBase familyBase = familyBases.get(i);
            RestResult restResult = new RestResult();
            restResult.statusCode = 404;

            CheckTask checkTask = DBHelper.getInstance().get_check_task(familyBase.getCheck_task_id());
            if (familyBase.getReqid() != null && checkTask.getRpc() != null) {
                restResult = AppContext.getAppContext().getRestClient().GetProgress(
                        familyBase.getReqid(), checkTask.getServer(), checkTask.getRpc());

                if (restResult == null) {
                    continue;
                }
            }
            int status =  0;
            if (restResult.body != null) {
                try {
                    status = restResult.body.getInt("status");
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
            // 如果上传不成功或者没有上传过，那么就需要重新上传
            if (restResult.statusCode != status_ok || status != 1) {
                AppContext context = AppContext.getAppContext();
                RestClient client = context.getRestClient();
                try {
                    restResult = client.UploadFamily(familyBase.ToJSONString());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                if (restResult.body != null && restResult.body.has("token")) {
                    familyBase.setReqid(restResult.body.getString("token"));
                    DBHelper.getInstance().insertOrUpdateFamilyBase(familyBase);
                    checkTask.setRpc(restResult.rpc);
                    checkTask.setServer(restResult.server);
                    DBHelper.getInstance().insertOrUpdateCheckTask(checkTask);
                } else {
                    return true;
                }
                List<Attachment> atts = DBHelper.getInstance().getAllAttachments(familyBase.getSqrsfzh(), familyBase.getCheck_task_id());
                boolean can_continue = true;
                for (int idx = 0; idx < atts.size(); idx++) {
                    Attachment attachment = atts.get(idx);
                    if (appearNumber(attachment.getPath(), "/") < 4) {
                        continue;
                    }
                    // if exit app here, just reminder
                    can_continue = false;
                    try {
                        while (true) {
                            RestResult res = AppContext.getAppContext().getRestClient().
                                    UploadFile(attachment.getPath(),
                                            BitmapUtils.getBitmapByte(attachment.getContent()),
                                            checkTask.getServer(), checkTask.getRpc());
                            if (res.statusCode == 200) {
                                break;
                            }
                            Thread.sleep(TIME_INETRVAL_MS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 已经上传完，设置path为空
                    attachment.setPath("");
                    DBHelper.getInstance().insertOrUpdateAttachment(attachment);

                    can_continue = true;
                }

                if (can_continue == true) {
                    // 已经成功上传，删除所有信息
                    DBHelper.getInstance().deleteAllTasks(familyBase.getCheck_task_id());
                }
            } else {
                // 检查所有附件也已经上传成功
                List<Attachment> atts = DBHelper.getInstance().getAllAttachments(familyBase.getSqrsfzh(), familyBase.getCheck_task_id());
                boolean can_continue = true;
                for (int idx = 0; idx < atts.size(); idx++) {
                    Attachment attachment = atts.get(idx);
                    if (appearNumber(attachment.getPath(), "/") < 4) {
                        continue;
                    }
                    // if exit app here, just reminder
                    can_continue = false;
                    try {
                        while (true) {
                            RestResult res = AppContext.getAppContext().getRestClient().
                                    UploadFile(attachment.getPath(),
                                            BitmapUtils.getBitmapByte(attachment.getContent()),
                                            checkTask.getServer(), checkTask.getRpc());
                            if (res.body != null && res.body.getInt("status") == 0) {
                                break;
                            }

                            Thread.sleep(TIME_INETRVAL_MS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 已经上传完，设置path为空
                    attachment.setPath("");
                    DBHelper.getInstance().insertOrUpdateAttachment(attachment);

                    can_continue = true;
                }
                if (can_continue == true) {
                    // 已经成功上传，删除所有信息
                    DBHelper.getInstance().deleteAllTasks(familyBase.getCheck_task_id());
                }
            }
        }
        return  true;
    }

    private boolean check_qu_hua_ma() {
        boolean download = false;
        long last_time = PreferencesUtils.getInstance().getQuhuamaLastUpdateTime();
        if (last_time == 0 || DBHelper.getInstance().tabbleIsExist(TABLE_QUHUAMA) == false) {
            last_time = System.currentTimeMillis();
            download = true;
        } else if (System.currentTimeMillis() - last_time > update_quhuama_interval_mills) {
            last_time = System.currentTimeMillis();
            download = true;
        }

        if (DBHelper.getInstance().tabbleIsExist(TABLE_QUHUAMA) == true) {
            List<QuHuaMa> result = DBHelper.getInstance().getQuHuaMaWithDepth("2");
            if (result.size() == 0) {
                // no data available
                last_time = System.currentTimeMillis();
                download = true;
            }
        }

        PreferencesUtils.getInstance().setQuhuamaLastUpdateTime(last_time);
        if (download) {
            RestResult restResult = null;
            try {
                // TODO 3->6
                restResult = AppContext.getAppContext().getTaijiClient().GetZoneInfo(hunan_qu_hua_ma, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (restResult.statusCode == status_ok) {
                DBHelper.getInstance().deleteALLQuHuaMa();
                try {
                    JSONObject obj = restResult.body;
                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        String name =jsonObject.getString("xzqhmc");
                        String code =jsonObject.getString("xzqhdm");
                        String depth =jsonObject.getString("xzqhjb");
                        String father_code = "";
                        if (depth.equals("2")){
                            father_code = code;
                        } else if (depth.equals("3")){
                            father_code = code.substring(0,2) + "0000000000";
                        } else if (depth.equals("4")) {
                            father_code = code.substring(0,4) + "00000000";
                        } else if (depth.equals("5")) {
                            father_code = code.substring(0,6) + "000000";
                        } else if (depth.equals("6")) {
                            father_code = code.substring(0,9) + "000";
                        }
                        QuHuaMa quHuaMa = new QuHuaMa();
                        quHuaMa.setDepth(depth);
                        quHuaMa.setName(name);
                        quHuaMa.setId(code);
                        quHuaMa.setFather_id(father_code);
                        DBHelper.getInstance().insertQuHuaMa(quHuaMa);
                    }
                } catch (org.json.JSONException exp) {
                }
            }
        }

        return true;
    }
    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    public final void exit() {
        mReady = false;
    }

    public WorkerThread(Context context) {
        this.mContext = context;
    }
}
