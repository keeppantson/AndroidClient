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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CheckUserInfoActivity extends SubActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {


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

    private TextView chengyuanxingbie_1;
    private TextView chengyuanxingbie_2;
    private TextView lao_dong_neng_li_1;
    private TextView lao_dong_neng_li_2;
    private TextView jiankangqingkuang_1;
    private TextView jiankangqingkuang_2;
    private TextView id_canjileibie_1;
    private TextView id_canjileibie_2;
    private TextView canjidengji_1;
    private TextView canjidengji_2;
    private TextView ren_yuan_zhuang_tai_1;
    private TextView ren_yuan_zhuang_tai_2;
    private TextView wenjiachengdu_1;
    private TextView wenjiachengdu_2;
    private TextView yushengqingrenguanxi_1;
    private TextView yushengqingrenguanxi_2;

    private EditText xing_ming;
    private EditText nian_ling;
    private String nian_ling_str;
    private EditText shen_feng_zheng;
    private Button xing_bie;
    private Button jian_kang_qing_kang;
    private Button can_ji_lei_bie;
    private Button can_ji_deng_ji;
    private Button ren_yuan_zhuang_tai;
    private Button wen_hua_cheng_du;
    private Button shen_qing_ren_guan_xi;
    private Button lao_dong_neng_li;
    private ImageView er_dai_shen_fen_zhen;
    CheckBox sfzx_yes;
    CheckBox sfzx_no;
    CheckBox sfhzhkb_yes;
    CheckBox sfhzhkb_no;
    CheckBox sfhzsfz_yes;
    CheckBox sfhzsfz_no;


    String year;
    String month;
    String date;
    public int get_birth_day(String sfzid){
        String id = sfzid;
        String regex = "^\\d{6}(\\d{4})(\\d{2})(\\d{2})\\d{3}(X|\\d)$";
        if(!id.matches(regex)){
            System.err.println("输入的身份证号码不符合规格");
            return -1;
        }else{
            year = id.replaceAll(regex, "$1");
            month = id.replaceAll(regex, "$2");
            date = id.replaceAll(regex, "$3");
        }
        return 0;
    }

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
                } else {
                    can_ji_lei_bie.setText("无残疾");
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
                } else {
                    can_ji_lei_bie.setText("无残疾");
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
                    //shi_fou_zai_xiao.setImageResource(R.drawable.check_box_true_2);
                    sfzx_yes.setChecked(true);
                    sfzx_no.setChecked(false);
                    _shi_fou_zai_xiao = 1;
                } else if (mFamilyMemberInfo.getSfzx().equals("02")) {
                    //shi_fou_zai_xiao.setImageResource(R.drawable.check_box_false_2);
                    sfzx_yes.setChecked(false);
                    sfzx_no.setChecked(true);
                    _shi_fou_zai_xiao = 0;
                }
            }
            if (mFamilyMemberInfo.getHchkb() != null) {
                if (mFamilyMemberInfo.getHchkb().equals("01")) {
                    //shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_true_2);
                    sfhzhkb_yes.setChecked(true);
                    sfhzhkb_no.setChecked(false);
                    _id_shifouyihechahukouben = 1;
                } else if (mFamilyMemberInfo.getHchkb().equals("02")) {
                    //shi_fou_he_cha_hu_kou_ben.setImageResource(R.drawable.check_box_false_2);
                    sfhzhkb_yes.setChecked(false);
                    sfhzhkb_no.setChecked(true);
                    _id_shifouyihechahukouben = 0;
                }
            }
            if (mFamilyMemberInfo.getHcsfz() != null) {
                if (mFamilyMemberInfo.getHcsfz().equals("01")) {
                    //shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_true_2);
                    sfhzsfz_yes.setChecked(true);
                    sfhzsfz_no.setChecked(false);
                    _id_shifouyicaijishenfenzhen = 1;
                } else if (mFamilyMemberInfo.getHcsfz().equals("02")) {
                    //shi_fou_cai_ji_shen_fen_zheng.setImageResource(R.drawable.check_box_false_2);
                    sfhzsfz_yes.setChecked(false);
                    sfhzsfz_no.setChecked(true);
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
            int ret = get_birth_day(info.getIdNumber());
            if (ret == -1) {
                nian_ling.setText("无效年龄");
            } else {
                nian_ling.setText(year + "-" + month + "-" + date);
                nian_ling_str = String.valueOf(IdNOToAge(info.getIdNumber()));
            }
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
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String str = shen_feng_zheng.getText().toString();
            int ret = get_birth_day(str);
            if (ret == -1) {
                nian_ling.setText("无效年龄");
            } else {
                nian_ling.setText(year + "-" + month + "-" + date);
                nian_ling_str = String.valueOf(IdNOToAge(str));
            }

        }
    };
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
        er_dai_shen_fen_zhen = (ImageView) this.findViewById(R.id.er_dai_shen_fen_zhen);

        chengyuanxingbie_1 = (TextView) this.findViewById(R.id.chengyuanxingbie_1);
        chengyuanxingbie_2 = (TextView) this.findViewById(R.id.chengyuanxingbie_2);
        lao_dong_neng_li_1 = (TextView) this.findViewById(R.id.lao_dong_neng_li_1);
        lao_dong_neng_li_2 = (TextView) this.findViewById(R.id.lao_dong_neng_li_2);
        jiankangqingkuang_1 = (TextView) this.findViewById(R.id.jiankangqingkuang_1);
        jiankangqingkuang_2 = (TextView) this.findViewById(R.id.jiankangqingkuang_2);
        id_canjileibie_1 = (TextView) this.findViewById(R.id.id_canjileibie_1);
        id_canjileibie_2 = (TextView) this.findViewById(R.id.id_canjileibie_2);
        canjidengji_1 = (TextView) this.findViewById(R.id.canjidengji_1);
        canjidengji_2 = (TextView) this.findViewById(R.id.canjidengji_2);
        ren_yuan_zhuang_tai_1 = (TextView) this.findViewById(R.id.ren_yuan_zhuang_tai_1);
        ren_yuan_zhuang_tai_2 = (TextView) this.findViewById(R.id.ren_yuan_zhuang_tai_2);
        wenjiachengdu_1 = (TextView) this.findViewById(R.id.wenjiachengdu_1);
        wenjiachengdu_2 = (TextView) this.findViewById(R.id.wenjiachengdu_2);
        yushengqingrenguanxi_1 = (TextView) this.findViewById(R.id.yushengqingrenguanxi_1);
        yushengqingrenguanxi_2 = (TextView) this.findViewById(R.id.yushengqingrenguanxi_2);


        chengyuanxingbie_1.setOnClickListener(this);
        chengyuanxingbie_2.setOnClickListener(this);
        lao_dong_neng_li_1.setOnClickListener(this);
        lao_dong_neng_li_2.setOnClickListener(this);
        jiankangqingkuang_1.setOnClickListener(this);
        jiankangqingkuang_2.setOnClickListener(this);
        id_canjileibie_1.setOnClickListener(this);
        id_canjileibie_2.setOnClickListener(this);
        canjidengji_1.setOnClickListener(this);
        canjidengji_2.setOnClickListener(this);
        ren_yuan_zhuang_tai_1.setOnClickListener(this);
        ren_yuan_zhuang_tai_2.setOnClickListener(this);
        wenjiachengdu_1.setOnClickListener(this);
        wenjiachengdu_2.setOnClickListener(this);
        yushengqingrenguanxi_1.setOnClickListener(this);
        yushengqingrenguanxi_2.setOnClickListener(this);

        sfzx_yes = (CheckBox)this.findViewById(R.id.sfzx_yes);
        sfzx_no = (CheckBox)this.findViewById(R.id.sfzx_no);
        sfhzsfz_yes = (CheckBox)this.findViewById(R.id.sfhzsfz_yes);
        sfhzsfz_no = (CheckBox)this.findViewById(R.id.sfhzsfz_no);
        sfhzhkb_yes = (CheckBox)this.findViewById(R.id.sfhzhkb_yes);
        sfhzhkb_no = (CheckBox)this.findViewById(R.id.sfhzhkb_no);
        shen_feng_zheng.addTextChangedListener(textWatcher);

        xing_bie.setOnClickListener(this);
        er_dai_shen_fen_zhen.setOnClickListener(this);
        jian_kang_qing_kang.setOnClickListener(this);
        can_ji_lei_bie.setOnClickListener(this);
        wen_hua_cheng_du.setOnClickListener(this);
        shen_qing_ren_guan_xi.setOnClickListener(this);
        ren_yuan_zhuang_tai.setOnClickListener(this);
        can_ji_deng_ji.setOnClickListener(this);
        lao_dong_neng_li.setOnClickListener(this);
        sfzx_yes.setOnCheckedChangeListener(this);
        sfzx_no.setOnCheckedChangeListener(this);
        sfhzsfz_yes.setOnCheckedChangeListener(this);
        sfhzsfz_no.setOnCheckedChangeListener(this);
        sfhzhkb_yes.setOnCheckedChangeListener(this);
        sfhzhkb_no.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.yu_lan_chen_yuan:
                if (bao_cun_1() == -1) {
                    break;
                }
                preview();
                break;
            case R.id.er_dai_zheng:
            case R.id.er_dai_shen_fen_zhen:
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
            case R.id.chengyuanxingbie_1:
            case R.id.chengyuanxingbie_2:
                show_xing_bie();
                break;
            case R.id.id_jiankangqingkuang:
            case R.id.jiankangqingkuang_1:
            case R.id.jiankangqingkuang_2:
                jiankangqingkuang();
                break;
            case R.id.canjileibie:
            case R.id.id_canjileibie_1:
            case R.id.id_canjileibie_2:
                canjileibie();
                break;
            case R.id.id_canjidengji:
            case R.id.canjidengji_1:
            case R.id.canjidengji_2:
                canjidengji();
                break;
            case R.id.id_wenhuachengdu:
            case R.id.wenjiachengdu_1:
            case R.id.wenjiachengdu_2:
                id_wenhuachengdu();
                break;
            case R.id.id_yushengqingrenguanxi:
            case R.id.yushengqingrenguanxi_1:
            case R.id.yushengqingrenguanxi_2:
                id_yushengqingrenguanxi();
                break;
            case R.id.ren_yuan_zhuang_tai:
            case R.id.ren_yuan_zhuang_tai_1:
            case R.id.ren_yuan_zhuang_tai_2:
                on_ren_yuan_zhuang_tai();
                break;

            case R.id.lao_dong_neng_li:
            case R.id.lao_dong_neng_li_1:
            case R.id.lao_dong_neng_li_2:
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
    int bao_cun() {
        if (bao_cun_1() == -1) {
            return -1;
        }

        finish();
        return 0;
    }
    int bao_cun_1() {

        if(xing_ming.getText().toString().equals("") ||
                shen_feng_zheng.getText().toString().equals("") ) {
            ToastUtils.showShortToast("请输入正确的姓名和身份证号码");
            return -1;
        }

        mFamilyMemberInfo.setCyxm(xing_ming.getText().toString().trim());
        mFamilyMemberInfo.setCysfzh(shen_feng_zheng.getText().toString().trim());
        if (!nian_ling_str.equals("")) {
            mFamilyMemberInfo.setNl(nian_ling_str.trim());
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
            return -1;
        }
        if (mFamilyMemberInfo.getCyxm() == null) {
            ToastUtils.showLongToast("成员姓名为空");
            return -1;
        }
        if (mFamilyMemberInfo.getCysfzh() == null) {
            ToastUtils.showLongToast("成员身份证为空");
            return -1;
        }
        if (mFamilyMemberInfo.getNl() == null) {
            ToastUtils.showLongToast("成员年龄为空");
            return -1;
        }
        if (mFamilyMemberInfo.getRyzt() == null) {
            ToastUtils.showLongToast("未设置成员状态");
            return -1;
        }
        if (mFamilyMemberInfo.getHchkb() == null) {
            ToastUtils.showLongToast("未设置是否核查户口本");
            return -1;
        }

        if (mFamilyMemberInfo.getHcsfz() == null) {
            ToastUtils.showLongToast("未设置是否核查身份证");
            return -1;
        }


        boolean ret = DBHelper.getInstance().insertOrUpdateMember(mFamilyMemberInfo);

        if(ret == true) {
            ToastUtils.showLongToast("保存成功");
        }
        else {
            ToastUtils.showLongToast("保存失败");
            return -1;
        }

        return 0;
    }


    int _shi_fou_zai_xiao = -1;
    int _id_shifouyicaijishenfenzhen = -1;
    int _id_shifouyihechahukouben = -1;
    boolean change_sf_hc_hkb = false;
    boolean change_sf_hc_sfz = false;
    boolean change_sf_zx = false;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == sfzx_yes) {
            change_sf_zx = true;
            if (isChecked) {
                _shi_fou_zai_xiao = 1;
                sfzx_yes.setChecked(true);
                sfzx_no.setChecked(false);
            }
        } else if (buttonView == sfzx_no) {
            change_sf_zx = true;
            if (isChecked) {
                _shi_fou_zai_xiao = 0;
                sfzx_yes.setChecked(false);
                sfzx_no.setChecked(true);
            }
        } else if (buttonView == sfhzhkb_yes) {
            change_sf_hc_hkb = true;
            if (isChecked) {
                _id_shifouyihechahukouben = 1;
                sfhzhkb_yes.setChecked(true);
                sfhzhkb_no.setChecked(false);
            }
        } else if (buttonView == sfhzhkb_no) {
            change_sf_hc_hkb = true;
            if (isChecked) {
                _id_shifouyihechahukouben = 0;
                sfhzhkb_yes.setChecked(false);
                sfhzhkb_no.setChecked(true);
            }
        } else if (buttonView == sfhzsfz_yes) {
            change_sf_hc_sfz = true;
            if (isChecked) {
                _id_shifouyicaijishenfenzhen = 1;
                sfhzsfz_yes.setChecked(true);
                sfhzsfz_no.setChecked(false);
            }
        } else if (buttonView == sfhzsfz_no) {
            change_sf_hc_sfz = true;
            if (isChecked) {
                _id_shifouyicaijishenfenzhen = 0;
                sfhzsfz_yes.setChecked(false);
                sfhzsfz_no.setChecked(true);
            }
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
        strList.add("无残疾");
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
        strList.add("无残疾");
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
}