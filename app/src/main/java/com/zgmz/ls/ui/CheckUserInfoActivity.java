package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.InfoType;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.dialog.ListDialog;
import com.zgmz.ls.dialog.ListItem;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.FingerPrint;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.BitmapUtils;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.zgmz.ls.R.id.bao_cun_cheng_yuan;
import static com.zgmz.ls.R.id.id_username;
import static com.zgmz.ls.model.Attachment.TYPE_IMAGE_SHENGFENZHEN;
import static com.zgmz.ls.ui.LoginActivity.TEST_XZBM;

public class CheckUserInfoActivity extends SubActivity implements OnClickListener {


    private TextView mName;

    private TextView mNianLing;
    private TextView mIDNumber;

    private ImageView mAvatar;

    private ImageView mImageID;
    private ImageView mImageFingerPrint;
    private ImageView mImageYearPhoto;

    private ImageView mImageSign;
    private ImageView mImageAttachment;
    private ImageView mGPS;
    private ImageView mPreview;
    private ImageView mStore;

    private EditText xing_ming;
    private EditText nian_ling;
    private EditText shen_feng_zheng;
    private Button xing_bie;
    private Button jian_kang_qing_kang;
    private Button can_ji_lei_bie;
    private Button can_ji_deng_ji;
    private Button ren_yuan_zhuang_tai;
    private Button wen_hua_cheng_du;
    private Button shen_qing_ren_guan_xi;
    private Button lao_dong_neng_li;
    private ImageView shi_fou_zai_xiao;
    private ImageView shi_fou_cai_ji_shen_fen_zheng;
    private ImageView shi_fou_he_cha_hu_kou_ben;

