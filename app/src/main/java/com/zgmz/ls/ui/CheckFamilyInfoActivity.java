package com.zgmz.ls.ui;

import com.zgmz.ls.R;
import com.zgmz.ls.base.Const;
import com.zgmz.ls.base.Const.InfoType;
import com.zgmz.ls.base.SharedDatas;
import com.zgmz.ls.base.SubActivity;
import com.zgmz.ls.db.DBHelper;
import com.zgmz.ls.db.TableTools;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.SimpleUserInfo;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.ui.adapter.UserInfoAdapter;
import com.zgmz.ls.ui.adapter.UserInfoMemberAdapter;
import com.zgmz.ls.utils.Family;
import com.zgmz.ls.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CheckFamilyInfoActivity extends SubActivity implements OnClickListener{

    private TextView mName;

    private TextView mIDNumber;

    private ImageView mAvatar;

    ImageButton mBtnJiaTingXinxi;
    ImageButton mBtnAddMember;
    ListView mUncheckList;

    ImageButton mBtnCommit;
    TextView mUncheckEmpty;
    UserInfoMemberAdapter mUncheckedAdapter;

    List<UserInfo> mUncheckedUserInfos = new ArrayList<UserInfo>();

    private UserInfo mUserInfo;

    private int mUserId = 0;

    private int mType = InfoType.CHECK;

    private static final int STATE_UNCHECK = 1;


    int mState = STATE_UNCHECK;

    public void onTitleBarRightButtonOnClick(View v) {
        // TODO Auto-generated method stub
        if(mType == InfoType.CHECK) {
            if (mUserInfo != null && !mUserInfo.isChecked()) {

                if(!mUserInfo.isCompleted()) {
                    ToastUtils.showLongToast("有必要项未填写，无法确认");
                    return ;
                }
                if(DBHelper.getInstance().updateUserChecked(mUserId)) {
                    updateUserInfo();
                    SharedDatas.getInstance().checkUpdated();
                    ToastUtils.showLongToast("已确认核查");
                }
            }
        }
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        updateUncheckedData();
    }

    private void ifneedUpdate() {
        if(SharedDatas.getInstance().isCheckUpdated()) {
            if(mState == STATE_UNCHECK) {
                updateUncheckedData();
            }
        }
    }
    public void updateUncheckedData() {
        List<UserInfo> list = DBHelper.getInstance().getUncheckedFamilyMember(100, true, LocalUserInfo.getCheck_task_id());
        if(list != null) {
            mUncheckedUserInfos.clear();
            mUncheckedUserInfos.addAll(list);
            mUncheckedAdapter.notifyDataSetChanged();
        }
    }
    protected void onConfigrationTitleBar() {
        // TODO Auto-generated method stub
        super.onConfigrationTitleBar();
        setTitleBarTitleText("家庭信息");
        //hideTitleBar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jia_ting_xin_xi);
        onNewIntent(getIntent());
        setupViews();
    }
    private SimpleUserInfo LocalUserInfo;
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        LocalUserInfo = (SimpleUserInfo) intent.getSerializableExtra(Const.KEY_USER_INFO);
        if(LocalUserInfo == null) {
            ToastUtils.showLongToast("用户基本信息不能为空！");
            finish();
        }
        else {
            mUserId = LocalUserInfo.getUserId();
            mType = intent.getIntExtra(Const.KEY_TYPE, InfoType.INPUT);
        }

    }

    private void updateUserInfo() {
        mUserInfo = DBHelper.getInstance().getOneUncheckedMember(LocalUserInfo.getIdNumber(),
                LocalUserInfo.getCheck_task_id(), true);
        updateUserBaseInfo(mUserInfo);
    }

    private void updateUserBaseInfo(UserInfo info) {
        if(info == null) return;
        mName.setText(info.getName());
        mIDNumber.setText(info.getIdNumber());
        if(info.getAvatar() != null) {
            mAvatar.setImageBitmap(info.getAvatar());
        }
    }

    protected void setupViews() {
        onNewIntent(getIntent());
        // TODO Auto-generated method stub
        mAvatar = (ImageView) this.findViewById(R.id.avatar);
        mName = (TextView) this.findViewById(R.id.name);
        mIDNumber = (TextView) this.findViewById(R.id.id_number);
        updateUserInfo();
        mBtnJiaTingXinxi = (ImageButton) this.findViewById(R.id.family_situation);
        mBtnJiaTingXinxi.setOnClickListener(this);

        mBtnAddMember = (ImageButton)this.findViewById(R.id.add_member);
        mBtnAddMember.setOnClickListener(this);
        mBtnCommit = (ImageButton)this.findViewById(R.id.commit);
        mBtnCommit.setOnClickListener(this);
        mUncheckEmpty = (TextView) this.findViewById(R.id.empty_uncheck);
        mUncheckList = (ListView) this.findViewById(R.id.list);
        mUncheckedAdapter = new UserInfoMemberAdapter(this, mUncheckedUserInfos);
        mUncheckList.setOnItemClickListener(mUncheckedItemListener);
        mUncheckList.setAdapter(mUncheckedAdapter);
        showTabUncheck();
    }
    private void showTabUncheck() {
        mState = STATE_UNCHECK;
        updateUncheckedData();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.base_info:
                preview();
                break;
            case R.id.family_situation:
                inputFamilySituation();
                break;
            case R.id.add_member:
                inputMemberSituation();
                break;
            case R.id.commit:
                commit();
                break;
        }
    }

    private void commit() {

        FamilyBase mFamilyDBInfo = new FamilyBase();
        mFamilyDBInfo.setCheck_task_id(LocalUserInfo.getCheck_task_id());
        mFamilyDBInfo.setIsChecked("1");
        mFamilyDBInfo.setSqrsfzh(LocalUserInfo.getIdNumber());

        if(DBHelper.getInstance().insertOrUpdateFamilyBase(mFamilyDBInfo)) {
            finish();
            ToastUtils.showLongToast("家庭保存成功");
        }
    }

    public void updateItemIcon(ImageView image, int resid) {
        image.setImageResource(resid);
    }

    private void preview() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, LocalUserInfo);
        intent.setClass(this,DBFamilyPreviewActivity.class);
        startActivity(intent);
    }

    private void inputFamilySituation() {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_USER_INFO, LocalUserInfo);
        intent.setClass(this, FamilyBaseSituationActivity.class);
        startActivity(intent);
    }

    private void inputMemberSituation() {
        Intent intent = new Intent();
        SimpleUserInfo newLocalUserInfo = new SimpleUserInfo();
        newLocalUserInfo.setCheck_task_id(LocalUserInfo.getCheck_task_id());

        FamilyBase info = DBHelper.getInstance().getFamilyWithTaskID(LocalUserInfo.getCheck_task_id());
        newLocalUserInfo.setTime(info.getSqrq());
        newLocalUserInfo.setFather_card_id(LocalUserInfo.getIdNumber());
        intent.putExtra(Const.KEY_USER_INFO, newLocalUserInfo);
        intent.putExtra(Const.KEY_TYPE, InfoType.CHECK);
        intent.setClass(this, CheckUserInfoActivity.class);
        startActivity(intent);
    }

    OnItemClickListener mUncheckedItemListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if(mUncheckedUserInfos.size() > position) {
                startUserInfoActivity(mUncheckedUserInfos.get(position));
            }
        }
    };

    private void startUserInfoActivity(UserInfo userInfo) {
        Intent intent = new Intent();
        userInfo.setFather_idNumber(LocalUserInfo.getIdNumber());
        intent.putExtra(Const.KEY_USER_INFO, userInfo.toSimpleUserInfo());
        intent.putExtra(Const.KEY_TYPE, InfoType.CHECK);
        intent.setClass(this, CheckUserInfoActivity.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUncheckedData();
    }
}
