package com.zgmz.ls.db;


import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.QuHuaMa;
import com.zgmz.ls.utils.BitmapUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.Date;

public class TableTools {
	
	public static final String TABLE_CHECK_USER = "checkuser";
	public static final String TABLE_USER = "user";
	
	public static final String TABLE_ID_CARD = "idcard";
	public static final String TABLE_FINGER_PRINT = "fingerprint";
	public static final String TABLE_DISTRICT = "district";
	
	public static final String TABLE_FAMILY_SITUATION = "fmsituation";
	public static final String TABLE_FAMILY_PROPERTY = "fmproperty";
	public static final String TABLE_FAMILY_INCOME = "fmincome";
	public static final String TABLE_FAMILY_SPENDING = "fmspending";
	
	public static final String TABLE_SIGNATURE = "signature";
	public static final String TABLE_ATTACHMENT = "attachment";
    // 低保专用数据库
    public static final String TABLE_FAMILY_INFO = "familyinfo";
    public static final String TABLE_MEMBERS= "familymembers";
    public static final String TABLE_ATTACHMENTS= "familyattachments";
	public static final String TABLE_CHECKTASK = "checktasks";
	public static final String TABLE_QUHUAMA = "quhuama";
    public static final String TABLE_DOWNLOAD_TASK = "download";
    public interface Download_Task {
        public static final String _ID = "id";
        public static final String NOW_WORK_QUHUAMA = "qu_hua_ma";
        public static final String TOTAL_NUMBER = "total_number";
        public static final String PAGE_ID = "page_id";
        public static final String APPLY_TYPE = "apply_type";    // 救助类型
        public static final String PAGE_NUMBER = "page_number";
        public static final String NOW_WORK_PAGE = "now_work_page";
        public static final String NOW_WORK_TARGET = "now_work_target";
        public static final String NOW_WORK_TARGET_APPLY_TIME = "now_work_target_apply_time";

    }

	public interface QuHuaMa {
		public static final String _ID = "id";
		public static final String NAME = "name";
		public static final String ID_NUMBER = "id_number";
		public static final String FATHER_ID = "father_id";
		public static final String DEPTH = "depth";
	}

	public interface CheckTask {
		// 上传任务编号，unique,
		public static final String _ID = "id";
		// 抽查上报状态，完成上报/待检测/上报失败，待重新发起
		public static final String STATUS = "task_status";
		// 抽查年度
		public static final String CHECK_YEAR = "check_year";
        // 抽查日期
        public static final String CHECK_DATE = "check_date";
        // 抽查人
        public static final String CHECK_OPERATOR = "check_operator";
        // 抽查相关人
        public static final String CHECK_RELATOR = "check_relator";
        // 抽查类型：抽查，核查和新增
        public static final String CHECK_TYPE = "check_type";
		// 抽查对象
		public static final String CHECK_TARGET = "check_target";
		// 上传Server
		public static final String CHECK_SERVER = "check_server";
		// 上传RPC
		public static final String CHECK_RPC = "check_rpc";

	}

    public interface FamilyInfo {
        public static final String _ID = "id";
        // 上传任务编号, unique
        public static final String CHECK_TASK_ID = "check_task_id";
        // 行政区代码
        public static final String DISTRICT = "district";
        // 申请人姓名
        public static final String NAME = "name";
        // 申请人身份证号码
        public static final String CARD_ID = "cardid";
        // 申请时间
        public static final String APPLY_TIME = "applytime";
        // 困难救助类型
        public static final String TYPE = "type";
        // 致贫原因
        public static final String REASON = "reason";
        // 头像
        public static final String IMAGE = "image";
        // 是否已核查/是否上传
        public static final String CHECKED = "checked";
        // 是否已核查/是否上传
        public static final String REQ_ID = "request_id";
    }

    public interface FamilyMember {
        // 编号
        public static final String _ID = "id";
        // 上传任务编号
        public static final String CHECK_TASK_ID = "check_task_id";
        // 申请人身份证号码
        public static final String FATHER_CARD_ID = "fathercardid";
        // 成员姓名
        public static final String NAME = "name";
        // 成员身份证
        public static final String CARD_ID = "cardid";
        // 成员性别
        public static final String GENDER = "gender";
        // 成员年龄
        public static final String AGE = "age";
        // 成员劳动能力
        public static final String LABER_ABAILITY = "laber_abaility";
        // 成员健康状况
        public static final String HEALTHY_STATUS = "healthy_status";
        // 成员残疾类别
        public static final String DISABILITY_TYPE = "disability_type";
        // 成员残疾等级
        public static final String DISABILITY_LEVEL = "disability_level";
        // 成员文化程度
        public static final String EDUCATION_TYPE = "education_type";
        // 成员是否在校
        public static final String IS_AT_SCHOOL = "is_at_school";
        // 是否有成员身份证头像照片
        public static final String IS_HAVE_HEAD_IMAGE = "is_have_head_image";
        // 是否已核查照片
        public static final String IS_CHECK_PHOTO = "is_check_photo";
        // 是否已核查身份证
        public static final String IS_CHECK_ID_CARD = "is_check_id_card";
        // 是否已核查户口本
        public static final String IS_CHECK_HUKOUBEN = "is_check_hukouben";
        // 成员与申请人关系
        public static final String RELATIONSHIP = "relationship";
        // 头像
        public static final String IMAGE = "image";
		// 成员状态
		public static final String STATUS = "status";
		// 身份证扫描还是手动
		public static final String SFZ_STATUS= "sfz_status";
    }