    private UserInfo mUserInfo;
    public static int IdNOToAge(String IdNO){
        int leh = IdNO.length();
        String dates="";
        if (leh == 18) {
            int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(new Date());
            int u=Integer.parseInt(year)-Integer.parseInt(dates);
            return u;
        }else if (leh == 15) {
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }

            return 0;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cheng_yuan_xin_xi);
        setupViews();
        onNewIntent(getIntent());
    }
    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("成员信息");
        //hideTitleBar();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateUserInfo();
    }

    SimpleUserInfo local_simple_info;

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        local_simple_info = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
        if (local_simple_info == null) {
            ToastUtils.showLongToast("用户基本信息不能为空！");
            finish();
        }
        mFamilyMemberInfo = null;
        if (local_simple_info.getIdNumber() != null) {
            mFamilyMemberInfo = DBHelper.getInstance().getOneUncheckedMember(local_simple_info.getIdNumber(), local_simple_info.getCheck_task_id());
        }
        if(mFamilyMemberInfo == null) {
            FamilyBase base = new FamilyBase();
            mFamilyMemberInfo = base.new member();
            mFamilyMemberInfo.setCheck_task_id(local_simple_info.getCheck_task_id());
        } else {
            if (mFamilyMemberInfo.getXb() != null) {
                if (mFamilyMemberInfo.getXb().equals("1")) {
                    xing_bie.setText("男");
                } else if (mFamilyMemberInfo.getXb().equals("2")) {
                    xing_bie.setText("女");
                }
            }
            if (mFamilyMemberInfo.getLdnl() != null) {
                if (mFamilyMemberInfo.getLdnl().equals("01")) {
                    lao_dong_neng_li.setText("有劳动能力");
                } else if (mFamilyMemberInfo.getLdnl().equals("02")) {
                    lao_dong_neng_li.setText("部分丧失劳动能力");
                } else if (mFamilyMemberInfo.getLdnl().equals("03")) {
                    lao_dong_neng_li.setText("完全丧失劳动能力");
                } else if (mFamilyMemberInfo.getLdnl().equals("04")) {
                    lao_dong_neng_li.setText("无劳动能力");
                }
            }
            if (mFamilyMemberInfo.getJkzk() != null) {
                if (mFamilyMemberInfo.getJkzk().equals("10")) {
                    jian_kang_qing_kang.setText("健康或良好");
                } else if (mFamilyMemberInfo.getJkzk().equals("20")) {
                    jian_kang_qing_kang.setText("一般或较弱");
                } else if (mFamilyMemberInfo.getJkzk().equals("40")) {
                    jian_kang_qing_kang.setText("重病");
                }
            }
            if (mFamilyMemberInfo.getCjlb() != null) {
                if (mFamilyMemberInfo.getCjlb().equals("61")) {
                    can_ji_lei_bie.setText("视力残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("62")) {
                    can_ji_lei_bie.setText("听力残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("63")) {
                    can_ji_lei_bie.setText("言语残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("64")) {
                    can_ji_lei_bie.setText("肢体残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("65")) {
                    can_ji_lei_bie.setText("智力残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("66")) {
                    can_ji_lei_bie.setText("精神残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("67")) {
                    can_ji_lei_bie.setText("多重残疾");
                } else if (mFamilyMemberInfo.getCjlb().equals("69")) {
                    can_ji_lei_bie.setText("其他残疾");
                }
            }
            if (mFamilyMemberInfo.getCjdj() != null) {
                if (mFamilyMemberInfo.getCjdj().equals("01")) {
                    can_ji_deng_ji.setText("一级残疾");
                } else if (mFamilyMemberInfo.getCjdj().equals("02")) {
                    can_ji_deng_ji.setText("二级残疾");
                } else if (mFamilyMemberInfo.getCjdj().equals("03")) {
                    can_ji_deng_ji.setText("三级残疾");
                } else if (mFamilyMemberInfo.getCjdj().equals("04")) {
                    can_ji_deng_ji.setText("四级残疾");
                }
            }
            if (mFamilyMemberInfo.getWhcd() != null) {
                if (mFamilyMemberInfo.getWhcd().equals("10")) {
                    wen_hua_cheng_du.setText("研究生");
                } else if (mFamilyMemberInfo.getWhcd().equals("20")) {
                    wen_hua_cheng_du.setText("大学本科");
                } else if (mFamilyMemberInfo.getWhcd().equals("30")) {
                    wen_hua_cheng_du.setText("专科");
                } else if (mFamilyMemberInfo.getWhcd().equals("40")) {
                    wen_hua_cheng_du.setText("中等职业");
                } else if (mFamilyMemberInfo.getWhcd().equals("50")) {
                    wen_hua_cheng_du.setText("普通高级中学");
                } else if (mFamilyMemberInfo.getWhcd().equals("60")) {
                    wen_hua_cheng_du.setText("初级中学");
                } else if (mFamilyMemberInfo.getWhcd().equals("70")) {
                    wen_hua_cheng_du.setText("小学");
                } else if (mFamilyMemberInfo.getWhcd().equals("90")) {
                    wen_hua_cheng_du.setText("其他");
                }
            }

            if (mFamilyMemberInfo.getSfzx() != null) {
                if (mFamilyMemberInfo.getSfzx().equals("01")) {
                    shi_fou_zai_xiao.setImageResource(R.drawable.check_box_true_2);
                    _shi_fou_zai_xiao = 1;
                } else if (mFamilyMemberInfo.getSfzx().equals("02")) {
                    shi_fou_zai_xiao.setImageResource(R.drawable.check_box_false_2);
                    _shi_fou_zai_xiao = 0;
                }
            }
            if (mFamilyMemberInfo.getHchkb() != null) {
                if (mFamilyMemberInfo.getHchkb().equals("01")) {
                    shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_true_2);
                    _id_shifouyihechahukouben = 1;
                } else if (mFamilyMemberInfo.getHchkb().equals("02")) {
                    shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_false_2);
                    _id_shifouyihechahukouben = 0;
                }
            }
            if (mFamilyMemberInfo.getHcsfz() != null) {
                if (mFamilyMemberInfo.getHcsfz().equals("01")) {
                    shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_true_2);
                    _id_shifouyicaijishenfenzhen = 1;
                } else if (mFamilyMemberInfo.getHcsfz().equals("02")) {
                    shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_false_2);
                    _id_shifouyicaijishenfenzhen = 0;
                }
            }
            if (mFamilyMemberInfo.getRyzt() != null) {
                if (mFamilyMemberInfo.getRyzt().equals("01")) {
                    ren_yuan_zhuang_tai.setText("正常");
                } else if (mFamilyMemberInfo.getRyzt().equals("02")) {
                    ren_yuan_zhuang_tai.setText("失踪");
                } else if (mFamilyMemberInfo.getRyzt().equals("03")) {
                    ren_yuan_zhuang_tai.setText("死亡");
                } else if (mFamilyMemberInfo.getRyzt().equals("04")) {
                    ren_yuan_zhuang_tai.setText("嫁娶");
                } else if (mFamilyMemberInfo.getRyzt().equals("05")) {
                    ren_yuan_zhuang_tai.setText("狱中服刑");
                } else if (mFamilyMemberInfo.getRyzt().equals("06")) {
                    ren_yuan_zhuang_tai.setText("分户迁出");
                } else if (mFamilyMemberInfo.getRyzt().equals("99")) {
                    ren_yuan_zhuang_tai.setText("其他");
                }
            }

            if (mFamilyMemberInfo.getYsqrgx() != null) {
                if (mFamilyMemberInfo.getYsqrgx().equals("01")) {
                    shen_qing_ren_guan_xi.setText("本人");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("10")) {
                    shen_qing_ren_guan_xi.setText("配偶");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("20")) {
                    shen_qing_ren_guan_xi.setText("子");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("30")) {
                    shen_qing_ren_guan_xi.setText("女");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("40")) {
                    shen_qing_ren_guan_xi.setText("孙子、孙女或外孙子、外孙女");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("50")) {
                    shen_qing_ren_guan_xi.setText("父母");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("60")) {
                    shen_qing_ren_guan_xi.setText("祖父母或外祖父母");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("70")) {
                    shen_qing_ren_guan_xi.setText("兄弟姐妹");
                } else if (mFamilyMemberInfo.getYsqrgx().equals("99")) {
                    shen_qing_ren_guan_xi.setText("其他");
                }
            }
        }
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (local_simple_info.getIdNumber() != null) {
            mUserInfo = DBHelper.getInstance().getOneUncheckedMember(local_simple_info.getIdNumber(),
                    local_simple_info.getCheck_task_id(), true);
            updateUserBaseInfo(mUserInfo);
        } else {
            mUserInfo = new UserInfo();
            mUserInfo.setUserId(local_simple_info.getUserId());
            mUserInfo.setCheck_task_id(local_simple_info.getCheck_task_id());
        }
    }

    private void updateUserBaseInfo(UserInfo info) {
        if (info == null) return;
        mName.setText(info.getName());
        mIDNumber.setText(info.getIdNumber());
        if (info.getAvatar()!= null) {
            mAvatar.setImageBitmap(info.getAvatar());
        }
        if (!mName.getText().toString().equals("")) {
            xing_ming.setText(info.getName());
        }
        if (!mIDNumber.getText().toString().equals("")) {
            shen_feng_zheng.setText(info.getIdNumber());
            nian_ling.setText(String.valueOf(IdNOToAge(info.getIdNumber())));
        }
    }


    private void updateAllInfosState() {
        UserInfo info = mUserInfo;
        updateIdCardState(info.isFlagId());
        updateFingerState(info.isFlagFinger());
        updateYearPhotoState(info.isFlagYearPhoto());
        updateSignState(info.isFlagSignature());
        updateAttachmentState(info.isFlagAttachment());
    }


    private void updateIdCardState(boolean flag) {
        if (flag) {
            mImageID.setImageResource(R.drawable.icon_card_on);
        } else {
            mImageID.setImageResource(R.drawable.icon_card_off);
        }
    }

    private void updateFingerState(boolean flag) {
        if (flag) {
            mImageFingerPrint.setImageResource(R.drawable.icon_fingerprint_on);
        } else {
            mImageFingerPrint.setImageResource(R.drawable.icon_fingerprint_off);
        }
    }

    private void updateYearPhotoState(boolean flag) {
        if (flag) {
            mImageYearPhoto.setImageResource(R.drawable.icon_camera_on);
        } else {
            mImageYearPhoto.setImageResource(R.drawable.icon_camera_off);
        }
    }

    private void updateSignState(boolean flag) {
        if (flag) {
            mImageSign.setImageResource(R.drawable.icon_sign_on);
        } else {
            mImageSign.setImageResource(R.drawable.icon_sign_off);
        }
    }

    private void updateAttachmentState(boolean flag) {
        if (flag) {
            mImageAttachment.setImageResource(R.drawable.icon_attachment_on);
        } else {
            mImageAttachment.setImageResource(R.drawable.icon_attachment_off);
        }
    }

    protected void setupViews() {
        // TODO Auto-generated method stub
        mAvatar = (ImageView) this.findViewById(R.id.avatar);
        mName = (TextView) this.findViewById(R.id.name);
        mIDNumber = (TextView) this.findViewById(R.id.id_number);
        mImageID = (ImageView) this.findViewById(R.id.er_dai_zheng);
        mImageID.setOnClickListener(this);

        mImageFingerPrint = (ImageView) this.findViewById(R.id.zhiwenduibi);
        mImageFingerPrint.setOnClickListener(this);

        mImageYearPhoto = (ImageView) this.findViewById(R.id.ben_ci_zhao_pian_pai_she);
        mImageYearPhoto.setOnClickListener(this);

        mImageAttachment = (ImageView) this.findViewById(R.id.qi_ta_zhao_pian);
        mImageAttachment.setOnClickListener(this);

        mGPS = (ImageView) this.findViewById(R.id.gps);
        mGPS.setOnClickListener(this);

        mPreview = (ImageView) this.findViewById(R.id.yu_lan_chen_yuan);
        mPreview.setOnClickListener(this);

        mImageSign = (ImageView) this.findViewById(R.id.qianming);
        mImageSign.setOnClickListener(this);

        mStore = (ImageView) this.findViewById(R.id.bao_cun_cheng_yuan);
        mStore.setOnClickListener(this);

        xing_ming = (EditText)this.findViewById(R.id.id_username);
        nian_ling = (EditText)this.findViewById(R.id.nianling);
        shen_feng_zheng = (EditText)this.findViewById(R.id.id_shengfenzheng);

        xing_bie = (Button)this.findViewById(R.id.xing_bie);
        jian_kang_qing_kang = (Button)this.findViewById(R.id.id_jiankangqingkuang);
        can_ji_lei_bie = (Button)this.findViewById(R.id.canjileibie);
        ren_yuan_zhuang_tai  = (Button)this.findViewById(R.id.ren_yuan_zhuang_tai);
        can_ji_deng_ji  = (Button)this.findViewById(R.id.id_canjidengji);
        wen_hua_cheng_du = (Button)this.findViewById(R.id.id_wenhuachengdu);
        shen_qing_ren_guan_xi = (Button)this.findViewById(R.id.id_yushengqingrenguanxi);
        lao_dong_neng_li = (Button)this.findViewById(R.id.lao_dong_neng_li);

        shi_fou_zai_xiao = (ImageView) this.findViewById(R.id.id_shifouzaixiao);
        shi_fou_he_cha_hu_kou_ben = (ImageView)this.findViewById(R.id.id_shifouyihechahukouben);
        shi_fou_cai_ji_shen_fen_zheng = (ImageView)this.findViewById(R.id.id_shifouyicaijishenfenzhen);

        xing_bie.setOnClickListener(this);
        jian_kang_qing_kang.setOnClickListener(this);
        can_ji_lei_bie.setOnClickListener(this);
        wen_hua_cheng_du.setOnClickListener(this);
        shen_qing_ren_guan_xi.setOnClickListener(this);
        ren_yuan_zhuang_tai.setOnClickListener(this);
        can_ji_deng_ji.setOnClickListener(this);
        lao_dong_neng_li.setOnClickListener(this);


        shi_fou_zai_xiao.setOnClickListener(this);
        shi_fou_he_cha_hu_kou_ben.setOnClickListener(this);
        shi_fou_cai_ji_shen_fen_zheng.setOnClickListener(this);


    }

    boolean change_sf_hc_hkb = false;
    boolean change_sf_hc_sfz = false;
    boolean change_sf_zx = false;
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.yu_lan_chen_yuan:
                preview();
                break;
            case R.id.er_dai_zheng:
                recognizeIdCard();
                break;
            case R.id.zhiwenduibi:
                inputFingerPring();
                break;
            case R.id.ben_ci_zhao_pian_pai_she:
                inputYearPhoto();
                break;
            case R.id.bao_cun_cheng_yuan:
                bao_cun();
                break;
            case R.id.qianming:
                inputSignature();
                break;
            case R.id.qi_ta_zhao_pian:
                addAttachment();
                break;
            case R.id.gps:
                gps();
                break;
            case R.id.xing_bie:
                show_xing_bie();
                break;
            case R.id.id_jiankangqingkuang:
                jiankangqingkuang();
                break;
            case R.id.canjileibie:
                canjileibie();
                break;
            case R.id.id_canjidengji:
                canjidengji();
                break;
            case R.id.id_wenhuachengdu:
                id_wenhuachengdu();
                break;
            case R.id.id_yushengqingrenguanxi:
                id_yushengqingrenguanxi();
                break;
            case R.id.id_shifouzaixiao:
                change_sf_zx = true;
                id_shifouzaixiao();
                break;
            case R.id.id_shifouyihechahukouben:
                change_sf_hc_hkb = true;
                id_shifouyihechahukouben();
                break;
            case R.id.id_shifouyicaijishenfenzhen:
                change_sf_hc_sfz = true;
                id_shifouyicaijishenfenzhen();
                break;
            case R.id.ren_yuan_zhuang_tai:
                on_ren_yuan_zhuang_tai();
                break;

            case R.id.lao_dong_neng_li:
                on_lao_dong_neng_li();
                break;
        }
    }

    int choice = 0;
    int type = 0;
    List<ListItem> item_list = new ArrayList<ListItem>();
    private void showSingleChoiceDialog(List<String> list){
        item_list.clear();
        for (int i = 0; i < list.size(); i++) {
            ListItem list_item = new ListItem();
            list_item.name = list.get(i).toString();
            item_list.add(i, list_item);
        }

        ListDialog listDialog = new ListDialog(this) ;
        listDialog.setListItem(item_list);
        WindowManager.LayoutParams params = listDialog.getWindow()
                .getAttributes();
        params.width = 1000;
        params.height = 650;
        listDialog.getWindow().setAttributes(params);
        listDialog.setOnDialogItemClickListener(new ListDialog.OnDialogItemClickListener() {

            @Override
            public void onItemClick(int id, Object data) {
                if (id == -1) {
                    return;
                }
                choice = id;
                if (type == 6) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setXb("1");
                    } else {
                        mFamilyMemberInfo.setXb("2");
                    }

                    xing_bie.setText(item_list.get(choice).name);
                } else if(type == 7) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setJkzk("10");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setJkzk("20");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setJkzk("40");
                    }

                    jian_kang_qing_kang.setText(item_list.get(choice).name);
                } else if(type == 8) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setCjlb("61");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setCjlb("62");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setCjlb("63");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setCjlb("64");
                    } else if (choice == 4) {
                        mFamilyMemberInfo.setCjlb("65");
                    } else if (choice == 5) {
                        mFamilyMemberInfo.setCjlb("66");
                    } else if (choice == 6) {
                        mFamilyMemberInfo.setCjlb("67");
                    } else if (choice == 7) {
                        mFamilyMemberInfo.setCjlb("69");
                    }

                    can_ji_lei_bie.setText(item_list.get(choice).name);
                } else if(type == 9) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setCjdj("01");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setCjdj("02");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setCjdj("03");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setCjdj("04");
                    }

                    can_ji_deng_ji.setText(item_list.get(choice).name);
                } else if(type == 10) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setWhcd("10");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setWhcd("20");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setWhcd("30");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setWhcd("40");
                    } else if (choice == 4) {
                        mFamilyMemberInfo.setWhcd("50");
                    } else if (choice == 5) {
                        mFamilyMemberInfo.setWhcd("60");
                    } else if (choice == 6) {
                        mFamilyMemberInfo.setWhcd("70");
                    } else if (choice == 7) {
                        mFamilyMemberInfo.setWhcd("70");
                    }

                    wen_hua_cheng_du.setText(item_list.get(choice).name);
                }  else if(type == 11) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setLdnl("01");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setLdnl("02");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setLdnl("03");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setLdnl("04");
                    }

                    lao_dong_neng_li.setText(item_list.get(choice).name);
                } else if(type == 12) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setYsqrgx("01");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setYsqrgx("10");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setYsqrgx("20");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setYsqrgx("30");
                    } else if (choice == 4) {
                        mFamilyMemberInfo.setYsqrgx("40");
                    } else if (choice == 5) {
                        mFamilyMemberInfo.setYsqrgx("50");
                    } else if (choice == 6) {
                        mFamilyMemberInfo.setYsqrgx("60");
                    } else if (choice == 7) {
                        mFamilyMemberInfo.setYsqrgx("70");
                    } else if (choice == 8) {
                        mFamilyMemberInfo.setYsqrgx("99");
                    }
                    shen_qing_ren_guan_xi.setText(item_list.get(choice).name);
                } else if(type == 13) {
                    if (choice == 0) {
                        mFamilyMemberInfo.setRyzt("01");
                    } else if (choice == 1) {
                        mFamilyMemberInfo.setRyzt("02");
                    } else if (choice == 2) {
                        mFamilyMemberInfo.setRyzt("03");
                    } else if (choice == 3) {
                        mFamilyMemberInfo.setRyzt("04");
                    } else if (choice == 4) {
                        mFamilyMemberInfo.setRyzt("05");
                    } else if (choice == 5) {
                        mFamilyMemberInfo.setRyzt("06");
                    } else if (choice == 6) {
                        mFamilyMemberInfo.setRyzt("99");
                    }
                    ren_yuan_zhuang_tai.setText(item_list.get(choice).name);
                }
            }
        });
        listDialog.show();
    }


    private FamilyBase.member mFamilyMemberInfo;
    void bao_cun() {

        if(xing_ming.getText().toString().equals("") ||
                shen_feng_zheng.getText().toString().equals("") ) {
            ToastUtils.showShortToast("error input name and id number");
            return;
        }

        mFamilyMemberInfo.setCyxm(xing_ming.getText().toString().trim());
        mFamilyMemberInfo.setCysfzh(shen_feng_zheng.getText().toString().trim());
        if (!nian_ling.getText().toString().equals("")) {
            mFamilyMemberInfo.setNl(nian_ling.getText().toString().trim());
        }
        mFamilyMemberInfo.setFather_id(local_simple_info.getFather_card_id());
        if (change_sf_zx) {
            if (_shi_fou_zai_xiao == 1) {
                mFamilyMemberInfo.setSfzx("01");
            } else {
                mFamilyMemberInfo.setSfzx("02");
            }
        }
        if (change_sf_hc_hkb) {
            if (_id_shifouyihechahukouben == 1) {
                mFamilyMemberInfo.setHchkb("01");
            } else {
                mFamilyMemberInfo.setHchkb("02");
            }
        }
        if (change_sf_hc_sfz) {
            if (_id_shifouyicaijishenfenzhen == 1) {
                mFamilyMemberInfo.setHcsfz("01");
            } else {
                mFamilyMemberInfo.setHcsfz("02");
            }
        }

        if (mFamilyMemberInfo.getYsqrgx() == null) {
            ToastUtils.showLongToast("未设置和申请人关系");
            return;
        }
        if (mFamilyMemberInfo.getCyxm() == null) {
            ToastUtils.showLongToast("成员姓名为空");
            return;
        }
        if (mFamilyMemberInfo.getCysfzh() == null) {
            ToastUtils.showLongToast("成员身份证为空");
            return;
        }
        if (mFamilyMemberInfo.getNl() == null) {
            ToastUtils.showLongToast("成员年龄为空");
            return;
        }
        if (mFamilyMemberInfo.getRyzt() == null) {
            ToastUtils.showLongToast("未设置成员状态");
            return;
        }
        if (mFamilyMemberInfo.getHchkb() == null) {
            ToastUtils.showLongToast("未设置是否核查户口本");
            return;
        }

        if (mFamilyMemberInfo.getHcsfz() == null) {
            ToastUtils.showLongToast("未设置是否核查身份证");
            return;
        }


        boolean ret = false;
        /*
        if (attachment != null) {
            DBHelper.getInstance().insertOrUpdateAttachment(attachment);
            ret = DBHelper.getInstance().insertOrUpdateMember(mFamilyMemberInfo,
                    pic);
        } else */
        {
            ret = DBHelper.getInstance().insertOrUpdateMember(mFamilyMemberInfo);
        }
        if(ret == true) {
            finish();
            ToastUtils.showLongToast("保存成功");
        }
        else {
            ToastUtils.showLongToast("保存失败");
        }
        finish();
    }

    int _id_shifouyicaijishenfenzhen = 1;
    void id_shifouyicaijishenfenzhen() {
        if (_id_shifouyicaijishenfenzhen == 0) {
            _id_shifouyicaijishenfenzhen = 1;
            shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_true_2);
        } else {
            _id_shifouyicaijishenfenzhen = 0;
            shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_false_2);
        }
    }

    int _id_shifouyihechahukouben = 1;
    void id_shifouyihechahukouben() {
        if (_id_shifouyihechahukouben == 0) {
            _id_shifouyihechahukouben = 1;
            shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_true_2);
        } else {

            _id_shifouyihechahukouben = 0;
            shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_false_2);
        }
    }

    int _shi_fou_zai_xiao = 1;
    void id_shifouzaixiao() {
        if (_shi_fou_zai_xiao == 0) {
            _shi_fou_zai_xiao = 1;
            shi_fou_zai_xiao.setImageResource(R.drawable.check_box_true_2);
        } else {

            _shi_fou_zai_xiao = 0;
            shi_fou_zai_xiao.setImageResource(R.drawable.check_box_false_2);
        }
    }

    void on_ren_yuan_zhuang_tai() {
        List<String> strList = new ArrayList<String>();
        strList.add("正常");
        strList.add("失踪");
        strList.add("死亡");
        strList.add("嫁娶");
        strList.add("狱中服刑");
        strList.add("分户迁出");
        strList.add("其他");
        showSingleChoiceDialog(strList);
        type = 13;
    }

    void id_yushengqingrenguanxi() {
        List<String> strList = new ArrayList<String>();
        strList.add("本人");
        strList.add("配偶");
        strList.add("子");
        strList.add("女");
        strList.add("孙子、孙女或外孙子、外孙女");
        strList.add("父母");
        strList.add("祖父母或外祖父母");
        strList.add("兄弟姐妹");
        strList.add("其他");
        showSingleChoiceDialog(strList);
        type = 12;
    }


    void on_lao_dong_neng_li() {
        List<String> strList = new ArrayList<String>();
        strList.add("有劳动能力");
        strList.add("部分丧失劳动能力");
        strList.add("完全丧失劳动能力");
        strList.add("无劳动能力");
        showSingleChoiceDialog(strList);
        type = 11;
    }
    void id_wenhuachengdu() {


        List<String> strList = new ArrayList<String>();
        strList.add("研究生");
        strList.add("大学本科");
        strList.add("专科");
        strList.add("中等职业");
        strList.add("普通高级中学");
        strList.add("初级中学");
        strList.add("小学");
        strList.add("其他");
        showSingleChoiceDialog(strList);
        type = 10;
    }
    void canjidengji() {


        List<String> strList = new ArrayList<String>();
        strList.add("一级残疾");
        strList.add("二级残疾");
        strList.add("三级残疾");
        strList.add("四级残疾");
        showSingleChoiceDialog(strList);
        type = 9;
    }
    void canjileibie () {


        List<String> strList = new ArrayList<String>();
        strList.add("视力残疾");
        strList.add("听力残疾");
        strList.add("言语残疾");
        strList.add("肢体残疾");
        strList.add("智力残疾");
        strList.add("精神残疾");
        strList.add("多重残疾");
        strList.add("其他残疾");
        showSingleChoiceDialog(strList);
        type = 8;
    }

    void jiankangqingkuang() {

        List<String> strList = new ArrayList<String>();
        strList.add("健康或良好");
        strList.add("一般或较弱");
        strList.add("重病");
        showSingleChoiceDialog(strList);
        type = 7;
    }

    void show_xing_bie() {

        List<String> strList = new ArrayList<String>();
        strList.add("男");
        strList.add("女");
        showSingleChoiceDialog(strList);
        type = 6;
    }


    private static final int REQUEST_CODE_RECONGNIZE = 2001;

    private static final int REQUEST_CODE_FINGERPRINT = 2002;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RECONGNIZE) {
            if (resultCode == Activity.RESULT_OK) {
                String key = data.getStringExtra(Const.KEY_ID_CARD);
                if (key != null) {
                    IdCard idcard = (IdCard) SharedDatas.getInstance().getValue(key);
                    if (idcard != null) {
                        if (local_simple_info.getIdNumber() == null) {

                        } else if (idcard.getIdNumber().equals(mUserInfo.getIdNumber())) {
                            idcard.setUserId(mUserInfo.getUserId());
                            saveIdCard(idcard);
                        } else {
                            ToastUtils.showLongToast("身份证号不一致，无法保存");
                            return;
                        }


                            Long time = System.currentTimeMillis();
                            Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy-MM-dd");
                            Date curDate = new Date(time);//获取当前时间
                            String str =  formatter.format(curDate);
                            FamilyBase family = new FamilyBase();
                            FamilyBase.member member = family.new member();
                            member.setCheck_task_id(local_simple_info.getCheck_task_id());
                            member.setCyxm(idcard.getName());
                            member.setCysfzh(idcard.getIdNumber());
                            if (idcard.getSex().equals("男")) {
                                member.setXb("1");
                            } else {
                                member.setXb("2");
                            }
                            byte[] bmp = idcard.getWlt();
                            Bitmap pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
                            DBHelper.getInstance().insertOrUpdateMember(member, pic);

                            Attachment attachment = new Attachment();
                            attachment.setCheck_task_id(local_simple_info.getCheck_task_id());
                            attachment.setCard_id(idcard.getIdNumber());
                            bmp = idcard.getWlt();
                            pic = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
                            attachment.setContent(pic);
                            attachment.setName(idcard.getIdNumber() + "-" + "102.jpg");
                            attachment.setType(TYPE_IMAGE_SHENGFENZHEN);
                            attachment.setPath(str + "/" + TEST_XZBM + "/"+ idcard.getIdNumber() + "/102/"+ idcard.getIdNumber() + "-102.jpg");
                            DBHelper.getInstance().insertAttachment(attachment);

                        local_simple_info.setName(idcard.getName());
                        local_simple_info.setIdNumber(idcard.getIdNumber());
                        updateUserInfo();
                    }

                }
            }
        } else if (requestCode == REQUEST_CODE_FINGERPRINT) {
            if (resultCode == Activity.RESULT_OK) {
                String key = data.getStringExtra(Const.KEY_FINGER_PRINT);
                if (key != null) {
                    FingerPrint finger = (FingerPrint) SharedDatas.getInstance().getValue(key);
                    if (finger != null && mUserInfo != null) {
                        finger.setUserId(mUserInfo.getUserId());
                        saveFingerPrint(finger);
                    }

                }
            }
        }
    }

    private void saveIdCard(IdCard idCard) {
        if (!DBHelper.getInstance().insertOrUpdateIdCard(idCard)) {
            updateUserInfo();
        }
    }

    private void saveFingerPrint(FingerPrint finger) {
        if (DBHelper.getInstance().insertOrUpdateFingerPrint(finger)) {
            mUserInfo.setFlagFinger(true);
            updateFingerState(true);
        }
    }

    public void updateItemIcon(ImageView image, int resid) {
        image.setImageResource(resid);
    }

    private void preview() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, DBMemberPreviewActivity.class);
        startActivity(intent);
    }

    private void recognizeIdCard() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, IDRecoginzeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_RECONGNIZE);
    }

    private void inputFingerPring() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, FingerprintCheckActivitiy.class);
        startActivityForResult(intent, REQUEST_CODE_FINGERPRINT);
    }

    private void inputYearPhoto() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.putExtra(Const.KEY_ATTACHMENT_TYPE, String.valueOf(Attachment.TYPE_IMAGE));
        intent.setClass(this, YearPhotoActivity.class);
        startActivity(intent);
    }

    private void gps() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, DistrictActivity.class);
        startActivity(intent);
    }

    private void inputOtherSituation() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, FamilyMemberSituationActivity.class);
        startActivity(intent);
    }

    private void inputSignature() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, local_simple_info);
        intent.setClass(this, SignatureActivity.class);
        startActivity(intent);
    }

    private void addAttachment() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, UserInfo.getSimpleUserInfo(mUserInfo));
        intent.setClass(this, AttachmentActivity.class);
        startActivity(intent);
    }

    private void deleteUser() {

        DBHelper.getInstance().deleteHCMember(local_simple_info.getIdNumber(), local_simple_info.getCheck_task_id());
        finish();
    }
}