    public interface Attachment {
        // 编号
        public static final String _ID = "id";
        // 上传任务编号
        public static final String CHECK_TASK_ID = "check_task_id";
        // 成员身份证
        public static final String CARD_ID = "cardid";
        // 材料名称
        public static final String METERIAL_NAME = "meterial_name";
        // 材料类型
        public static final String METERIAL_TYPE = "meterial_type";
        // 材料内容
        public static final String METERIAL_CONTENT = "meterial_content";
		// 材料内容
		public static final String METERIAL_VOD_CONTENT = "meterial_vod_content";
        // 材料路径
        public static final String METERIAL_URI = "meterial_uri";
		// 材料年月
		public static final String METERIAL_TIME = "meterial_time";
    }

	public interface BaseColumn {
		// 编号
		public static final String _ID = "id";
		
		// 用户ID
	    public static final String USER_ID = "user_id";
	    
		// 创建时间
	    public static final String CREATED_AT = "created_at";
	    
	    // 最后更新时间
	    public static final String UPDATED_AT = "updated_at";
	}
	
	public interface CheckUserColumn extends BaseColumn {
		// 姓名
		public static final String NAME = "name";
		// 身份证号
		public static final String ID_NUMBER = "id_number";
		
		// 是否已核查
		public static final String CHECKED = "checked";
		
		// 头像
		public static final String AVATAR = "avatar";
	}
	
	
	public interface UserInfoColumn extends BaseColumn {
		// 姓名
		public static final String NAME = "name";
		// 身份证号
		public static final String ID_NUMBER = "id_number";
		// 类型
		public static final String TYPE = "type";
		
		// 是否已核查
		public static final String CHECKED = "checked";
		
		// 是否录入识别身份证
		public static final String F_ID_CARD = "f_id";
		// 是否录入指纹
		public static final String F_FINGER = "f_finger";
		// 是否年度照片
		public static final String F_YEAR_PHOTO = "f_year_photo";
		// 是否录入区域
		public static final String F_DISTRICT = "f_district";
		
		
		// 是否录入家庭状况
		public static final String F_FM_SITUATION = "f_fm_situation";
		// 是否录入家庭财产
		public static final String F_FM_PROPERTY = "f_fm_property";
		// 是否录入家庭收入
		public static final String F_FM_INCOME = "f_fm_income";
		// 是否录入家庭支出
		public static final String F_FM_SPENDING = "f_fm_spending";
		
		
		// 是否录入签名
		public static final String F_SIGN = "f_sign";
		// 是否录入附件
		public static final String F_ATTACH = "f_attach";
		
		// 头像
		public static final String AVATAR = "avatar";
	}
	
	public interface IDCardColumn extends BaseColumn {
		// 姓名
		public static final String NAME = "name";
		// 性别
		public static final String SEX = "sex";
		// 民族
		public static final String ETHNICITY = "ethnicity";
		// 出身日期
		public static final String BIRTH = "birth";
		// 身份证号
		public static final String ID_NUMBER = "id_number";
		
		// 家庭住址
		public static final String ADDRESS = "address";
		// 签发机关
		public static final String AUTHORITY = "authority";
		// 起始有效日期
		public static final String START_VALID_DATE = "start_valid_Date";
		// 结束有效日期
		public static final String END_VALID_DATE = "end_valid_Date";
		
		// wlt
		public static final String WLT = "wlt";
		
		// 头像数据
		public static final String AVATAR = "avatar";
	}

	public interface FingerPrintColumn extends BaseColumn {
	    
	    // 指纹特征值
	    public static final String EIGEN_VALUE = "eigen_value";
	    
	    // 指纹图像数据
	    public static final String IMAGE = "image";
	}
	
	
	public interface DistrictColumn extends BaseColumn {
	    
	    public static final String PROVICE = "provice";
	    
	    public static final String CITY = "city";
	    
	    public static final String DISTRICT = "district";
	    
	    public static final String ADDRESS = "address";
	    
	    public static final String LOCATION = "location";
	    
	    public static final String LATITUDE = "lat";
	    
	    public static final String LONGITUDE = "lng";
	}
	
	public interface FamilySituationColumn extends BaseColumn {
	    
	    public static final String CODE_1001 = "c1001";
	    
	    public static final String CODE_1002 = "c1002";
	    
	    public static final String CODE_1003 = "c1003";
	    
	    public static final String CODE_1004 = "c1004";
	    
	    public static final String CODE_1005 = "c1005";
	    
	    public static final String CODE_1006 = "c1006";
	    
	    public static final String CODE_1007 = "c1007";
	    
	    public static final String CODE_1008 = "c1008";
	    
	    public static final String CODE_1009 = "c1009";
	    
	    public static final String CODE_1010 = "c1010";
	    
	    public static final String CODE_1011 = "c1011";
	    
	    public static final String CODE_1012 = "c1012";
	    
	    public static final String CODE_1013 = "c1013";
	    
	    public static final String CODE_1014 = "c1014";
	    
	    public static final String CODE_1015 = "c1015";
	    
	    public static final String CODE_1016 = "c1016";
	    
	    public static final String CODE_1017 = "c1017";
	}
	
	public interface FamilyPropertyColumn extends BaseColumn {
	    
	    public static final String CODE_2001 = "c2001";
	    
	    public static final String CODE_2002 = "c2002";
	    
	    public static final String CODE_2003 = "c2003";
	    
	    public static final String CODE_2004 = "c2004";
	    
	    public static final String CODE_2005 = "c2005";
	    
	    public static final String CODE_2006 = "c2006";
	    
	    public static final String CODE_2007 = "c2007";
	    
	    public static final String CODE_2008 = "c2008";
	    
	    public static final String CODE_2009 = "c2009";
	    
	    public static final String CODE_2010 = "c2010";
	    
	    public static final String CODE_2011 = "c2011";
	    
	    public static final String CODE_2012 = "c2012";
	    
	    public static final String CODE_2013 = "c2013";
	}
	
	public interface FamilyIncomeColumn extends BaseColumn {
	    
	    public static final String CODE_3001 = "c3001";
	    
	    public static final String CODE_3002 = "c3002";
	    
	    public static final String CODE_3003 = "c3003";
	    
	    public static final String CODE_3004 = "c3004";
	    
	    public static final String CODE_3005 = "c3005";
	    
	    public static final String CODE_3006 = "c3006";
	    
	    public static final String CODE_3007 = "c3007";
	    
	    public static final String CODE_3008 = "c3008";
	    
	    public static final String CODE_3009 = "c3009";
	    
	    public static final String CODE_3010 = "c3010";
	}
	
	public interface FamilySpendingColumn extends BaseColumn {
	    
	    public static final String CODE_4001 = "c4001";
	    
	    public static final String CODE_4002 = "c4002";
	    
	    public static final String CODE_4003 = "c4003";
	    
	    public static final String CODE_4004 = "c4004";
	    
	    public static final String CODE_4005 = "c4005";
	    
	    public static final String CODE_4006 = "c4006";
	    
	    public static final String CODE_4007 = "c4007";
	    
	    public static final String CODE_4008 = "c4008";
	    
	    public static final String CODE_4009 = "c4009";
	    
	    public static final String CODE_4010 = "c4010";
	}
	
	public interface SignatureColumn extends BaseColumn {
	    // 用户签名
	    public static final String USER_SIGN = "user_sign";
	    // 管理员签名
	    public static final String MANA_SIGN = "mana_sign";
	}
	
	public interface AttachmentColumn extends BaseColumn {
	    // 用户签名
	    public static final String NAME = "name";
	    // 管理员签名
	    public static final String PATH = "path";
	}

	public static String[] CHECK_USER_PROJECTION = {
			CheckUserColumn._ID,
			CheckUserColumn.NAME,
			CheckUserColumn.ID_NUMBER,
			CheckUserColumn.CHECKED
	};

    public static String[] CHECK_TASK_PROJECTION = {
            CheckTask._ID,
            CheckTask.CHECK_YEAR,
            CheckTask.CHECK_DATE,
            CheckTask.CHECK_OPERATOR,
            CheckTask.CHECK_TYPE,
            CheckTask.CHECK_TARGET,
			CheckTask.CHECK_SERVER,
			CheckTask.CHECK_RPC,
    };

    public static String[] FAMILY_INFO_PROJECTION = {
            FamilyInfo.REASON,
            FamilyInfo.TYPE,
            FamilyInfo.NAME,
            FamilyInfo.DISTRICT,
            FamilyInfo.CARD_ID,
            FamilyInfo._ID,
            FamilyInfo.APPLY_TIME,
            FamilyInfo.CHECK_TASK_ID,
            FamilyInfo.IMAGE,
            FamilyInfo.CHECKED,
            FamilyInfo.REQ_ID,
    };

	public static String[] USER_INFO_PROJECTION = {
	    	UserInfoColumn._ID,
	    	UserInfoColumn.NAME,
	    	UserInfoColumn.ID_NUMBER,
	    	UserInfoColumn.F_ID_CARD,
	    	UserInfoColumn.F_FINGER,
	    	UserInfoColumn.F_DISTRICT,
	    	UserInfoColumn.F_FM_SITUATION,
	    	UserInfoColumn.F_FM_PROPERTY,
	    	UserInfoColumn.F_FM_INCOME,
	    	UserInfoColumn.F_FM_SPENDING,
	    	UserInfoColumn.F_SIGN,
	    	UserInfoColumn.F_ATTACH,
	    	UserInfoColumn.AVATAR,
	    	UserInfoColumn.TYPE,
	    	UserInfoColumn.CHECKED,
	    	UserInfoColumn.F_YEAR_PHOTO,
	};
	
	public static String[] ID_CARD_PROJECTION = {
	    	IDCardColumn.USER_ID,
	    	IDCardColumn.NAME,
	    	IDCardColumn.ID_NUMBER,
	    	IDCardColumn.SEX,
	    	IDCardColumn.ETHNICITY,
	    	IDCardColumn.BIRTH,
	    	IDCardColumn.ADDRESS,
	    	IDCardColumn.AUTHORITY,
	    	IDCardColumn.START_VALID_DATE,
	    	IDCardColumn.END_VALID_DATE,
	    	IDCardColumn.AVATAR,
	};
	
	public static String[] FINGER_PRINT_PROJECTION = {
	    	FingerPrintColumn.USER_ID,
	    	FingerPrintColumn.EIGEN_VALUE,
	    	FingerPrintColumn.IMAGE,
	};
	
	public static String[] DISTRICT_PROJECTION = {
	    	DistrictColumn.USER_ID,
	    	DistrictColumn.PROVICE,
	    	DistrictColumn.CITY,
	    	DistrictColumn.DISTRICT,
	    	DistrictColumn.ADDRESS,
	    	DistrictColumn.LOCATION,
	    	DistrictColumn.LATITUDE,
	    	DistrictColumn.LONGITUDE,
	};
	
	public static String[] FAMILY_SITUATION_PROJECTION = {
	    	FamilySituationColumn.USER_ID,
	    	FamilySituationColumn.CODE_1001,
	    	FamilySituationColumn.CODE_1002,
	    	FamilySituationColumn.CODE_1003,
	    	FamilySituationColumn.CODE_1004,
	    	FamilySituationColumn.CODE_1005,
	    	FamilySituationColumn.CODE_1006,
	    	FamilySituationColumn.CODE_1007,
	    	FamilySituationColumn.CODE_1008,
	    	FamilySituationColumn.CODE_1009,
	    	FamilySituationColumn.CODE_1010,
	    	FamilySituationColumn.CODE_1011,
	    	FamilySituationColumn.CODE_1012,
	    	FamilySituationColumn.CODE_1013,
	    	FamilySituationColumn.CODE_1014,
	    	FamilySituationColumn.CODE_1015,
	    	FamilySituationColumn.CODE_1016,
	    	FamilySituationColumn.CODE_1017,
	};
	
	public static String[] FAMILY_PROPERTY_PROJECTION = {
			FamilyPropertyColumn.USER_ID,
			FamilyPropertyColumn.CODE_2001,
			FamilyPropertyColumn.CODE_2002,
			FamilyPropertyColumn.CODE_2003,
			FamilyPropertyColumn.CODE_2004,
			FamilyPropertyColumn.CODE_2005,
			FamilyPropertyColumn.CODE_2006,
			FamilyPropertyColumn.CODE_2007,
			FamilyPropertyColumn.CODE_2008,
			FamilyPropertyColumn.CODE_2009,
			FamilyPropertyColumn.CODE_2010,
			FamilyPropertyColumn.CODE_2011,
			FamilyPropertyColumn.CODE_2012,
			FamilyPropertyColumn.CODE_2013,
	};
	
	public static String[] FAMILY_INCOME_PROJECTION = {
			FamilyIncomeColumn.USER_ID,
			FamilyIncomeColumn.CODE_3001,
			FamilyIncomeColumn.CODE_3002,
			FamilyIncomeColumn.CODE_3003,
			FamilyIncomeColumn.CODE_3004,
			FamilyIncomeColumn.CODE_3005,
			FamilyIncomeColumn.CODE_3006,
			FamilyIncomeColumn.CODE_3007,
			FamilyIncomeColumn.CODE_3008,
			FamilyIncomeColumn.CODE_3009,
			FamilyIncomeColumn.CODE_3010,
	};
	
	public static String[] FAMILY_SPENDING_PROJECTION = {
			FamilySpendingColumn.USER_ID,
			FamilySpendingColumn.CODE_4001,
			FamilySpendingColumn.CODE_4002,
			FamilySpendingColumn.CODE_4003,
			FamilySpendingColumn.CODE_4004,
			FamilySpendingColumn.CODE_4005,
			FamilySpendingColumn.CODE_4006,
			FamilySpendingColumn.CODE_4007,
			FamilySpendingColumn.CODE_4008,
			FamilySpendingColumn.CODE_4009,
			FamilySpendingColumn.CODE_4010,
	};
	
	public static String[] SIGNATURE_PROJECTION = {
			SignatureColumn.USER_ID,
			SignatureColumn.USER_SIGN,
			SignatureColumn.MANA_SIGN,
	};
	
	public static String[] ATTACHMENT_PROJECTION = {
			AttachmentColumn.USER_ID,
			AttachmentColumn.NAME,
			AttachmentColumn.PATH,
	};

    public static String[] SB_CHECK_FAMILY_PROJECTION = {
            FamilyInfo._ID,
            FamilyInfo.CHECK_TASK_ID,
            FamilyInfo.DISTRICT,
            FamilyInfo.NAME,
            FamilyInfo.CARD_ID,
            FamilyInfo.APPLY_TIME,
            FamilyInfo.TYPE,
            FamilyInfo.REASON,
            FamilyInfo.IMAGE,
            FamilyInfo.CHECKED,
            FamilyInfo.REQ_ID,
    };

	public static String[] SB_CHECK_ATTACHMENT_PROJECTION = {
			Attachment.METERIAL_TYPE,
			Attachment.METERIAL_NAME,
			Attachment.METERIAL_URI,
            Attachment.CARD_ID,
            Attachment.METERIAL_CONTENT,
            Attachment.CHECK_TASK_ID,
			Attachment.METERIAL_TIME,
	};

    public static String[] SB_CHECK_ATTACHMENT_TIME = {
            Attachment.METERIAL_TIME,
    };
    public static String[] QU_HUA_MA_PROJECTION = {
            QuHuaMa._ID,
            QuHuaMa.FATHER_ID,
            QuHuaMa.ID_NUMBER,
            QuHuaMa.NAME,
            QuHuaMa.DEPTH,
    };
    public static String[] DOWNLOAD_TASK_PROJECTION = {
            Download_Task._ID,
            Download_Task.APPLY_TYPE,
            Download_Task.NOW_WORK_PAGE,
            Download_Task.NOW_WORK_QUHUAMA,
            Download_Task.PAGE_NUMBER,
            Download_Task.NOW_WORK_TARGET,
            Download_Task.NOW_WORK_TARGET_APPLY_TIME,
            Download_Task.PAGE_ID,
            Download_Task.TOTAL_NUMBER,
    };
    public static String[] SB_CHECK_MEMBERS_PROJECTION = {
            FamilyMember._ID,
            FamilyMember.CHECK_TASK_ID,
            FamilyMember.FATHER_CARD_ID,
            FamilyMember.NAME,
            FamilyMember.CARD_ID,
            FamilyMember.GENDER,
            FamilyMember.AGE,
            FamilyMember.LABER_ABAILITY,
            FamilyMember.HEALTHY_STATUS,
            FamilyMember.DISABILITY_TYPE,
            FamilyMember.DISABILITY_LEVEL,
            FamilyMember.EDUCATION_TYPE,
            FamilyMember.IS_AT_SCHOOL,
            FamilyMember.IS_HAVE_HEAD_IMAGE,
            FamilyMember.IS_CHECK_PHOTO,
            FamilyMember.IS_CHECK_ID_CARD,
            FamilyMember.IS_CHECK_HUKOUBEN,
            FamilyMember.RELATIONSHIP,
            FamilyMember.IMAGE,
			FamilyMember.STATUS,
			FamilyMember.SFZ_STATUS,
    };
	public static void createTables(SQLiteDatabase db) {
		
		createTableUserInfo(db);
		
		createTableIDCard(db);
		createTableFingerprint(db);
		createTableDistrict(db);
		
		createTableFamilySituation(db);
		createTableFamilyProperty(db);
		createTableFamilyIncome(db);
		createTableFamilySpending(db);
		
		createTableSignature(db);
		createTableAttachment(db);

        // 低保核查专用
        createTableCheckTask(db);
        createTableFamilyBase(db);
        createTableAttachments(db);
        createTableMembers(db);
        createTableQuHuaMa(db);
        createTableDownloadTask(db);

        // Mock
		initCheckUser(db);
	}

    public static void createTableAttachments(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENTS);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ATTACHMENTS + "(" +
                Attachment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Attachment.CHECK_TASK_ID + " TEXT, " +
                Attachment.METERIAL_CONTENT + " BLOB, " +
				Attachment.METERIAL_VOD_CONTENT + " BLOB, " +
                Attachment.METERIAL_TYPE + " TEXT, " +
                Attachment.CARD_ID + " TEXT, " +
				Attachment.METERIAL_URI + " TEXT, " +
				Attachment.METERIAL_TIME + " TEXT, " +
                Attachment.METERIAL_NAME + " TEXT); " );
    }
    public static void createTableCheckTask(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKTASK);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHECKTASK + "(" +
                CheckTask._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CheckTask.CHECK_DATE + " TEXT, " +
                CheckTask.CHECK_OPERATOR + " TEXT, " +
                CheckTask.CHECK_RELATOR + " TEXT, " +
                CheckTask.CHECK_TARGET + " TEXT, " +
                CheckTask.CHECK_TYPE + " TEXT, " +
				CheckTask.STATUS + " TEXT, " +
                CheckTask.CHECK_YEAR + " TEXT, "+
				CheckTask.CHECK_SERVER + " TEXT, "+
				CheckTask.CHECK_RPC + " TEXT); " );
    }

    public static void createTableFamilyBase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_INFO);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAMILY_INFO + "(" +
                FamilyInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FamilyInfo.CHECK_TASK_ID + " TEXT, " +
                FamilyInfo.APPLY_TIME + " TEXT, " +
                FamilyInfo.CARD_ID + " TEXT, " +
                FamilyInfo.DISTRICT + " TEXT, " +
                FamilyInfo.IMAGE + " BLOB, " +
                FamilyInfo.NAME + " TEXT, " +
                FamilyInfo.CHECKED + " TEXT, " +
                FamilyInfo.TYPE + " TEXT, " +
                FamilyInfo.REASON + " TEXT, " +
                FamilyInfo.REQ_ID + " TEXT); " );
    }

    public static void createTableQuHuaMa(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUHUAMA);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_QUHUAMA + "(" +
                QuHuaMa._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuHuaMa.DEPTH + " TEXT, " +
                QuHuaMa.FATHER_ID + " TEXT, " +
                QuHuaMa.ID_NUMBER + " TEXT, " +
                QuHuaMa.NAME + " TEXT); ");
    }

    public static void createTableDownloadTask(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD_TASK);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOAD_TASK + "(" +
                Download_Task._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Download_Task.NOW_WORK_QUHUAMA + " TEXT, " +
                Download_Task.PAGE_ID + " TEXT, " +
                Download_Task.TOTAL_NUMBER + " TEXT, " +
                Download_Task.APPLY_TYPE + " TEXT, " +
                Download_Task.PAGE_NUMBER + " TEXT, " +
                Download_Task.NOW_WORK_PAGE + " TEXT, " +
                Download_Task.NOW_WORK_TARGET + " TEXT, " +
                Download_Task.NOW_WORK_TARGET_APPLY_TIME + " TEXT); ");
    }

    public static void createTableMembers(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS + "(" +
                FamilyMember._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FamilyMember.AGE + " INTEGER, " +
                FamilyMember.CARD_ID + " TEXT, " +
                FamilyMember.CHECK_TASK_ID + " TEXT, " +
                FamilyMember.DISABILITY_LEVEL + " TEXT, " +
                FamilyMember.DISABILITY_TYPE + " TEXT, " +
                FamilyMember.EDUCATION_TYPE + " TEXT, " +
                FamilyMember.FATHER_CARD_ID + " TEXT, " +
                FamilyMember.GENDER + " TEXT, " +
                FamilyMember.HEALTHY_STATUS + " TEXT, " +
                FamilyMember.IS_AT_SCHOOL + " TEXT, " +
                FamilyMember.IS_CHECK_HUKOUBEN + " TEXT, " +
                FamilyMember.IS_CHECK_ID_CARD + " TEXT, " +
                FamilyMember.IS_CHECK_PHOTO + " TEXT, " +
                FamilyMember.IS_HAVE_HEAD_IMAGE + " TEXT, " +
                FamilyMember.LABER_ABAILITY + " TEXT, " +
                FamilyMember.NAME + " TEXT, " +
                FamilyMember.IMAGE + " BLOB, " +
				FamilyMember.SFZ_STATUS + " TEXT, " +
				FamilyMember.STATUS + " TEXT, " +
                FamilyMember.RELATIONSHIP + " TEXT); " );
    }

	public static void createTableCheckUser(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_USER);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHECK_USER + "(" +
        		CheckUserColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		CheckUserColumn.NAME + " TEXT, " +
        		CheckUserColumn.ID_NUMBER + " TEXT, " +
        		CheckUserColumn.CHECKED + " INTEGER, " +
                CheckUserColumn.AVATAR + " BLOB, " +
        		CheckUserColumn.CREATED_AT + " INTEGER, " +
        		CheckUserColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableUserInfo(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
        		UserInfoColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		UserInfoColumn.NAME + " TEXT, " +
        		UserInfoColumn.ID_NUMBER + " TEXT, " +
        		UserInfoColumn.TYPE + " INTEGER, " +
        		UserInfoColumn.CHECKED + " INTEGER, " +
        		UserInfoColumn.F_ID_CARD + " INTEGER, " +
        		UserInfoColumn.F_FINGER + " INTEGER, " +
        		UserInfoColumn.F_YEAR_PHOTO + " INTEGER, " +
        		UserInfoColumn.F_DISTRICT + " INTEGER, " +
        		UserInfoColumn.F_FM_SITUATION + " INTEGER, " +
        		UserInfoColumn.F_FM_PROPERTY + " INTEGER, " +
        		UserInfoColumn.F_FM_INCOME + " INTEGER, " +
        		UserInfoColumn.F_FM_SPENDING + " INTEGER, " +
        		UserInfoColumn.F_SIGN + " INTEGER, " +
        		UserInfoColumn.F_ATTACH + " INTEGER, " +
        		UserInfoColumn.AVATAR + " BLOB, " +
        		UserInfoColumn.CREATED_AT + " INTEGER, " +
        		UserInfoColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableIDCard(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID_CARD);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ID_CARD + "(" +
        		IDCardColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		IDCardColumn.USER_ID + " INTEGER, " +
        		IDCardColumn.NAME + " TEXT, " +
        		IDCardColumn.ID_NUMBER + " TEXT, " +
        		IDCardColumn.SEX + " TEXT, " +
        		IDCardColumn.BIRTH + " TEXT, " +
        		IDCardColumn.ETHNICITY + " TEXT, " +
        		IDCardColumn.ADDRESS + " TEXT, " +
        		IDCardColumn.AUTHORITY + " TEXT, " +
        		IDCardColumn.START_VALID_DATE + " TEXT, " +
        		IDCardColumn.END_VALID_DATE + " TEXT, " +
        		IDCardColumn.WLT + " BLOB, " +
        		IDCardColumn.AVATAR + " BLOB, " +
        		IDCardColumn.CREATED_AT + " INTEGER, " +
        		IDCardColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableFingerprint(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGER_PRINT);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_PRINT + "(" +
        		FingerPrintColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		FingerPrintColumn.USER_ID + " INTEGER, " +
        		FingerPrintColumn.EIGEN_VALUE + " BLOB, " +
        		FingerPrintColumn.IMAGE + " BLOB, " +
        		FingerPrintColumn.CREATED_AT + " INTEGER, " +
        		FingerPrintColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableDistrict(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DISTRICT + "(" +
        		DistrictColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		DistrictColumn.USER_ID + " INTEGER, " +
        		DistrictColumn.PROVICE + " TEXT, " +
        		DistrictColumn.CITY + " TEXT, " +
        		DistrictColumn.DISTRICT + " TEXT, " +
        		DistrictColumn.ADDRESS + " TEXT, " +
        		DistrictColumn.LOCATION + " TEXT, " +
        		DistrictColumn.LATITUDE + " REAL, " +
        		DistrictColumn.LONGITUDE + " REAL, " +
        		DistrictColumn.CREATED_AT + " INTEGER, " +
        		DistrictColumn.UPDATED_AT + " INTEGER); " );
	}
	
	
	public static void createTableFamilySituation(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_SITUATION);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAMILY_SITUATION + "(" +
        		FamilySituationColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		FamilySituationColumn.USER_ID + " INTEGER, " +
        		FamilySituationColumn.CODE_1001 + " INTEGER, " +
        		FamilySituationColumn.CODE_1002 + " INTEGER, " +
        		FamilySituationColumn.CODE_1003 + " INTEGER, " +
        		FamilySituationColumn.CODE_1004 + " INTEGER, " +
        		FamilySituationColumn.CODE_1005 + " INTEGER, " +
        		FamilySituationColumn.CODE_1006 + " INTEGER, " +
        		FamilySituationColumn.CODE_1007 + " INTEGER, " +
        		FamilySituationColumn.CODE_1008 + " INTEGER, " +
        		FamilySituationColumn.CODE_1009 + " INTEGER, " +
        		FamilySituationColumn.CODE_1010 + " INTEGER, " +
        		FamilySituationColumn.CODE_1011 + " INTEGER, " +
        		FamilySituationColumn.CODE_1012 + " INTEGER, " +
        		FamilySituationColumn.CODE_1013 + " INTEGER, " +
        		FamilySituationColumn.CODE_1014 + " INTEGER, " +
        		FamilySituationColumn.CODE_1015 + " INTEGER, " +
        		FamilySituationColumn.CODE_1016 + " INTEGER, " +
        		FamilySituationColumn.CODE_1017 + " INTEGER, " +
        		FamilySituationColumn.CREATED_AT + " INTEGER, " +
        		FamilySituationColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableFamilyProperty(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_PROPERTY);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAMILY_PROPERTY + "(" +
        		FamilyPropertyColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		FamilyPropertyColumn.USER_ID + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2001 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2002 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2003 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2004 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2005 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2006 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2007 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2008 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2009 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2010 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2011 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2012 + " INTEGER, " +
        		FamilyPropertyColumn.CODE_2013 + " INTEGER, " +
        		FamilyPropertyColumn.CREATED_AT + " INTEGER, " +
        		FamilyPropertyColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableFamilyIncome(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_INCOME);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAMILY_INCOME + "(" +
        		FamilyIncomeColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		FamilyIncomeColumn.USER_ID + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3001 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3002 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3003 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3004 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3005 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3006 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3007 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3008 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3009 + " INTEGER, " +
        		FamilyIncomeColumn.CODE_3010 + " INTEGER, " +
        		FamilyIncomeColumn.CREATED_AT + " INTEGER, " +
        		FamilyIncomeColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableFamilySpending(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAMILY_SPENDING);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAMILY_SPENDING + "(" +
        		FamilySpendingColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		FamilySpendingColumn.USER_ID + " INTEGER, " +
        		FamilySpendingColumn.CODE_4001 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4002 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4003 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4004 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4005 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4006 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4007 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4008 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4009 + " INTEGER, " +
        		FamilySpendingColumn.CODE_4010 + " INTEGER, " +
        		FamilySpendingColumn.CREATED_AT + " INTEGER, " +
        		FamilySpendingColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableSignature(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNATURE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SIGNATURE + "(" +
        		SignatureColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		SignatureColumn.USER_ID + " INTEGER, " +
        		SignatureColumn.USER_SIGN + " BLOB, " +
        		SignatureColumn.MANA_SIGN + " BLOB, " +
        		SignatureColumn.CREATED_AT + " INTEGER, " +
        		SignatureColumn.UPDATED_AT + " INTEGER); " );
	}
	
	public static void createTableAttachment(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENT);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ATTACHMENT + "(" +
        		AttachmentColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        		AttachmentColumn.USER_ID + " INTEGER, " +
        		AttachmentColumn.NAME + " TEXT, " +
        		AttachmentColumn.PATH + " TEXT, " +
        		AttachmentColumn.CREATED_AT + " INTEGER, " +
        		AttachmentColumn.UPDATED_AT + " INTEGER); " );
	}
	
	
	private static String[] sNames = {"张志新", "张宗敏", "张亚春", "史莹"};
	private static String[] sIdNumbers = {"370284197901130819", "230803197906010035", "232102196903151919", "210411198504282942"};
	private static String[] sAvatars = {"avatar/001.jpg","avatar/002.jpg","avatar/003.jpg","avatar/004.jpg"};
	private static String[] sFingers = {"finger/001.jpg","finger/002.jpg","finger/003.jpg","finger/004.jpg"};
	private static String[] sYearPtotos = {"year_photo/001.jpg","year_photo/002.jpg","year_photo/003.jpg","year_photo/004.jpg"};
    private static String[] sManagers = {"manager_signature/001.jpg","manager_signature/002.jpg","manager_signature/003.jpg","manager_signature/004.jpg"};
    private static String[] sUsers = {"user_signature/001.jpg","user_signature/002.jpg","user_signature/003.jpg","user_signature/004.jpg"};

	public static void initDBCheckUser(SQLiteDatabase db) {
		int len = sNames.length < sIdNumbers.length ? sNames.length: sIdNumbers.length;
		Bitmap bmp;
        long ret = 0;
		/*
        ContentValues values_family = new ContentValues();
        values_family.put(FamilyInfo.CHECK_TASK_ID, "0");
        values_family.put(FamilyInfo.APPLY_TIME, "2017-01-01");
        values_family.put(FamilyInfo.CARD_ID, sIdNumbers[0]);
        values_family.put(FamilyInfo.DISTRICT, "441502001001");
        values_family.put(FamilyInfo.NAME, sNames[0]);
        values_family.put(FamilyInfo.REASON, "02");
        values_family.put(FamilyInfo.TYPE, "110");
        values_family.put(FamilyInfo.CHECKED, "0");
        bmp = BitmapUtils.getAssetImage(sAvatars[0]);
        if (bmp != null) {
            values_family.put(FamilyInfo.IMAGE, BitmapUtils.getBitmapByte(bmp));
            bmp.recycle();
        }

        ret = db.insert(TABLE_FAMILY_INFO, null, values_family);

        ContentValues values_check_task = new ContentValues();
        values_check_task.put(CheckTask._ID, "0");
        values_check_task.put(CheckTask.CHECK_DATE, "2017-01-10");
        values_check_task.put(CheckTask.CHECK_OPERATOR, "封豪");
        values_check_task.put(CheckTask.CHECK_TYPE, "01");
        values_check_task.put(CheckTask.CHECK_YEAR, "2017");
        values_check_task.put(CheckTask.CHECK_TARGET, sIdNumbers[0]);
        values_check_task.put(CheckTask.CHECK_RELATOR, "操作员2");
        values_check_task.put(CheckTask.STATUS, "DOING");

        ret = db.insert(TABLE_CHECKTASK, null, values_check_task);


        ContentValues QuHuaMa_Valuyes = new ContentValues();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "0");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "2");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440000000000");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "000000000000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        QuHuaMa_Valuyes.clear();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "1");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "3");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440100000000");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "440000000000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN-1");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        QuHuaMa_Valuyes.clear();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "2");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "3");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440200000000");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "440000000000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN-2");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        QuHuaMa_Valuyes.clear();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "3");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "4");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440101000000");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "440100000000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN-1-1");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        QuHuaMa_Valuyes.clear();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "4");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "5");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440101001000");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "440101000000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN-1-1-1");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        QuHuaMa_Valuyes.clear();
        QuHuaMa_Valuyes.put(QuHuaMa._ID, "5");
        QuHuaMa_Valuyes.put(QuHuaMa.DEPTH, "6");
        QuHuaMa_Valuyes.put(QuHuaMa.ID_NUMBER, "440101001001");
        QuHuaMa_Valuyes.put(QuHuaMa.FATHER_ID, "440101001000");
        QuHuaMa_Valuyes.put(QuHuaMa.NAME, "HUNAN-1-1-1-1");

        ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

		for(int i=0; i<len; i++) {
			ContentValues values = new ContentValues();
			values.put(FamilyMember.NAME, sNames[i]);
			values.put(FamilyMember.CHECK_TASK_ID, "0");
			values.put(FamilyMember.AGE, 20 + i);
			values.put(FamilyMember.CARD_ID, sIdNumbers[i]);
			values.put(FamilyMember.FATHER_CARD_ID, sIdNumbers[0]);
			values.put(FamilyMember.DISABILITY_LEVEL, "0");
			values.put(FamilyMember.DISABILITY_TYPE, "0");
			values.put(FamilyMember.EDUCATION_TYPE, "50");
			values.put(FamilyMember.GENDER, "1");
			values.put(FamilyMember.HEALTHY_STATUS, "10");
			values.put(FamilyMember.IS_AT_SCHOOL, "02");
			values.put(FamilyMember.IS_CHECK_HUKOUBEN, "01");
			values.put(FamilyMember.IS_CHECK_PHOTO, "01");
			values.put(FamilyMember.IS_CHECK_ID_CARD, "01");
			values.put(FamilyMember.IS_HAVE_HEAD_IMAGE, "01");
			values.put(FamilyMember.LABER_ABAILITY, "01");
			values.put(FamilyMember.RELATIONSHIP, "70");
            bmp = BitmapUtils.getAssetImage(sAvatars[i]);
            if (bmp != null) {
                values.put(FamilyMember.IMAGE, BitmapUtils.getBitmapByte(bmp));
                bmp.recycle();
            }
            ret = db.insert(TABLE_MEMBERS, null, values);


            ContentValues values_attachment = new ContentValues();
			bmp = BitmapUtils.getAssetImage(sAvatars[i]);
			if (bmp != null) {
                values_attachment.put(Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(bmp));
				bmp.recycle();
			}
            values_attachment.put(Attachment.METERIAL_URI, "2017-11-12/441502001001/"+ sIdNumbers[0] + "/102/"+sIdNumbers[i]+"-102.jpg");
            values_attachment.put(Attachment.CHECK_TASK_ID, "0");
            values_attachment.put(Attachment.METERIAL_TYPE, "102");
            values_attachment.put(Attachment.METERIAL_NAME, sIdNumbers[i]+"-102.jpg");
            values_attachment.put(Attachment.CARD_ID, sIdNumbers[i]);
            ret = db.insert(TABLE_ATTACHMENTS, null, values_attachment);

			values_attachment = new ContentValues();
			bmp = BitmapUtils.getAssetImage(sFingers[i]);
			if (bmp != null) {
				values_attachment.put(Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(bmp));
				bmp.recycle();
			}
			values_attachment.put(Attachment.METERIAL_URI, "2017-11-12/441502001001/"+ sIdNumbers[0] + "/800/"+sIdNumbers[i]+"-800.jpg");
			values_attachment.put(Attachment.CHECK_TASK_ID, "0");
			values_attachment.put(Attachment.METERIAL_TYPE, "800");
			values_attachment.put(Attachment.METERIAL_NAME, sIdNumbers[i]+"-800.jpg");
			values_attachment.put(Attachment.CARD_ID, sIdNumbers[i]);
			ret = db.insert(TABLE_ATTACHMENTS, null, values_attachment);

            values_attachment = new ContentValues();
            bmp = BitmapUtils.getAssetImage(sFingers[i]);
            if (bmp != null) {
                values_attachment.put(Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(bmp));
                bmp.recycle();
            }
            values_attachment.put(Attachment.METERIAL_URI, "2017-11-12/441502001001/"+ sIdNumbers[0] + "/103/"+sIdNumbers[i]+"-103.jpg");
            values_attachment.put(Attachment.CHECK_TASK_ID, "0");
            values_attachment.put(Attachment.METERIAL_TYPE, "103");
            values_attachment.put(Attachment.METERIAL_NAME, sIdNumbers[i]+"-103.jpg");
            values_attachment.put(Attachment.CARD_ID, sIdNumbers[i]);
            ret = db.insert(TABLE_ATTACHMENTS, null, values_attachment);

            values_attachment = new ContentValues();
            bmp = BitmapUtils.getAssetImage(sManagers[i]);
            if (bmp != null) {
                values_attachment.put(Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(bmp));
                bmp.recycle();
            }
            values_attachment.put(Attachment.METERIAL_URI, "2017-11-12/441502001001/"+ sIdNumbers[0] + "/801/"+sIdNumbers[i]+"-801.jpg");
            values_attachment.put(Attachment.CHECK_TASK_ID, "0");
            values_attachment.put(Attachment.METERIAL_TYPE, "801");
            values_attachment.put(Attachment.METERIAL_NAME, sIdNumbers[i]+"-801.jpg");
            values_attachment.put(Attachment.CARD_ID, sIdNumbers[i]);
            ret = db.insert(TABLE_ATTACHMENTS, null, values_attachment);



            bmp = BitmapUtils.getAssetImage(sUsers[i]);
            if (bmp != null) {
                values_attachment.put(Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(bmp));
                bmp.recycle();
            }
            values_attachment.put(Attachment.METERIAL_URI, "2017-11-12/441502001001/"+ sIdNumbers[0] + "/802/"+sIdNumbers[i]+"-802.jpg");
            values_attachment.put(Attachment.CHECK_TASK_ID, "0");
            values_attachment.put(Attachment.METERIAL_TYPE, "802");
            values_attachment.put(Attachment.METERIAL_NAME, sIdNumbers[i]+"-802.jpg");
            values_attachment.put(Attachment.CARD_ID, sIdNumbers[i]);
            ret = db.insert(TABLE_ATTACHMENTS, null, values_attachment);

            ret = 0;
		}*/
	}
	public static void initCheckUser(SQLiteDatabase db) {

        initDBCheckUser(db);
		int len = sNames.length < sIdNumbers.length ? sNames.length: sIdNumbers.length;
		Bitmap bmp;
		for(int i=0; i<len; i++) {
			ContentValues values = new ContentValues();
			values.put(UserInfoColumn.ID_NUMBER, sNames[i]);
			values.put(UserInfoColumn.NAME, sIdNumbers[i]);
			values.put(UserInfoColumn.CHECKED, 0);
			bmp = BitmapUtils.getAssetImage(sAvatars[i]);
			if (bmp != null) {
				values.put(UserInfoColumn.AVATAR, BitmapUtils.getBitmapByte(bmp));
				bmp.recycle();
			}
			long time = System.currentTimeMillis();
			values.put(UserInfoColumn.CREATED_AT, time);
			values.put(UserInfoColumn.UPDATED_AT, time);
			db.insert(TABLE_USER, null, values);
		}
	}
}
