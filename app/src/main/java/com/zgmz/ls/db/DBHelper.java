package com.zgmz.ls.db;

import java.util.ArrayList;
import java.util.List;

import com.zgmz.ls.AppContext;
import com.zgmz.ls.db.TableTools.AttachmentColumn;
import com.zgmz.ls.db.TableTools.DistrictColumn;
import com.zgmz.ls.db.TableTools.FamilyIncomeColumn;
import com.zgmz.ls.db.TableTools.FamilyPropertyColumn;
import com.zgmz.ls.db.TableTools.FamilySituationColumn;
import com.zgmz.ls.db.TableTools.FamilySpendingColumn;
import com.zgmz.ls.db.TableTools.FingerPrintColumn;
import com.zgmz.ls.db.TableTools.IDCardColumn;
import com.zgmz.ls.db.TableTools.SignatureColumn;
import com.zgmz.ls.db.TableTools.UserInfoColumn;
import com.zgmz.ls.model.Attachment;
import com.zgmz.ls.model.CheckTask;
import com.zgmz.ls.model.District;
import com.zgmz.ls.model.DownloadTask;
import com.zgmz.ls.model.FamilyBase;
import com.zgmz.ls.model.FamilyBase.member;
import com.zgmz.ls.model.FamilyIncome;
import com.zgmz.ls.model.FamilyProperty;
import com.zgmz.ls.model.FamilySituation;
import com.zgmz.ls.model.FamilySpending;
import com.zgmz.ls.model.FingerPrint;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.PreviewInfo;
import com.zgmz.ls.model.QuHuaMa;
import com.zgmz.ls.model.Signature;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.BitmapUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import org.json.JSONObject;

import static com.zgmz.ls.db.TableTools.TABLE_QUHUAMA;
import static com.zgmz.ls.model.Attachment.TYPE_FINGER;
import static com.zgmz.ls.model.Attachment.TYPE_IMAGE_PEOPLE;
import static com.zgmz.ls.model.Attachment.TYPE_SIGNTURE_MANAGER;
import static com.zgmz.ls.model.Attachment.TYPE_SIGNTURE_USER;
import static com.zgmz.ls.model.Attachment.TYPE_VIDEO;
import static com.zgmz.ls.model.CheckTask.STATUS_DOWNLOADING;

public class DBHelper {

	static final String DB_NAME = "ls.db";

	static final int DB_VERSION = 1;

	public static final String CONTENT_URI = "content://com.zgmz.ls";

	class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			onUpgrade(db, 0, DB_VERSION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			for (int ver = oldVersion + 1; ver <= newVersion; ver++) {
				upgradeTo(db, ver);
			}
		}

		private void upgradeTo(SQLiteDatabase db, int version) {
			switch (version) {
			case 1:
				TableTools.createTables(db);
				break;
			default:
				break;
			// throw new IllegalStateException("Don't know how to upgrade to " +
			// version);
			}
		}
	}

	private static DBHelper sInstance;

	private SQLiteHelper mSQLiteHelper;

	private DBHelper(Context context) {
		mSQLiteHelper = new SQLiteHelper(context);
	}

	public SQLiteDatabase getReadableDatabase() {
		return mSQLiteHelper.getReadableDatabase();
	}

	public SQLiteDatabase getWritableDatabase() {
		return mSQLiteHelper.getWritableDatabase();
	}

	public static DBHelper getInstance() {
		if (sInstance == null) {
			synchronized (DBHelper.class) {
				if (sInstance == null) {
					sInstance = new DBHelper(AppContext.getAppContext());
				}
			}
		}
		return sInstance;
	}

	public void destory() {
		sInstance = null;
		mSQLiteHelper.close();
	}

	public PreviewInfo getPreviewInfo(int userId) {
		if (userId <= 0)
			return null;
		PreviewInfo preview = new PreviewInfo();
		preview.setIdCard(getIdCard(userId));
		preview.setFingerPrint(getFingerPrint(userId));
		preview.setDistrict(getDistrict(userId));
		preview.setFamilySituation(getFamilySituation(userId));
		preview.setFamilyProperty(getFamilyProperty(userId));
		preview.setFamilyIncome(getFamilyIncome(userId));
		preview.setFamilySpending(getFamilySpending(userId));
		preview.setSignature(getSignature(userId));
		preview.setAttachments(getAttachments(userId));
		return preview;
	}

	public boolean insertUser(IdCard idCard) {
		if (idCard == null)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserInfoColumn.ID_NUMBER, idCard.getIdNumber());
		values.put(UserInfoColumn.NAME, idCard.getName());
		values.put(UserInfoColumn.CHECKED, 0);

		if (idCard.getAvatar() != null) {
			values.put(UserInfoColumn.AVATAR, BitmapUtils.getBitmapByte(idCard.getAvatar()));
		}

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.CREATED_AT, time);
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_USER, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean hasUser(String idNumber) {
		if (idNumber == null)
			return false;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION, UserInfoColumn.ID_NUMBER + "=?",
				new String[] { String.valueOf(idNumber) }, null, null, null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public boolean insertUser(UserInfo userInfo) {
		if (userInfo == null)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserInfoColumn.ID_NUMBER, userInfo.getIdNumber());
		values.put(UserInfoColumn.NAME, userInfo.getName());
		values.put(UserInfoColumn.CHECKED, 0);

		if (userInfo.getAvatar() != null) {
			values.put(UserInfoColumn.AVATAR, BitmapUtils.getBitmapByte(userInfo.getAvatar()));
		}

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.CREATED_AT, time);
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_USER, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateUserAvatar(IdCard idCard) {
		if (idCard == null)
			return false;
		return updateUserAvatar(idCard.getUserId(), idCard.getAvatar());
	}

	public boolean updateUserAvatar(int userId, Bitmap bmp) {
		if (userId <= 0 || bmp == null)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		if (bmp != null) {
			values.put(UserInfoColumn.AVATAR, BitmapUtils.getBitmapByte(bmp));
		}

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_USER, values, UserInfoColumn._ID + "=? ",
				new String[] { String.valueOf(userId) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateUserChecked(int userId) {
		return updateUserCheck(userId, true);
	}

	public boolean updateUserCheck(int userId, boolean check) {
		if (userId <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserInfoColumn.CHECKED, check ? 1 : 0);

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_USER, values, UserInfoColumn._ID + "=? ",
				new String[] { String.valueOf(userId) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateUserFlag(int userId, String column, boolean flag) {
		if (userId <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(column, flag ? 1 : 0);

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_USER, values, UserInfoColumn._ID + "=? ",
				new String[] { String.valueOf(userId) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertIdCard(IdCard idCard) {
		if (idCard == null || hasIdCard(idCard))
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(IDCardColumn.USER_ID, idCard.getUserId());
		values.put(IDCardColumn.ID_NUMBER, idCard.getIdNumber());
		values.put(IDCardColumn.NAME, idCard.getName());
		values.put(IDCardColumn.ETHNICITY, idCard.getNation());
		values.put(IDCardColumn.SEX, idCard.getSex());
		values.put(IDCardColumn.BIRTH, idCard.getBirth());
		values.put(IDCardColumn.ADDRESS, idCard.getAddress());
		values.put(IDCardColumn.AUTHORITY, idCard.getAuthority());
		values.put(IDCardColumn.START_VALID_DATE, idCard.getStartValidDate());
		values.put(IDCardColumn.END_VALID_DATE, idCard.getEndValidDate());
		values.put(IDCardColumn.WLT, idCard.getWlt());

		if (idCard.getAvatar() != null) {
			values.put(IDCardColumn.AVATAR, BitmapUtils.getBitmapByte(idCard.getAvatar()));
		}

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.CREATED_AT, time);
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_ID_CARD, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateIdCard(IdCard idCard) {
		if (idCard == null || idCard.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(IDCardColumn.ID_NUMBER, idCard.getIdNumber());
		values.put(IDCardColumn.NAME, idCard.getName());
		values.put(IDCardColumn.ETHNICITY, idCard.getNation());
		values.put(IDCardColumn.SEX, idCard.getSex());
		values.put(IDCardColumn.BIRTH, idCard.getBirth());
		values.put(IDCardColumn.ADDRESS, idCard.getAddress());
		values.put(IDCardColumn.AUTHORITY, idCard.getAuthority());
		values.put(IDCardColumn.START_VALID_DATE, idCard.getStartValidDate());
		values.put(IDCardColumn.END_VALID_DATE, idCard.getEndValidDate());
		values.put(IDCardColumn.WLT, idCard.getWlt());

		if (idCard.getAvatar() != null) {
			values.put(IDCardColumn.AVATAR, BitmapUtils.getBitmapByte(idCard.getAvatar()));
		}

		long time = System.currentTimeMillis();
		values.put(UserInfoColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_ID_CARD, values, IDCardColumn.USER_ID + "=? ",
				new String[] { String.valueOf(idCard.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean hasIdCard(IdCard idCard) {
		if (idCard == null || idCard.getUserId() <= 0)
			return false;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_ID_CARD, TableTools.ID_CARD_PROJECTION, IDCardColumn.ID_NUMBER + "=?",
				new String[] { String.valueOf(idCard.getIdNumber()) }, null, null, null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public boolean insertOrUpdateIdCard(IdCard idCard) {
		boolean ret = false;
		if (hasIdCard(idCard)) {
			ret = updateIdCard(idCard);
		} else {
			ret = insertIdCard(idCard);
		}

		if (ret) {
			updateUserAvatar(idCard);
			if (ret) {
				updateUserFlag(idCard.getUserId(), UserInfoColumn.F_ID_CARD, idCard.isCompleted());
			}
		}
		return ret;
	}

	public UserInfo getUserInfo(int userId) {
		return getUserInfo(userId, false);
	}

	public UserInfo getUserInfo(int userId, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION, UserInfoColumn._ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			UserInfo info = new UserInfo();
			info.setUserId(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setIdNumber(cursor.getString(2));
			info.setFlagId(cursor.getInt(3) > 0);
			info.setFlagFinger(cursor.getInt(4) > 0);
			info.setFlagDistrict(cursor.getInt(5) > 0);
			info.setFlagFmSituation(cursor.getInt(6) > 0);
			info.setFlagFmProperty(cursor.getInt(7) > 0);
			info.setFlagFmIncome(cursor.getInt(8) > 0);
			info.setFlagFmSpending(cursor.getInt(9) > 0);
			info.setFlagSignature(cursor.getInt(10) > 0);
			info.setFlagAttachment(cursor.getInt(11) > 0);
			if (avatar)
				info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(12)));
			
			info.setFlagYearPhoto(cursor.getInt(15) > 0);
			return info;
			
		}
		return null;
	}

	public UserInfo getUserInfo(String idNumber) {
		return getUserInfo(idNumber, false);
	}

	public UserInfo getUserInfo(String idNumber, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION,
				UserInfoColumn.ID_NUMBER + "=?", new String[] { String.valueOf(idNumber) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			UserInfo info = new UserInfo();
			info.setUserId(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setIdNumber(cursor.getString(2));
			info.setFlagId(cursor.getInt(3) > 0);
			info.setFlagFinger(cursor.getInt(4) > 0);
			info.setFlagDistrict(cursor.getInt(5) > 0);
			info.setFlagFmSituation(cursor.getInt(6) > 0);
			info.setFlagFmProperty(cursor.getInt(7) > 0);
			info.setFlagFmIncome(cursor.getInt(8) > 0);
			info.setFlagFmSpending(cursor.getInt(9) > 0);
			info.setFlagSignature(cursor.getInt(10) > 0);
			info.setFlagAttachment(cursor.getInt(11) > 0);

			if (avatar)
				info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(12)));

			info.setType(cursor.getInt(13));
			info.setChecked(cursor.getInt(14) > 0);
			return info;
		}
		return null;
	}

	public List<UserInfo> getLastUserInfos(int count) {
		return getLastUserInfos(count, false);
	}

	public List<UserInfo> getLastUserInfos(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 10 || count < 0)
			count = 10;
		Cursor cursor = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION, null, null, null, null,
				UserInfoColumn.UPDATED_AT + " desc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(0));
				info.setName(cursor.getString(1));
				info.setIdNumber(cursor.getString(2));
				info.setFlagId(cursor.getInt(3) > 0);
				info.setFlagFinger(cursor.getInt(4) > 0);
				info.setFlagDistrict(cursor.getInt(5) > 0);
				info.setFlagFmSituation(cursor.getInt(6) > 0);
				info.setFlagFmProperty(cursor.getInt(7) > 0);
				info.setFlagFmIncome(cursor.getInt(8) > 0);
				info.setFlagFmSpending(cursor.getInt(9) > 0);
				info.setFlagSignature(cursor.getInt(10) > 0);
				info.setFlagAttachment(cursor.getInt(11) > 0);
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(12)));
				info.setType(cursor.getInt(13));
				info.setChecked(cursor.getInt(14) > 0);
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}

	public IdCard getIdCard(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ID_CARD, TableTools.ID_CARD_PROJECTION, IDCardColumn.USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			IdCard info = new IdCard();
			info.setUserId(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setIdNumber(cursor.getString(2));
			info.setSex(cursor.getString(3));
			info.setNation(cursor.getString(4));
			info.setBirth(cursor.getString(5));
			info.setAddress(cursor.getString(6));
			info.setAuthority(cursor.getString(7));
			info.setStartValidDate(cursor.getString(8));
			info.setEndValidDate(cursor.getString(9));
			info.setAvatar(BitmapUtils.getBitmapFromByte(cursor.getBlob(10)));
			return info;
		}
		return null;
	}

	public IdCard getIdCard(String idNumber) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ID_CARD, TableTools.ID_CARD_PROJECTION, IDCardColumn.ID_NUMBER + "=?",
				new String[] { String.valueOf(idNumber) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			IdCard info = new IdCard();
			info.setUserId(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setIdNumber(cursor.getString(2));
			info.setSex(cursor.getString(3));
			info.setNation(cursor.getString(4));
			info.setBirth(cursor.getString(5));
			info.setAddress(cursor.getString(6));
			info.setAuthority(cursor.getString(7));
			info.setStartValidDate(cursor.getString(8));
			info.setEndValidDate(cursor.getString(9));
			info.setAvatar(BitmapUtils.getBitmapFromByte(cursor.getBlob(10)));
			return info;
		}
		return null;
	}

	// FingerPrint
	public boolean insertFingerprint(FingerPrint finger) {
		if (finger == null || finger.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FingerPrintColumn.USER_ID, finger.getUserId());
		values.put(FingerPrintColumn.EIGEN_VALUE, finger.getEigenValue());
		if (finger.getCapture() != null) {
			values.put(FingerPrintColumn.IMAGE, BitmapUtils.getBitmapByte(finger.getCapture()));
		}

		long time = System.currentTimeMillis();
		values.put(FingerPrintColumn.CREATED_AT, time);
		values.put(FingerPrintColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_FINGER_PRINT, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateFingerPrint(FingerPrint finger) {
		if (finger == null || finger.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FingerPrintColumn.EIGEN_VALUE, finger.getEigenValue());
		if (finger.getCapture() != null) {
			values.put(FingerPrintColumn.IMAGE, BitmapUtils.getBitmapByte(finger.getCapture()));
		}

		long time = System.currentTimeMillis();
		values.put(FingerPrintColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_FINGER_PRINT, values, FingerPrintColumn.USER_ID + "=? ",
				new String[] { String.valueOf(finger.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdateFingerPrint(FingerPrint finger) {
		boolean ret = false;
		if (hasFingerprint(finger)) {
			ret = updateFingerPrint(finger);
		} else {
			ret = insertFingerprint(finger);
		}

		if (ret) {
			updateUserFlag(finger.getUserId(), UserInfoColumn.F_FINGER, finger.isCompleted());
		}
		return ret;
	}

	public boolean hasFingerprint(FingerPrint finger) {
		if (finger == null || finger.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_FINGER_PRINT, TableTools.FINGER_PRINT_PROJECTION,
				FingerPrintColumn.USER_ID + "=?", new String[] { String.valueOf(finger.getUserId()) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public FingerPrint getFingerPrint(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FINGER_PRINT, TableTools.FINGER_PRINT_PROJECTION,
				FingerPrintColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			FingerPrint info = new FingerPrint();
			info.setUserId(cursor.getInt(0));
			info.setEigenValue(cursor.getBlob(1));
			info.setCapture(BitmapUtils.getBitmapFromByte(cursor.getBlob(2)));
			return info;
		}
		return null;
	}

	// District
	public boolean insertDistrict(District district) {
		if (district == null || district.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DistrictColumn.USER_ID, district.getUserId());
		values.put(DistrictColumn.PROVICE, district.getProvice());
		values.put(DistrictColumn.CITY, district.getCity());
		values.put(DistrictColumn.DISTRICT, district.getDistric());
		values.put(DistrictColumn.ADDRESS, district.getAddress());
		values.put(DistrictColumn.LOCATION, district.getLocation());
		values.put(DistrictColumn.LATITUDE, district.getLatitude());
		values.put(DistrictColumn.LONGITUDE, district.getLongitude());

		long time = System.currentTimeMillis();
		values.put(DistrictColumn.CREATED_AT, time);
		values.put(DistrictColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_DISTRICT, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateDistrict(District district) {
		if (district == null || district.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DistrictColumn.PROVICE, district.getProvice());
		values.put(DistrictColumn.CITY, district.getCity());
		values.put(DistrictColumn.DISTRICT, district.getDistric());
		values.put(DistrictColumn.ADDRESS, district.getAddress());
		values.put(DistrictColumn.LOCATION, district.getLocation());
		values.put(DistrictColumn.LATITUDE, district.getLatitude());
		values.put(DistrictColumn.LONGITUDE, district.getLongitude());

		long time = System.currentTimeMillis();
		values.put(DistrictColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_DISTRICT, values, DistrictColumn.USER_ID + "=? ",
				new String[] { String.valueOf(district.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdatDistrict(District district) {
		boolean ret = false;
		if (hasDistrict(district)) {
			ret = updateDistrict(district);
		} else {
			ret = insertDistrict(district);
		}

		if (ret) {
			updateUserFlag(district.getUserId(), UserInfoColumn.F_DISTRICT, district.isCompleted());
		}
		return ret;
	}

	public boolean hasDistrict(District district) {
		if (district == null || district.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_DISTRICT, TableTools.DISTRICT_PROJECTION, DistrictColumn.USER_ID + "=?",
				new String[] { String.valueOf(district.getUserId()) }, null, null, null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public District getDistrict(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_DISTRICT, TableTools.DISTRICT_PROJECTION,
				DistrictColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			District info = new District();
			info.setUserId(cursor.getInt(0));
			info.setProvice(cursor.getString(1));
			info.setCity(cursor.getString(2));
			info.setDistric(cursor.getString(3));
			info.setAddress(cursor.getString(4));
			info.setLocation(cursor.getString(5));
			info.setLatitude(cursor.getDouble(6));
			info.setLongitude(cursor.getDouble(7));
			return info;
		}
		return null;
	}

	// FamilySituation
	public boolean insertFamilySituation(FamilySituation situation) {
		if (situation == null || situation.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilySituationColumn.USER_ID, situation.getUserId());
		values.put(FamilySituationColumn.CODE_1001, situation.getCode1001());
		values.put(FamilySituationColumn.CODE_1002, situation.getCode1002());
		values.put(FamilySituationColumn.CODE_1003, situation.getCode1003());
		values.put(FamilySituationColumn.CODE_1004, situation.getCode1004());
		values.put(FamilySituationColumn.CODE_1005, situation.getCode1005());
		values.put(FamilySituationColumn.CODE_1006, situation.getCode1006());
		values.put(FamilySituationColumn.CODE_1007, situation.getCode1007());
		values.put(FamilySituationColumn.CODE_1008, situation.getCode1008());
		values.put(FamilySituationColumn.CODE_1009, situation.getCode1009());
		values.put(FamilySituationColumn.CODE_1010, situation.getCode1010());
		values.put(FamilySituationColumn.CODE_1011, situation.getCode1011());
		values.put(FamilySituationColumn.CODE_1012, situation.getCode1012());
		values.put(FamilySituationColumn.CODE_1013, situation.getCode1013());
		values.put(FamilySituationColumn.CODE_1014, situation.getCode1014());
		values.put(FamilySituationColumn.CODE_1015, situation.getCode1015());
		values.put(FamilySituationColumn.CODE_1016, situation.getCode1016());
		values.put(FamilySituationColumn.CODE_1017, situation.getCode1017());

		long time = System.currentTimeMillis();
		values.put(FamilySituationColumn.CREATED_AT, time);
		values.put(FamilySituationColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_FAMILY_SITUATION, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateFamilySituation(FamilySituation situation) {
		if (situation == null || situation.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilySituationColumn.CODE_1001, situation.getCode1001());
		values.put(FamilySituationColumn.CODE_1002, situation.getCode1002());
		values.put(FamilySituationColumn.CODE_1003, situation.getCode1003());
		values.put(FamilySituationColumn.CODE_1004, situation.getCode1004());
		values.put(FamilySituationColumn.CODE_1005, situation.getCode1005());
		values.put(FamilySituationColumn.CODE_1006, situation.getCode1006());
		values.put(FamilySituationColumn.CODE_1007, situation.getCode1007());
		values.put(FamilySituationColumn.CODE_1008, situation.getCode1008());
		values.put(FamilySituationColumn.CODE_1009, situation.getCode1009());
		values.put(FamilySituationColumn.CODE_1010, situation.getCode1010());
		values.put(FamilySituationColumn.CODE_1011, situation.getCode1011());
		values.put(FamilySituationColumn.CODE_1012, situation.getCode1012());
		values.put(FamilySituationColumn.CODE_1013, situation.getCode1013());
		values.put(FamilySituationColumn.CODE_1014, situation.getCode1014());
		values.put(FamilySituationColumn.CODE_1015, situation.getCode1015());
		values.put(FamilySituationColumn.CODE_1016, situation.getCode1016());
		values.put(FamilySituationColumn.CODE_1017, situation.getCode1017());

		long time = System.currentTimeMillis();
		values.put(FamilySituationColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_FAMILY_SITUATION, values, FamilySituationColumn.USER_ID + "=? ",
				new String[] { String.valueOf(situation.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdateFamilySituation(FamilySituation situation) {
		boolean ret = false;
		if (hasFamilySituation(situation)) {
			ret = updateFamilySituation(situation);
		} else {
			ret = insertFamilySituation(situation);
		}

		if (ret) {
			updateUserFlag(situation.getUserId(), UserInfoColumn.F_FM_SITUATION, situation.isCompleted());
		}
		return ret;
	}

	public boolean hasFamilySituation(FamilySituation situation) {
		if (situation == null || situation.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_FAMILY_SITUATION, TableTools.FAMILY_SITUATION_PROJECTION,
				FamilySituationColumn.USER_ID + "=?", new String[] { String.valueOf(situation.getUserId()) }, null,
				null, null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}


	public FamilySituation getFamilySituation(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_SITUATION, TableTools.FAMILY_SITUATION_PROJECTION,
				FamilySituationColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			FamilySituation info = new FamilySituation();
			info.setUserId(cursor.getInt(0));
			info.setCode1001(cursor.getInt(1));
			info.setCode1002(cursor.getInt(2));
			info.setCode1003(cursor.getInt(3));
			info.setCode1004(cursor.getInt(4));
			info.setCode1005(cursor.getInt(5));
			info.setCode1006(cursor.getInt(6));
			info.setCode1007(cursor.getInt(7));
			info.setCode1008(cursor.getInt(8));
			info.setCode1009(cursor.getInt(9));
			info.setCode1010(cursor.getInt(10));
			info.setCode1011(cursor.getInt(11));
			info.setCode1012(cursor.getInt(12));
			info.setCode1013(cursor.getInt(13));
			info.setCode1014(cursor.getInt(14));
			info.setCode1015(cursor.getInt(15));
			info.setCode1016(cursor.getInt(16));
			info.setCode1017(cursor.getInt(17));
			return info;
		}
		return null;
	}

	// FamilyProperty
	public boolean insertFamilyProperty(FamilyProperty property) {
		if (property == null || property.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilyPropertyColumn.USER_ID, property.getUserId());
		values.put(FamilyPropertyColumn.CODE_2001, property.getCode2001());
		values.put(FamilyPropertyColumn.CODE_2002, property.getCode2002());
		values.put(FamilyPropertyColumn.CODE_2003, property.getCode2003());
		values.put(FamilyPropertyColumn.CODE_2004, property.getCode2004());
		values.put(FamilyPropertyColumn.CODE_2005, property.getCode2005());
		values.put(FamilyPropertyColumn.CODE_2006, property.getCode2006());
		values.put(FamilyPropertyColumn.CODE_2007, property.getCode2007());
		values.put(FamilyPropertyColumn.CODE_2008, property.getCode2008());
		values.put(FamilyPropertyColumn.CODE_2009, property.getCode2009());
		values.put(FamilyPropertyColumn.CODE_2010, property.getCode2010());
		values.put(FamilyPropertyColumn.CODE_2011, property.getCode2011());
		values.put(FamilyPropertyColumn.CODE_2012, property.getCode2012());
		values.put(FamilyPropertyColumn.CODE_2013, property.getCode2013());

		long time = System.currentTimeMillis();
		values.put(FamilyPropertyColumn.CREATED_AT, time);
		values.put(FamilyPropertyColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_FAMILY_PROPERTY, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateFamilyProperty(FamilyProperty property) {
		if (property == null || property.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilyPropertyColumn.CODE_2001, property.getCode2001());
		values.put(FamilyPropertyColumn.CODE_2002, property.getCode2002());
		values.put(FamilyPropertyColumn.CODE_2003, property.getCode2003());
		values.put(FamilyPropertyColumn.CODE_2004, property.getCode2004());
		values.put(FamilyPropertyColumn.CODE_2005, property.getCode2005());
		values.put(FamilyPropertyColumn.CODE_2006, property.getCode2006());
		values.put(FamilyPropertyColumn.CODE_2007, property.getCode2007());
		values.put(FamilyPropertyColumn.CODE_2008, property.getCode2008());
		values.put(FamilyPropertyColumn.CODE_2009, property.getCode2009());
		values.put(FamilyPropertyColumn.CODE_2010, property.getCode2010());
		values.put(FamilyPropertyColumn.CODE_2011, property.getCode2011());
		values.put(FamilyPropertyColumn.CODE_2012, property.getCode2012());
		values.put(FamilyPropertyColumn.CODE_2013, property.getCode2013());

		long time = System.currentTimeMillis();
		values.put(FamilyPropertyColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_FAMILY_PROPERTY, values, FamilyPropertyColumn.USER_ID + "=? ",
				new String[] { String.valueOf(property.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdateFamilyProperty(FamilyProperty property) {
		boolean ret = false;
		if (hasFamilyProperty(property)) {
			ret = updateFamilyProperty(property);
		} else {
			ret = insertFamilyProperty(property);
		}

		if (ret) {
			updateUserFlag(property.getUserId(), UserInfoColumn.F_FM_PROPERTY, property.isCompleted());
		}
		return ret;
	}

	public boolean hasFamilyProperty(FamilyProperty property) {
		if (property == null || property.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_FAMILY_PROPERTY, TableTools.FAMILY_PROPERTY_PROJECTION,
				FamilyPropertyColumn.USER_ID + "=?", new String[] { String.valueOf(property.getUserId()) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public FamilyProperty getFamilyProperty(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_PROPERTY, TableTools.FAMILY_PROPERTY_PROJECTION,
				FamilyPropertyColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			FamilyProperty info = new FamilyProperty();
			info.setUserId(cursor.getInt(0));
			info.setCode2001(cursor.getInt(1));
			info.setCode2002(cursor.getInt(2));
			info.setCode2003(cursor.getInt(3));
			info.setCode2004(cursor.getInt(4));
			info.setCode2005(cursor.getInt(5));
			info.setCode2006(cursor.getInt(6));
			info.setCode2007(cursor.getInt(7));
			info.setCode2008(cursor.getInt(8));
			info.setCode2009(cursor.getInt(9));
			info.setCode2010(cursor.getInt(10));
			info.setCode2011(cursor.getInt(11));
			info.setCode2012(cursor.getInt(12));
			info.setCode2013(cursor.getInt(13));
			return info;
		}
		return null;
	}

	// FamilyIncome
	public boolean insertFamilyIncome(FamilyIncome income) {
		if (income == null || income.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilyIncomeColumn.USER_ID, income.getUserId());
		values.put(FamilyIncomeColumn.CODE_3001, income.getCode3001());
		values.put(FamilyIncomeColumn.CODE_3002, income.getCode3002());
		values.put(FamilyIncomeColumn.CODE_3003, income.getCode3003());
		values.put(FamilyIncomeColumn.CODE_3004, income.getCode3004());
		values.put(FamilyIncomeColumn.CODE_3005, income.getCode3005());
		values.put(FamilyIncomeColumn.CODE_3006, income.getCode3006());
		values.put(FamilyIncomeColumn.CODE_3007, income.getCode3007());
		values.put(FamilyIncomeColumn.CODE_3008, income.getCode3008());
		values.put(FamilyIncomeColumn.CODE_3009, income.getCode3009());
		values.put(FamilyIncomeColumn.CODE_3010, income.getCode3010());

		long time = System.currentTimeMillis();
		values.put(FamilyIncomeColumn.CREATED_AT, time);
		values.put(FamilyIncomeColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_FAMILY_INCOME, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateFamilyIncome(FamilyIncome income) {
		if (income == null || income.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilyIncomeColumn.CODE_3001, income.getCode3001());
		values.put(FamilyIncomeColumn.CODE_3002, income.getCode3002());
		values.put(FamilyIncomeColumn.CODE_3003, income.getCode3003());
		values.put(FamilyIncomeColumn.CODE_3004, income.getCode3004());
		values.put(FamilyIncomeColumn.CODE_3005, income.getCode3005());
		values.put(FamilyIncomeColumn.CODE_3006, income.getCode3006());
		values.put(FamilyIncomeColumn.CODE_3007, income.getCode3007());
		values.put(FamilyIncomeColumn.CODE_3008, income.getCode3008());
		values.put(FamilyIncomeColumn.CODE_3009, income.getCode3009());
		values.put(FamilyIncomeColumn.CODE_3010, income.getCode3010());

		long time = System.currentTimeMillis();
		values.put(FamilyIncomeColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_FAMILY_INCOME, values, FamilyIncomeColumn.USER_ID + "=? ",
				new String[] { String.valueOf(income.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdateFamilyIncome(FamilyIncome income) {
		boolean ret = false;
		if (hasFamilyIncome(income)) {
			ret = updateFamilyIncome(income);
		} else {
			ret = insertFamilyIncome(income);
		}

		if (ret) {
			updateUserFlag(income.getUserId(), UserInfoColumn.F_FM_INCOME, income.isCompleted());
		}
		return ret;
	}

	public boolean hasFamilyIncome(FamilyIncome income) {
		if (income == null || income.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_FAMILY_INCOME, TableTools.FAMILY_INCOME_PROJECTION,
				FamilyIncomeColumn.USER_ID + "=?", new String[] { String.valueOf(income.getUserId()) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public FamilyIncome getFamilyIncome(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_INCOME, TableTools.FAMILY_INCOME_PROJECTION,
				FamilyIncomeColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			FamilyIncome info = new FamilyIncome();
			info.setUserId(cursor.getInt(0));
			info.setCode3001(cursor.getInt(1));
			info.setCode3002(cursor.getInt(2));
			info.setCode3003(cursor.getInt(3));
			info.setCode3004(cursor.getInt(4));
			info.setCode3005(cursor.getInt(5));
			info.setCode3006(cursor.getInt(6));
			info.setCode3007(cursor.getInt(7));
			info.setCode3008(cursor.getInt(8));
			info.setCode3009(cursor.getInt(9));
			info.setCode3010(cursor.getInt(10));
			return info;
		}
		return null;
	}

	// FamilySpending
	public boolean insertFamilySpending(FamilySpending spending) {
		if (spending == null || spending.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilySpendingColumn.USER_ID, spending.getUserId());
		values.put(FamilySpendingColumn.CODE_4001, spending.getCode4001());
		values.put(FamilySpendingColumn.CODE_4002, spending.getCode4002());
		values.put(FamilySpendingColumn.CODE_4003, spending.getCode4003());
		values.put(FamilySpendingColumn.CODE_4004, spending.getCode4004());
		values.put(FamilySpendingColumn.CODE_4005, spending.getCode4005());
		values.put(FamilySpendingColumn.CODE_4006, spending.getCode4006());
		values.put(FamilySpendingColumn.CODE_4007, spending.getCode4007());
		values.put(FamilySpendingColumn.CODE_4008, spending.getCode4008());
		values.put(FamilySpendingColumn.CODE_4009, spending.getCode4009());
		values.put(FamilySpendingColumn.CODE_4010, spending.getCode4010());

		long time = System.currentTimeMillis();
		values.put(FamilySpendingColumn.CREATED_AT, time);
		values.put(FamilySpendingColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_FAMILY_SPENDING, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateFamilySpending(FamilySpending spending) {
		if (spending == null || spending.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FamilySpendingColumn.CODE_4001, spending.getCode4001());
		values.put(FamilySpendingColumn.CODE_4002, spending.getCode4002());
		values.put(FamilySpendingColumn.CODE_4003, spending.getCode4003());
		values.put(FamilySpendingColumn.CODE_4004, spending.getCode4004());
		values.put(FamilySpendingColumn.CODE_4005, spending.getCode4005());
		values.put(FamilySpendingColumn.CODE_4006, spending.getCode4006());
		values.put(FamilySpendingColumn.CODE_4007, spending.getCode4007());
		values.put(FamilySpendingColumn.CODE_4008, spending.getCode4008());
		values.put(FamilySpendingColumn.CODE_4009, spending.getCode4009());
		values.put(FamilySpendingColumn.CODE_4010, spending.getCode4010());

		long time = System.currentTimeMillis();
		values.put(FamilySpendingColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_FAMILY_SPENDING, values, FamilySpendingColumn.USER_ID + "=? ",
				new String[] { String.valueOf(spending.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdateFamilySpending(FamilySpending spending) {
		boolean ret = false;
		if (hasFamilySpending(spending)) {
			ret = updateFamilySpending(spending);
		} else {
			ret = insertFamilySpending(spending);
		}

		if (ret) {
			updateUserFlag(spending.getUserId(), UserInfoColumn.F_FM_SPENDING, spending.isCompleted());
		}
		return ret;
	}

	public boolean hasFamilySpending(FamilySpending spending) {
		if (spending == null || spending.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_FAMILY_SPENDING, TableTools.FAMILY_SPENDING_PROJECTION,
				FamilySpendingColumn.USER_ID + "=?", new String[] { String.valueOf(spending.getUserId()) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public FamilySpending getFamilySpending(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_SPENDING, TableTools.FAMILY_SPENDING_PROJECTION,
				FamilyPropertyColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			FamilySpending info = new FamilySpending();
			info.setUserId(cursor.getInt(0));
			info.setCode4001(cursor.getInt(1));
			info.setCode4002(cursor.getInt(2));
			info.setCode4003(cursor.getInt(3));
			info.setCode4004(cursor.getInt(4));
			info.setCode4005(cursor.getInt(5));
			info.setCode4006(cursor.getInt(6));
			info.setCode4007(cursor.getInt(7));
			info.setCode4008(cursor.getInt(8));
			info.setCode4009(cursor.getInt(9));
			info.setCode4010(cursor.getInt(10));
			return info;
		}
		return null;
	}

	// Signature
	public boolean insertSignature(Signature signature) {
		if (signature == null || signature.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SignatureColumn.USER_ID, signature.getUserId());
		if (signature.getUserSignature() != null) {
			values.put(SignatureColumn.USER_SIGN, BitmapUtils.getBitmapByte(signature.getUserSignature()));
		}
		if (signature.getManagerSignature() != null) {
			values.put(SignatureColumn.MANA_SIGN, BitmapUtils.getBitmapByte(signature.getManagerSignature()));
		}

		long time = System.currentTimeMillis();
		values.put(SignatureColumn.CREATED_AT, time);
		values.put(SignatureColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_SIGNATURE, null, values);
		if (ret != -1)
			return true;
		return false;
	}

	public boolean updateSignature(Signature signature) {
		if (signature == null || signature.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		if (signature.getUserSignature() != null) {
			values.put(SignatureColumn.USER_SIGN, BitmapUtils.getBitmapByte(signature.getUserSignature()));
		}
		if (signature.getManagerSignature() != null) {
			values.put(SignatureColumn.MANA_SIGN, BitmapUtils.getBitmapByte(signature.getManagerSignature()));
		}

		long time = System.currentTimeMillis();
		values.put(SignatureColumn.UPDATED_AT, time);
		long ret = db.update(TableTools.TABLE_SIGNATURE, values, SignatureColumn.USER_ID + "=? ",
				new String[] { String.valueOf(signature.getUserId()) });
		if (ret != -1)
			return true;
		return false;
	}

	public boolean insertOrUpdatSignature(Signature signature) {
		boolean ret = false;
		if (hasSignature(signature)) {
			ret = updateSignature(signature);
		} else {
			ret = insertSignature(signature);
		}

		if (ret) {
			updateUserFlag(signature.getUserId(), UserInfoColumn.F_SIGN, signature.isCompleted());
		}
		return ret;
	}

	public boolean hasSignature(Signature signature) {
		if (signature == null || signature.getUserId() <= 0)
			return false;

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_SIGNATURE, TableTools.SIGNATURE_PROJECTION,
				SignatureColumn.USER_ID + "=?", new String[] { String.valueOf(signature.getUserId()) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

	public Signature getSignature(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_SIGNATURE, TableTools.SIGNATURE_PROJECTION,
				SignatureColumn.USER_ID + "=?", new String[] { String.valueOf(userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			Signature info = new Signature();
			info.setUserId(cursor.getInt(0));
			info.setUserSignature(BitmapUtils.getBitmapFromByte(cursor.getBlob(1)));
			info.setManagerSignature(BitmapUtils.getBitmapFromByte(cursor.getBlob(2)));
			return info;
		}
		return null;
	}
    public Signature getSignature(String check_task_id, String card_id) {
        Signature info = new Signature();
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION,
                TableTools.Attachment.CHECK_TASK_ID + "=? and " + TableTools.Attachment.CARD_ID + "=? and " +
                        TableTools.Attachment.METERIAL_TYPE + "=?"
                , new String[] { String.valueOf(check_task_id), String.valueOf(card_id), String.valueOf("801") }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            info.setManagerSignature(BitmapUtils.getBitmapFromByte(cursor.getBlob(4)));
        }
        cursor = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION,
                TableTools.Attachment.CHECK_TASK_ID + "=? and " + TableTools.Attachment.CARD_ID + "=? and " +
                        TableTools.Attachment.METERIAL_TYPE + "=?"
                , new String[] { String.valueOf(check_task_id), String.valueOf(card_id), String.valueOf("802") }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            info.setUserSignature(BitmapUtils.getBitmapFromByte(cursor.getBlob(4)));
            return info;
        }
        return null;
    }

	public boolean deleteHCMember(String card_id, String task_id) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		int ret = db.delete(TableTools.TABLE_MEMBERS, TableTools.FamilyMember.CHECK_TASK_ID + "=? and "+
						TableTools.FamilyMember.CARD_ID +" =?",
				new String[] { String.valueOf(task_id), new String(card_id)});
		if (ret > 0) {
			return true;
		}
		return false;
	}

	public Attachment getAttachment(String card_id, String task_id, String type) {
		if (task_id == null || card_id == null) return null;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION, TableTools.Attachment.CHECK_TASK_ID + "=? and "+
				TableTools.Attachment.CARD_ID +" = ? and "+
                TableTools.Attachment.METERIAL_TYPE +" = ?",
				new String[] { String.valueOf(card_id), new String(task_id), new String(type) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			Attachment info;
			info = new Attachment();
			info.setType(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setPath(cursor.getString(2));
            info.setCard_id(cursor.getString(3));
            info.setContent(BitmapUtils.getBitmapFromByte(cursor.getBlob(4)));
            info.setCheck_task_id(cursor.getString(5));
			cursor.close();
			return info;
		}
		return null;
	}


    public List<Attachment> getAllAttachments(String card_id, String task_id) {
        if (task_id == null || card_id == null) return null;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION,
                TableTools.Attachment.CHECK_TASK_ID + "=? and "+  TableTools.Attachment.CARD_ID +" = ?",
                new String[] { String.valueOf(task_id), new String(card_id) }, null, null, null);
        if (cursor != null) {
            List<Attachment> list = new ArrayList<Attachment>();
            Attachment info;
            while (cursor.moveToNext()) {
                info = new Attachment();
                info.setType(cursor.getInt(0));
                info.setName(cursor.getString(1));
                info.setPath(cursor.getString(2));
                info.setCard_id(cursor.getString(3));
                info.setContent(BitmapUtils.getBitmapFromByte(cursor.getBlob(4)));
                info.setCheck_task_id(cursor.getString(5));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }
	
	public List<Attachment> getAttachments(int userId) {
		if (userId <= 0) return null;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ATTACHMENT, TableTools.ATTACHMENT_PROJECTION, AttachmentColumn.USER_ID + "=? and "+ AttachmentColumn.NAME +" != ?",
				new String[] { String.valueOf(userId), new String("year" + userId) }, null, null, null);
		if (cursor != null) {
			List<Attachment> list = new ArrayList<Attachment>();
			Attachment info;
			while (cursor.moveToNext()) {
				info = new Attachment();
				info.setUserId(cursor.getInt(0));
				info.setName(cursor.getString(1));
				info.setPath(cursor.getString(2));
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}
	public List<CheckTask> getDownloadingTasks(int count) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        if (count > 100 || count < 0)
            count = 100;
        Cursor cursor = db.query(TableTools.TABLE_CHECKTASK, TableTools.CHECK_TASK_PROJECTION, TableTools.CheckTask.STATUS + "=?",
                new String[] { String.valueOf(STATUS_DOWNLOADING) }, null, null, TableTools.CheckTask._ID + " asc LIMIT " + count);
        if (cursor != null) {
            List<CheckTask> list = new ArrayList<CheckTask>();
            while (cursor.moveToNext() && list.size() <= count) {
                CheckTask info = new CheckTask();
                info.setCheck_task_id(cursor.getString(0));
                info.setNd(cursor.getString(1));
                info.setDate(cursor.getString(2));
                info.setFzr(cursor.getString(3));
                info.setLx(cursor.getString(4));
                info.setTarget(cursor.getString(5));
				info.setServer(cursor.getString(6));
				info.setRpc(cursor.getString(7));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }

    public List<UserInfo> getDownloadingTasksAsUserInfo(int count) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        if (count > 100 || count < 0)
            count = 100;
        Cursor cursor = db.query(TableTools.TABLE_CHECKTASK, TableTools.CHECK_TASK_PROJECTION, TableTools.CheckTask.STATUS + "=?",
                new String[] { String.valueOf(STATUS_DOWNLOADING) }, null, null, TableTools.CheckTask._ID + " asc LIMIT " + count);
        if (cursor != null) {
            List<UserInfo> list = new ArrayList<UserInfo>();
            while (cursor.moveToNext() && list.size() <= count) {
                UserInfo info = new UserInfo();
                info.setCheck_task_id(cursor.getString(0));
                info.setIdNumber(cursor.getString(5));
                info.setName(cursor.getString(5));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }

	public List<UserInfo> getUncheckedUser(boolean avatar) {
		return getUncheckedFamily(100, avatar);
	}
    public List<UserInfo> getUncheckedMember(String check_task_id, boolean avatar) {
        return getUncheckedFamilyMember(100, avatar, check_task_id);
    }

	public List<UserInfo> getUncheckedFamily(String check_task_id, boolean avatar, int count) {
        if (count > 100 || count < 0)
            count = 100;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.SB_CHECK_FAMILY_PROJECTION, TableTools.FamilyInfo.CHECK_TASK_ID + "=? and "+
                        TableTools.FamilyInfo.CHECKED + " =?", new String[] { String.valueOf(check_task_id), String.valueOf(0) },
                null, null, null);
        if (cursor != null) {
            List<UserInfo> list = new ArrayList<UserInfo>();
            while (cursor.moveToNext() && list.size() <= count) {

                UserInfo info;
                info = new UserInfo();
                info.setName(cursor.getString(3));
                info.setUserId(cursor.getInt(0));
                info.setIdNumber(cursor.getString(4));
                if (avatar)
                    info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
                info.setCheck_task_id(cursor.getString(1));
            }
            cursor.close();
            return list;
        }
        return null;
    }
    public List<UserInfo> getCheckedFamilyWithTaskID(String check_task_id, boolean avatar, int count) {
        if (count > 100 || count < 0)
            count = 100;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.SB_CHECK_FAMILY_PROJECTION, TableTools.FamilyInfo.CHECK_TASK_ID + "=? and "+
                        TableTools.FamilyInfo.CHECKED + " =?", new String[] { String.valueOf(check_task_id), String.valueOf(1) },
                null, null, null);
        if (cursor != null) {
            List<UserInfo> list = new ArrayList<UserInfo>();
            while (cursor.moveToNext() && list.size() <= count) {

                UserInfo info;
                info = new UserInfo();
                info.setName(cursor.getString(3));
                info.setUserId(cursor.getInt(0));
                info.setIdNumber(cursor.getString(4));
                if (avatar)
                    info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
                info.setCheck_task_id(cursor.getString(1));
            }
            cursor.close();
            return list;
        }
        return null;
    }
	public List<UserInfo> getUploadingFamilyWithTaskID(String check_task_id, boolean avatar, int count) {
		if (count > 100 || count < 0)
			count = 100;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.SB_CHECK_FAMILY_PROJECTION, TableTools.FamilyInfo.CHECK_TASK_ID + "=? and "+
						TableTools.FamilyInfo.CHECKED + " =?", new String[] { String.valueOf(check_task_id), String.valueOf(2) },
				null, null, null);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			while (cursor.moveToNext() && list.size() <= count) {

				UserInfo info;
				info = new UserInfo();
				info.setName(cursor.getString(3));
				info.setUserId(cursor.getInt(0));
				info.setIdNumber(cursor.getString(4));
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
				info.setCheck_task_id(cursor.getString(1));
			}
			cursor.close();
			return list;
		}
		return null;
	}
    public UserInfo getOneUncheckedMember(String cardID, String check_task_id, boolean avatar) {
		if (hasMember(cardID, check_task_id) == false) {
			return null;
		}
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION, TableTools.FamilyMember.CARD_ID + "=? and "+
                        TableTools.FamilyMember.CHECK_TASK_ID + " =?", new String[] { String.valueOf(cardID), String.valueOf(check_task_id) },
                null, null, null);
        if (cursor != null) {
			cursor.moveToFirst();
            UserInfo info;
			info = new UserInfo();
            info.setName(cursor.getString(3));
			info.setUserId(cursor.getInt(0));
			info.setIdNumber(cursor.getString(4));
			if (avatar)
				info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(18)));
			info.setCheck_task_id(cursor.getString(1));
            info.setFather_idNumber(cursor.getString(2));
			cursor.close();
			return info;
        }
        return null;
    }

    public void getFingerAndPhoto(UserInfo info) {
        SQLiteDatabase db =  mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION, TableTools.Attachment.CARD_ID + "=? and "+
                        TableTools.FamilyMember.CHECK_TASK_ID + " =?",
                new String[] { String.valueOf(info.getIdNumber()), String.valueOf(info.getCheck_task_id()) },  null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(String.valueOf(TYPE_IMAGE_PEOPLE))){
                    info.setNian_du_she_xiang(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(4)));
                }
                if(cursor.getString(0).equals(String.valueOf(TYPE_FINGER))){
                    info.setFinger(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(4)));
                }
                if(cursor.getString(0).equals(String.valueOf(TYPE_SIGNTURE_MANAGER))){
                    info.setManager_signture(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(4)));
                }
                if(cursor.getString(0).equals(String.valueOf(TYPE_SIGNTURE_USER))){
                    info.setUser_signture(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(4)));
                }
            }
        }
        return ;
    }

    public FamilyBase base1;
	public FamilyBase.member getOneUncheckedMember(String cardID, String check_task_id) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION, TableTools.FamilyMember.CARD_ID + "=? and "+
						TableTools.FamilyMember.CHECK_TASK_ID + " =?", new String[] { String.valueOf(cardID), String.valueOf(check_task_id) },
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
            base1 = new FamilyBase();
			FamilyBase.member info;
			info = base1.new member();
			info.setCyxm(cursor.getString(3));
			info.setCysfzh(cursor.getString(4));
			info.setXb(cursor.getString(5));
			info.setNl(cursor.getString(6));
			info.setLdnl(cursor.getString(7));
			info.setJkzk(cursor.getString(8));
			info.setCjlb(cursor.getString(9));
			info.setCjdj(cursor.getString(10));
			info.setWhcd(cursor.getString(11));
			info.setSfzx(cursor.getString(12));
			info.setSfzzp(cursor.getString(13));
			info.setHcdxzp(cursor.getString(14));
			info.setHcsfz(cursor.getString(15));
			info.setHchkb(cursor.getString(16));
			info.setYsqrgx(cursor.getString(17));
            info.setRyzt(cursor.getString(19));
            info.setSfz_status(cursor.getString(20));
            info.setCheck_task_id(check_task_id);
			cursor.close();
			return info;
		}
		return null;
	}


    public List<FamilyBase.member> getAllMembers(String check_task_id) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION,
                TableTools.FamilyMember.CHECK_TASK_ID + "=?", new String[] { String.valueOf(check_task_id) },
                null, null, null);

        if (cursor != null) {
            List<FamilyBase.member> list = new ArrayList<FamilyBase.member>();
            while (cursor.moveToNext() && list.size() <= 1000) {
                base1 = new FamilyBase();
                FamilyBase.member info;
                info = base1.new member();
                info.setCyxm(cursor.getString(3));
                info.setCysfzh(cursor.getString(4));
                info.setXb(cursor.getString(5));
                info.setNl(cursor.getString(6));
                info.setLdnl(cursor.getString(7));
                info.setJkzk(cursor.getString(8));
                info.setCjlb(cursor.getString(9));
                info.setCjdj(cursor.getString(10));
                info.setWhcd(cursor.getString(11));
                info.setSfzx(cursor.getString(12));
                info.setSfzzp(cursor.getString(13));
                info.setHcdxzp(cursor.getString(14));
                info.setHcsfz(cursor.getString(15));
                info.setHchkb(cursor.getString(16));
                info.setYsqrgx(cursor.getString(17));
                info.setRyzt(cursor.getString(19));
                info.setSfz_status(cursor.getString(20));
                info.setCheck_task_id(check_task_id);
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }

	public List<UserInfo> getUncheckedFamily(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECKED + "=?",
				new String[] { String.valueOf(0) }, null, null, TableTools.FamilyInfo._ID + " asc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(5));
				info.setName(cursor.getString(2));
				info.setIdNumber(cursor.getString(4));
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
				info.setType(cursor.getInt(7));
				info.setChecked(cursor.getInt(9) > 0);
                info.setCheck_task_id(cursor.getString(7));
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}

	public List<UserInfo> getUncheckedFamilyMember(int count, boolean avatar, String check_task_id) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION, TableTools.FamilyMember.CHECK_TASK_ID + "=?",
				new String[] { String.valueOf(check_task_id) }, null, null, TableTools.FamilyMember._ID + " asc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(1));
				info.setName(cursor.getString(3));
				info.setIdNumber(cursor.getString(4));
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(18)));
				info.setChecked(false);
                info.setCheck_task_id(cursor.getString(1));
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}
	public List<UserInfo> getCheckedUser(boolean avatar) {
		return getCheckedFamily(100, avatar);
	}

    public List<FamilyBase> getAllUploadingamily(int count) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        if (count > 100 || count < 0)
            count = 100;
        Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECKED + "=?",
                new String[] { String.valueOf("2") }, null, null, TableTools.FamilyInfo._ID + " desc LIMIT " + count);
        if (cursor != null) {
            List<FamilyBase> list = new ArrayList<FamilyBase>();
            FamilyBase info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new FamilyBase();

                info.setCheck_task_id(cursor.getString(7));
                info.setSqrsfzh(cursor.getString(4));
                info.setJzywlx(cursor.getString(1));
                info.setSqrq(cursor.getString(6));
                info.setSqrxm(cursor.getString(2));
                info.setXzqhdm(cursor.getString(3));
				info.setIsChecked(cursor.getString(9));
				info.setReqid(cursor.getString(10));
                info.setZyzpyy(cursor.getString(0));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }
    public List<FamilyBase> getAllCheckedFamily(int count) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        if (count > 100 || count < 0)
            count = 100;
        Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECKED + "=?",
                new String[] { String.valueOf("1") }, null, null, TableTools.FamilyInfo._ID + " desc LIMIT " + count);
        if (cursor != null) {
            List<FamilyBase> list = new ArrayList<FamilyBase>();
            FamilyBase info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new FamilyBase();

                info.setCheck_task_id(cursor.getString(7));
                info.setSqrsfzh(cursor.getString(4));
                info.setJzywlx(cursor.getString(1));
                info.setSqrq(cursor.getString(6));
                info.setSqrxm(cursor.getString(2));
                info.setXzqhdm(cursor.getString(3));
                info.setZyzpyy(cursor.getString(0));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }
	public List<UserInfo> getCheckedFamily(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECKED + "=?",
				new String[] { String.valueOf("1") }, null, null, TableTools.FamilyInfo._ID + " desc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(5));
                info.setCheck_task_id(cursor.getString(7));
				info.setName(cursor.getString(2));
				info.setIdNumber(cursor.getString(4));
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
				info.setChecked(cursor.getInt(9) > 0);
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}

    public FamilyBase getFamilyWithTaskID(String check_task_id) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECK_TASK_ID + "=?",
                new String[] { String.valueOf(check_task_id) }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            FamilyBase info;
            info = new FamilyBase();
            info.setJzywlx(cursor.getString(1));
            info.setSqrq(cursor.getString(6));
            info.setSqrsfzh(cursor.getString(4));
            info.setSqrxm(cursor.getString(2));
            info.setXzqhdm(cursor.getString(3));
            info.setZyzpyy(cursor.getString(0));
			info.setStatus(cursor.getString(9));
            info.setCheck_task_id(check_task_id);
            cursor.close();
            return info;
        }
        return null;
    }


	public List<UserInfo> getUploadingFamily(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION, TableTools.FamilyInfo.CHECKED + "=?",
				new String[] { String.valueOf("2") }, null, null, TableTools.FamilyInfo._ID + " desc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(5));
				info.setName(cursor.getString(2));
				info.setIdNumber(cursor.getString(4));
				if (avatar)
					info.setAvatar(BitmapUtils.getCircleBitmapFromByte(cursor.getBlob(8)));
                info.setCheck_task_id(cursor.getString(7));
				info.setChecked(cursor.getInt(9) > 0);
				list.add(info);
			}
			cursor.close();
			return list;
		}
		return null;
	}

    public CheckTask get_check_task(String check_task_id) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_CHECKTASK, TableTools.CHECK_TASK_PROJECTION,
                TableTools.CheckTask._ID + "=?", new String[] { String.valueOf(check_task_id) }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            CheckTask info = new CheckTask();
            info.setCheck_task_id(cursor.getString(0));
            info.setNd(cursor.getString(1));
            info.setDate(cursor.getString(2));
            info.setFzr(cursor.getString(3));
            info.setLx(cursor.getString(4));
            info.setTarget(cursor.getString(5));
			info.setServer(cursor.getString(6));
			info.setRpc(cursor.getString(7));
            return info;
        }
        return null;
    }

    public boolean insertOrUpdateMember(FamilyBase.member info) {
        boolean ret = false;
        if (hasMember(info)) {
            ret = updateMember(info);
        } else {
            ret = insertMember(info);
        }
        return ret;
    }

    public boolean insertOrUpdateMember(FamilyBase.member info, Bitmap pic) {
        boolean ret = false;
        if (hasMember(info)) {
            ret = updateMember(info, pic);
        } else {
            ret = insertMember(info, pic);
        }
        return ret;
    }

	public boolean hasMember(String cardID, String check_task_id) {

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION,
				TableTools.FamilyMember.CHECK_TASK_ID + "=? and " + TableTools.FamilyMember.CARD_ID + "=?"
				, new String[] { check_task_id, cardID }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}

    public boolean hasMember(FamilyBase.member info) {

        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor ret = db.query(TableTools.TABLE_MEMBERS, TableTools.SB_CHECK_MEMBERS_PROJECTION,
                TableTools.FamilyMember.CHECK_TASK_ID + "=? and " + TableTools.FamilyMember.CARD_ID + "=?"
                , new String[] { info.getCheck_task_id(), info.getCysfzh() }, null, null,
                null);
        if (ret != null && ret.getCount() > 0) {
            ret.close();
            return true;
        }
        return false;
    }

	public boolean updateMember(FamilyBase.member info)  {
		return updateMember(info, null);
	}
    public boolean updateMember(FamilyBase.member info, Bitmap pic)  {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.FamilyMember.CHECK_TASK_ID, info.getCheck_task_id());
        }
        if (info.getCyxm() != null) {
            values.put(TableTools.FamilyMember.NAME, info.getCyxm());
        }
        if (info.getXb() != null) {
            values.put(TableTools.FamilyMember.GENDER, info.getXb());
        }
        if (info.getNl() != null) {
            values.put(TableTools.FamilyMember.AGE, info.getNl());
        }
        if (info.getLdnl() != null) {
            values.put(TableTools.FamilyMember.LABER_ABAILITY, info.getLdnl());
        }
        if (info.getJkzk() != null) {
            values.put(TableTools.FamilyMember.HEALTHY_STATUS, info.getJkzk());
        }
        if (info.getCjlb() != null) {
            values.put(TableTools.FamilyMember.DISABILITY_TYPE, info.getCjlb());
        }
        if (info.getCjdj() != null) {
            values.put(TableTools.FamilyMember.DISABILITY_LEVEL, info.getCjdj());
        }
        if (info.getWhcd() != null) {
            values.put(TableTools.FamilyMember.EDUCATION_TYPE, info.getWhcd());
        }
        if (info.getSfzx() != null) {
            values.put(TableTools.FamilyMember.IS_AT_SCHOOL, info.getSfzx());
        }
        if (info.getSfzzp() != null) {
            values.put(TableTools.FamilyMember.IS_HAVE_HEAD_IMAGE, info.getSfzzp());
        }
        if (info.getHcdxzp() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_PHOTO, info.getHcdxzp());
        }
        if (info.getHcsfz() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_ID_CARD, info.getHcsfz());
        }
        if (info.getHchkb() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_HUKOUBEN, info.getHchkb());
        }
        if (info.getYsqrgx() != null) {
            values.put(TableTools.FamilyMember.RELATIONSHIP, info.getYsqrgx());
        }
        if (info.getFather_id() != null) {
            values.put(TableTools.FamilyMember.FATHER_CARD_ID, info.getFather_id());
        }
        if (info.getSfz_status() != null) {
            values.put(TableTools.FamilyMember.SFZ_STATUS, info.getSfz_status());
        }
        if (info.getRyzt() != null) {
            values.put(TableTools.FamilyMember.STATUS, info.getRyzt());
        }

		if (pic != null) {
			values.put(TableTools.FamilyMember.IMAGE, BitmapUtils.getBitmapByte(pic));
		}
        long ret = db.update(TableTools.TABLE_MEMBERS, values, TableTools.FamilyMember.CHECK_TASK_ID +
                "=? and " + TableTools.FamilyMember.CARD_ID + "=?",
                new String[] { String.valueOf(info.getCheck_task_id()),  String.valueOf(info.getCysfzh()) });
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertMember(FamilyBase.member info) {
        return insertMember(info , null);
    }

    public boolean insertMember(FamilyBase.member info, Bitmap pic)  {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.FamilyMember.CHECK_TASK_ID, info.getCheck_task_id());
        }
        if (info.getCyxm() != null) {
            values.put(TableTools.FamilyMember.NAME, info.getCyxm());
        }
        if (info.getCysfzh() != null) {
            values.put(TableTools.FamilyMember.CARD_ID, info.getCysfzh());
        }
        if (info.getXb() != null) {
            values.put(TableTools.FamilyMember.GENDER, info.getXb());
        }
        if (info.getNl() != null) {
            values.put(TableTools.FamilyMember.AGE, info.getNl());
        }
        if (info.getLdnl() != null) {
            values.put(TableTools.FamilyMember.LABER_ABAILITY, info.getLdnl());
        }
        if (info.getJkzk() != null) {
            values.put(TableTools.FamilyMember.HEALTHY_STATUS, info.getJkzk());
        }
        if (info.getCjlb() != null) {
            values.put(TableTools.FamilyMember.DISABILITY_TYPE, info.getCjlb());
        }
        if (info.getCjdj() != null) {
            values.put(TableTools.FamilyMember.DISABILITY_LEVEL, info.getCjdj());
        }
        if (info.getWhcd() != null) {
            values.put(TableTools.FamilyMember.EDUCATION_TYPE, info.getWhcd());
        }
        if (info.getSfzx() != null) {
            values.put(TableTools.FamilyMember.IS_AT_SCHOOL, info.getSfzx());
        }
        if (info.getSfzzp() != null) {
            values.put(TableTools.FamilyMember.IS_HAVE_HEAD_IMAGE, info.getSfzzp());
        }
        if (info.getHcdxzp() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_PHOTO, info.getHcdxzp());
        }
        if (info.getHcsfz() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_ID_CARD, info.getHcsfz());
        }
        if (info.getHchkb() != null) {
            values.put(TableTools.FamilyMember.IS_CHECK_HUKOUBEN, info.getHchkb());
        }
        if (info.getYsqrgx() != null) {
            values.put(TableTools.FamilyMember.RELATIONSHIP, info.getYsqrgx());
        }
        if (info.getFather_id() != null) {
            values.put(TableTools.FamilyMember.FATHER_CARD_ID, info.getFather_id());
        }
        if (info.getSfz_status() != null) {
            values.put(TableTools.FamilyMember.SFZ_STATUS, info.getSfz_status());
        }
        if (info.getRyzt() != null) {
            values.put(TableTools.FamilyMember.STATUS, info.getRyzt());
        }
        if (pic != null) {
            values.put(TableTools.FamilyMember.IMAGE, BitmapUtils.getBitmapByte(pic));
        }
        long ret = db.insert(TableTools.TABLE_MEMBERS, null, values);

        boolean rett = hasMember(info);
        UserInfo infoo = getOneUncheckedMember(info.getCysfzh(), info.getCheck_task_id(), true);
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertOrUpdateCheckTask(CheckTask info) {
        boolean ret = false;
        if (hasCheckTask(info)) {
            ret = updateCheckTask(info);
        } else {
            ret = insertCheckTask(info);
        }
        return ret;
    }

    public boolean hasCheckTask(CheckTask info) {

        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor ret = db.query(TableTools.TABLE_CHECKTASK, TableTools.CHECK_TASK_PROJECTION,
                TableTools.CheckTask._ID + "=?", new String[] { String.valueOf(info.getCheck_task_id()) }, null, null,
                null);
        if (ret != null && ret.getCount() > 0) {
            ret.close();
            return true;
        }
        return false;
    }
	public boolean hasCheckTask(String taskId) {

		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor ret = db.query(TableTools.TABLE_CHECKTASK, TableTools.CHECK_TASK_PROJECTION,
				TableTools.CheckTask._ID + "=?", new String[] { String.valueOf(taskId) }, null, null,
				null);
		if (ret != null && ret.getCount() > 0) {
			ret.close();
			return true;
		}
		return false;
	}
    public boolean updateCheckTask(CheckTask info) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.CheckTask._ID, info.getCheck_task_id());
        }
        if (info.getStatus() != null) {
            values.put(TableTools.CheckTask.STATUS, info.getStatus());
        }
        if (info.getFzr() != null) {
            values.put(TableTools.CheckTask.CHECK_OPERATOR, info.getFzr());
        }
        if (info.getNd() != null) {
            values.put(TableTools.CheckTask.CHECK_YEAR, info.getNd());
        }
        if (info.getDate() != null) {
            values.put(TableTools.CheckTask.CHECK_DATE, info.getDate());
        }
        if (info.getLx() != null) {
            values.put(TableTools.CheckTask.CHECK_TYPE, info.getLx());
        }

        if (info.getTarget() != null) {
            values.put(TableTools.CheckTask.CHECK_TARGET, info.getTarget());
        }

		if (info.getServer() != null) {
			values.put(TableTools.CheckTask.CHECK_SERVER, info.getServer());
		}

		if (info.getRpc() != null) {
			values.put(TableTools.CheckTask.CHECK_RPC, info.getRpc());
		}
        long ret = db.update(TableTools.TABLE_CHECKTASK, values, TableTools.CheckTask._ID + "=? ",
                new String[] { String.valueOf(info.getCheck_task_id()) });
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertCheckTask(CheckTask info)  {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.CheckTask._ID, info.getCheck_task_id());
        }
        if (info.getStatus() != null) {
            values.put(TableTools.CheckTask.STATUS, info.getStatus());
        }
        if (info.getFzr() != null) {
            values.put(TableTools.CheckTask.CHECK_OPERATOR, info.getFzr());
        }
        if (info.getNd() != null) {
            values.put(TableTools.CheckTask.CHECK_YEAR, info.getNd());
        }
        if (info.getDate() != null) {
            values.put(TableTools.CheckTask.CHECK_DATE, info.getDate());
        }
        if (info.getLx() != null) {
            values.put(TableTools.CheckTask.CHECK_TYPE, info.getLx());
        }
        if (info.getTarget() != null) {
            values.put(TableTools.CheckTask.CHECK_TARGET, info.getTarget());
        }

		if (info.getServer() != null) {
			values.put(TableTools.CheckTask.CHECK_SERVER, info.getServer());
		}

		if (info.getRpc() != null) {
			values.put(TableTools.CheckTask.CHECK_RPC, info.getRpc());
		}
        long ret = db.insert(TableTools.TABLE_CHECKTASK, null, values);
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertOrUpdateFamilyBase(FamilyBase info) {
        boolean ret = false;
        if (hasFamilyBase(info)) {
            ret = updateFamilyBase(info);
        } else {
            ret = insertFamilyBase(info);
        }
        return ret;
    }

    public boolean insertOrUpdateFamilyBase(FamilyBase info, Bitmap bmp) {
        boolean ret = false;
        if (hasFamilyBase(info)) {
            ret = updateFamilyBase(info);
        } else {
            ret = insertFamilyBase(info, bmp);
        }
        return ret;
    }

    public boolean hasFamilyBase(FamilyBase info) {

        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor ret = db.query(TableTools.TABLE_FAMILY_INFO, TableTools.FAMILY_INFO_PROJECTION,
                TableTools.FamilyInfo.CHECK_TASK_ID + "=? and " + TableTools.FamilyInfo.CARD_ID + "=?",
                new String[] { String.valueOf(info.getCheck_task_id()), String.valueOf(info.getSqrsfzh())  }, null, null,
                null);
        if (ret != null && ret.getCount() > 0) {
            ret.close();
            return true;
        }
        return false;
    }

    public boolean updateFamilyBase(FamilyBase info) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.FamilyInfo.CHECK_TASK_ID, info.getCheck_task_id());
        }
        if (info.getXzqhdm() != null) {
            values.put(TableTools.FamilyInfo.DISTRICT, info.getXzqhdm());
        }
        if (info.getSqrxm() != null) {
            values.put(TableTools.FamilyInfo.NAME, info.getSqrxm());
        }
        if (info.getSqrsfzh() != null) {
            values.put(TableTools.FamilyInfo.CARD_ID, info.getSqrsfzh());
        }
        if (info.getSqrq() != null) {
            values.put(TableTools.FamilyInfo.APPLY_TIME, info.getSqrq());
        }
        if (info.getJzywlx() != null) {
            values.put(TableTools.FamilyInfo.TYPE, info.getJzywlx());
        }
        if (info.getZyzpyy() != null) {
            values.put(TableTools.FamilyInfo.REASON, info.getZyzpyy());
        }
		if (info.getIsChecked() != null) {
			values.put(TableTools.FamilyInfo.CHECKED, info.getIsChecked());
		}
        if (info.getReqid() != null) {
            values.put(TableTools.FamilyInfo.REQ_ID, info.getReqid());
        }
        long ret = db.update(TableTools.TABLE_FAMILY_INFO, values, TableTools.FamilyInfo.CHECK_TASK_ID + "=? and "
                + TableTools.FamilyInfo.CARD_ID + "=?",
                new String[] { String.valueOf(info.getCheck_task_id()), String.valueOf(info.getSqrsfzh()) });
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertFamilyBase(FamilyBase info) {
        return insertFamilyBase(info, null);
    }
    public boolean insertFamilyBase(FamilyBase info, Bitmap bmp) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getCheck_task_id() != null) {
            values.put(TableTools.FamilyInfo.CHECK_TASK_ID, info.getCheck_task_id());
        }
        if (info.getXzqhdm() != null) {
            values.put(TableTools.FamilyInfo.DISTRICT, info.getXzqhdm());
        }
        if (info.getSqrxm() != null) {
            values.put(TableTools.FamilyInfo.NAME, info.getSqrxm());
        }
        if (info.getSqrsfzh() != null) {
            values.put(TableTools.FamilyInfo.CARD_ID, info.getSqrsfzh());
        }
        if (info.getSqrq() != null) {
            values.put(TableTools.FamilyInfo.APPLY_TIME, info.getSqrq());
        }
        if (info.getJzywlx() != null) {
            values.put(TableTools.FamilyInfo.TYPE, info.getJzywlx());
        }
        if (info.getZyzpyy() != null) {
            values.put(TableTools.FamilyInfo.REASON, info.getZyzpyy());
        }
		if (info.getIsChecked() != null) {
			values.put(TableTools.FamilyInfo.CHECKED, info.getIsChecked());
		}
        if (info.getReqid() != null) {
            values.put(TableTools.FamilyInfo.REQ_ID, info.getReqid());
        }

        if (bmp != null) {
            values.put(TableTools.FamilyInfo.IMAGE, BitmapUtils.getBitmapByte(bmp));
            bmp.recycle();
        }
        long ret = db.insert(TableTools.TABLE_FAMILY_INFO, null, values);
        if (ret != -1)
            return true;
        return false;
    }

	public boolean insertOrUpdateAttachmentVOD (Attachment info, byte[] content) {
		boolean ret = false;
		if (hasAttachment(info)) {
			ret = updateAttachmentVOD(info, content);
		} else {
			ret = insertAttachmentVOD(info, content);
		}
		return ret;
	}
	public boolean updateAttachmentVOD(Attachment attachment, byte[] content) {
		if (attachment == null || attachment.getCheck_task_id() == null)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TableTools.Attachment.CARD_ID, attachment.getCard_id());
		values.put(TableTools.Attachment.CHECK_TASK_ID, attachment.getCheck_task_id());
		values.put(TableTools.Attachment.METERIAL_NAME, attachment.getName());
		values.put(TableTools.Attachment.METERIAL_TYPE, TYPE_VIDEO);
		values.put(TableTools.Attachment.METERIAL_VOD_CONTENT, content);
		values.put(TableTools.Attachment.METERIAL_URI, attachment.getPath());

		long ret = db.update(TableTools.TABLE_ATTACHMENTS, values, TableTools.Attachment.CHECK_TASK_ID + "=? and "
						+ TableTools.Attachment.CARD_ID + "=? and "+ TableTools.Attachment.METERIAL_TYPE + "=?",
				new String[] { String.valueOf(attachment.getCheck_task_id()), String.valueOf(attachment.getCard_id()),
						String.valueOf(attachment.getType())});
		if (ret != -1) {
			return true;
		}
		return false;
	}

	public boolean insertAttachmentVOD(Attachment attachment, byte[] content) {
		if (attachment == null || attachment.getCheck_task_id() == null)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TableTools.Attachment.CARD_ID, attachment.getCard_id());
		values.put(TableTools.Attachment.CHECK_TASK_ID, attachment.getCheck_task_id());
		values.put(TableTools.Attachment.METERIAL_VOD_CONTENT, attachment.getName());
		values.put(TableTools.Attachment.METERIAL_NAME, attachment.getName());
		values.put(TableTools.Attachment.METERIAL_TYPE, TYPE_VIDEO);
		values.put(TableTools.Attachment.METERIAL_URI, attachment.getPath());

		long ret = db.insert(TableTools.TABLE_ATTACHMENTS, null, values);
		if (ret != -1) {
			return true;
		}
		return false;
	}

    public boolean insertOrUpdateAttachment(Attachment info) {
        boolean ret = false;
        if (hasAttachment(info)) {
            ret = updateAttachment(info);
        } else {
            ret = insertAttachment(info);
        }
        return ret;
    }

    public boolean hasAttachment(Attachment info) {

        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor ret = db.query(TableTools.TABLE_ATTACHMENTS, TableTools.SB_CHECK_ATTACHMENT_PROJECTION,
                TableTools.Attachment.CHECK_TASK_ID + "=? and " + TableTools.Attachment.CARD_ID + "=? and "
                        + TableTools.Attachment.METERIAL_TYPE + "=?",
                new String[] { String.valueOf(info.getCheck_task_id()), String.valueOf(info.getCard_id()), String.valueOf(info.getType()) }, null, null,
                null);
        if (ret != null && ret.getCount() > 0) {
            ret.close();
            return true;
        }
        return false;
    }


    public boolean updateAttachment(Attachment attachment) {
        if (attachment == null || attachment.getCheck_task_id() == null)
            return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableTools.Attachment.CARD_ID, attachment.getCard_id());
        values.put(TableTools.Attachment.CHECK_TASK_ID, attachment.getCheck_task_id());
        values.put(TableTools.Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(attachment.getContent()));
        values.put(TableTools.Attachment.METERIAL_NAME, attachment.getName());
        values.put(TableTools.Attachment.METERIAL_TYPE, attachment.getType());
        values.put(TableTools.Attachment.METERIAL_URI, attachment.getPath());

        long ret = db.update(TableTools.TABLE_ATTACHMENTS, values, TableTools.Attachment.CHECK_TASK_ID + "=? and "
                        + TableTools.Attachment.CARD_ID + "=? and "+ TableTools.Attachment.METERIAL_TYPE + "=?",
                new String[] { String.valueOf(attachment.getCheck_task_id()), String.valueOf(attachment.getCard_id()),
						String.valueOf(attachment.getType())});
        if (ret != -1) {
            return true;
        }
        return false;
    }

    // Attachment
    public boolean insertAttachment(Attachment attachment) {
        if (attachment == null || attachment.getCheck_task_id() == null)
            return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableTools.Attachment.CARD_ID, attachment.getCard_id());
        values.put(TableTools.Attachment.CHECK_TASK_ID, attachment.getCheck_task_id());
        values.put(TableTools.Attachment.METERIAL_CONTENT, BitmapUtils.getBitmapByte(attachment.getContent()));
        values.put(TableTools.Attachment.METERIAL_NAME, attachment.getName());
        values.put(TableTools.Attachment.METERIAL_TYPE, attachment.getType());
        values.put(TableTools.Attachment.METERIAL_URI, attachment.getPath());

        long ret = db.insert(TableTools.TABLE_ATTACHMENTS, null, values);
        if (ret != -1) {
            return true;
        }
        return false;
    }

    public boolean deleteAllAttachment(String task_id) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        int ret = db.delete(TableTools.TABLE_ATTACHMENTS, TableTools.Attachment.CHECK_TASK_ID + "=?",
                new String[] { String.valueOf(task_id)});
        if (ret > 0) {
            return true;
        }
        return false;
    }

	public boolean deleteAllMembers(String task_id) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		int ret = db.delete(TableTools.TABLE_MEMBERS, TableTools.FamilyMember.CHECK_TASK_ID + "=?",
				new String[] { String.valueOf(task_id)});
		if (ret > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteAllTasks(String task_id) {
        deleteAllAttachment(task_id);
        deleteAllMembers(task_id);
        deleteAllFamily(task_id);
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		int ret = db.delete(TableTools.TABLE_CHECKTASK, TableTools.CheckTask._ID + "=?",
				new String[] { String.valueOf(task_id)});
		if (ret > 0) {
			return true;
		}
		return false;
	}

	public boolean deleteAllFamily(String task_id) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		int ret = db.delete(TableTools.TABLE_FAMILY_INFO, TableTools.FamilyInfo.CHECK_TASK_ID + "=?",
				new String[] { String.valueOf(task_id)});
		if (ret > 0) {
			return true;
		}
		return false;
	}
    /**
     * 判断某张表是否存在
     * @param
     * @return
     */
    public boolean tabbleIsExist(String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+tableName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result;
    }
    public boolean deleteALLQuHuaMa() {
        SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUHUAMA);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_QUHUAMA + "(" +
                TableTools.QuHuaMa._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableTools.QuHuaMa.DEPTH + " TEXT, " +
                TableTools.QuHuaMa.FATHER_ID + " TEXT, " +
                TableTools.QuHuaMa.ID_NUMBER + " TEXT, " +
                TableTools.QuHuaMa.NAME + " TEXT); " );
        return true;
	}

    public List<QuHuaMa> getQuHuaMaWithFatherID(String father) {
        int count = 1000000;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_QUHUAMA, TableTools.QU_HUA_MA_PROJECTION,
                null, null, null, null, null);
        if (cursor != null) {
            List<QuHuaMa> list = new ArrayList<QuHuaMa>();
            QuHuaMa info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new QuHuaMa();
                info.setFather_id(cursor.getString(1));
                info.setName(cursor.getString(3));
                info.setDepth(cursor.getString(4));
                info.setId(cursor.getString(2));
                if (info.getFather_id().equals(father)) {
                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        return null;
    }
    public int insertQuHuaMa(QuHuaMa quHuaMa) {

        SQLiteDatabase db = mSQLiteHelper.getWritableDatabase();
        ContentValues QuHuaMa_Valuyes = new ContentValues();
        QuHuaMa_Valuyes.put(TableTools.QuHuaMa.DEPTH, quHuaMa.getDepth());
        QuHuaMa_Valuyes.put(TableTools.QuHuaMa.ID_NUMBER, quHuaMa.getId());
        QuHuaMa_Valuyes.put(TableTools.QuHuaMa.FATHER_ID, quHuaMa.getFather_id());
        QuHuaMa_Valuyes.put(TableTools.QuHuaMa.NAME, quHuaMa.getName());

        long ret = db.insert(TABLE_QUHUAMA, null, QuHuaMa_Valuyes);

        return (int)ret;
    }

    public List<QuHuaMa> getQuHuaMaWithDepth(String depdth) {
        int count = 1000000;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_QUHUAMA, TableTools.QU_HUA_MA_PROJECTION,
                null, null, null, null, null);
        if (cursor != null) {
            List<QuHuaMa> list = new ArrayList<QuHuaMa>();
            QuHuaMa info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new QuHuaMa();
                info.setFather_id(cursor.getString(1));
                info.setName(cursor.getString(3));
                info.setDepth(cursor.getString(4));
                info.setId(cursor.getString(2));
                if (info.getDepth().equals(depdth)) {
                    list.add(info);
                }
            }
            cursor.close();
            return list;
        }
        return null;
    }

    public List<QuHuaMa> getAllQuHuaMa() {
        int count = 1000000;
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_QUHUAMA, TableTools.QU_HUA_MA_PROJECTION,
                null, null, null, null, null);
        if (cursor != null) {
            List<QuHuaMa> list = new ArrayList<QuHuaMa>();
            QuHuaMa info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new QuHuaMa();
                info.setFather_id(cursor.getString(1));
                info.setName(cursor.getString(3));
                info.setDepth(cursor.getString(4));
                info.setId(cursor.getString(2));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }
    public boolean insertOrUpdateDownloadTask(DownloadTask info) {
        boolean ret = false;
        if (hasDownloadTask(info)) {
            ret = updateDownloadTask(info);
        } else {
            ret = insertDownloadTask(info);
        }
        return ret;
    }

	public List<DownloadTask> getDownloadTaskQuHuMa(int count) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_DOWNLOAD_TASK, TableTools.DOWNLOAD_TASK_PROJECTION, null, null, null, null,
				null);
		if (cursor != null) {
			List<DownloadTask> list = new ArrayList<DownloadTask>();
			DownloadTask info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new DownloadTask();
				info.setApply_type(cursor.getString(1));
				info.setNow_work_page(cursor.getString(2));
				info.setQu_hua_ma(cursor.getString(3));
				info.setPage_number(cursor.getString(4));
				info.setNow_work_target(cursor.getString(5));
				info.setNow_work_target_apply_time(cursor.getString(6));
				info.setPage_id(cursor.getString(7));
				info.setTotal_number(cursor.getString(8));
				if (info.getQu_hua_ma() != null) {
					list.add(info);
				}
			}
			cursor.close();
			return list;
		}
		return null;
	}

    public List<DownloadTask> getDownloadTask(int count) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_DOWNLOAD_TASK, TableTools.DOWNLOAD_TASK_PROJECTION, null, null, null, null,
                null);
        if (cursor != null) {
            List<DownloadTask> list = new ArrayList<DownloadTask>();
            DownloadTask info;
            while (cursor.moveToNext() && list.size() <= count) {
                info = new DownloadTask();
                info.setApply_type(cursor.getString(1));
                info.setNow_work_page(cursor.getString(2));
                info.setQu_hua_ma(cursor.getString(3));
                info.setPage_number(cursor.getString(4));
                info.setNow_work_target(cursor.getString(5));
                info.setNow_work_target_apply_time(cursor.getString(6));
                info.setPage_id(cursor.getString(7));
                info.setTotal_number(cursor.getString(8));
                list.add(info);
            }
            cursor.close();
            return list;
        }
        return null;
    }

    public DownloadTask getDownloadTask(String QuHuaMa) {
        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TableTools.TABLE_DOWNLOAD_TASK, TableTools.DOWNLOAD_TASK_PROJECTION,
                TableTools.Download_Task.NOW_WORK_QUHUAMA + "=?", new String[] { String.valueOf(QuHuaMa) }, null, null,
                null);
        DownloadTask info = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                info = new DownloadTask();
                info.setApply_type(cursor.getString(1));
                info.setNow_work_page(cursor.getString(2));
                info.setQu_hua_ma(cursor.getString(3));
                info.setPage_number(cursor.getString(4));
                info.setNow_work_target(cursor.getString(5));
                info.setNow_work_target_apply_time(cursor.getString(6));
                info.setPage_id(cursor.getString(7));
                info.setTotal_number(cursor.getString(8));
            }
            cursor.close();
        }
        return info;
    }

    public boolean hasDownloadTask(DownloadTask info) {

        SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();

        if (info.getQu_hua_ma() != null) {
            Cursor ret = db.query(TableTools.TABLE_DOWNLOAD_TASK, TableTools.DOWNLOAD_TASK_PROJECTION,
                    TableTools.Download_Task.NOW_WORK_QUHUAMA + "=?", new String[]{String.valueOf(info.getQu_hua_ma())}, null, null,
                    null);
            if (ret != null && ret.getCount() > 0) {
                ret.close();
                return true;
            }
        } else if (info.getNow_work_target() != null){

            Cursor ret = db.query(TableTools.TABLE_DOWNLOAD_TASK, TableTools.DOWNLOAD_TASK_PROJECTION,
                    TableTools.Download_Task.NOW_WORK_TARGET + "=?", new String[]{String.valueOf(info.getNow_work_target())}, null, null,
                    null);
            if (ret != null && ret.getCount() > 0) {
                ret.close();
                return true;
            }
        }
        return false;
    }

    public boolean updateDownloadTask(DownloadTask info) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getApply_type() != null) {
            values.put(TableTools.Download_Task.APPLY_TYPE, info.getApply_type());
        }
        if (info.getNow_work_page() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_PAGE, info.getNow_work_page());
        }
        if (info.getQu_hua_ma() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_QUHUAMA, info.getQu_hua_ma());
        }
        if (info.getPage_number() != null) {
            values.put(TableTools.Download_Task.PAGE_NUMBER, info.getPage_number());
        }
        if (info.getNow_work_target() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_TARGET, info.getNow_work_target());
        }
        if (info.getNow_work_target_apply_time() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_TARGET_APPLY_TIME, info.getNow_work_target_apply_time());
        }
        if (info.getPage_id() != null) {
            values.put(TableTools.Download_Task.PAGE_ID, info.getPage_id());
        }
        if (info.getTotal_number() != null) {
            values.put(TableTools.Download_Task.TOTAL_NUMBER, info.getTotal_number());
        }
        long ret;
        if (info.getQu_hua_ma()!= null) {
            ret = db.update(TableTools.TABLE_DOWNLOAD_TASK, values, TableTools.Download_Task.NOW_WORK_QUHUAMA + "=? ",
                    new String[]{String.valueOf(info.getQu_hua_ma())});
        } else {
            ret = db.update(TableTools.TABLE_DOWNLOAD_TASK, values, TableTools.Download_Task.NOW_WORK_TARGET + "=? ",
                    new String[]{String.valueOf(info.getNow_work_target())});
        }
        if (ret != -1)
            return true;
        return false;
    }

    public boolean insertDownloadTask(DownloadTask info)  {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getApply_type() != null) {
            values.put(TableTools.Download_Task.APPLY_TYPE, info.getApply_type());
        }
        if (info.getNow_work_page() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_PAGE, info.getNow_work_page());
        }
        if (info.getQu_hua_ma() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_QUHUAMA, info.getQu_hua_ma());
        }
        if (info.getPage_number() != null) {
            values.put(TableTools.Download_Task.PAGE_NUMBER, info.getPage_number());
        }
        if (info.getNow_work_target() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_TARGET, info.getNow_work_target());
        }
        if (info.getNow_work_target_apply_time() != null) {
            values.put(TableTools.Download_Task.NOW_WORK_TARGET_APPLY_TIME, info.getNow_work_target_apply_time());
        }
        if (info.getPage_id() != null) {
            values.put(TableTools.Download_Task.PAGE_ID, info.getPage_id());
        }
        if (info.getTotal_number() != null) {
            values.put(TableTools.Download_Task.TOTAL_NUMBER, info.getTotal_number());
        }

        long ret = db.insert(TableTools.TABLE_DOWNLOAD_TASK, null, values);
        if (ret != -1)
            return true;
        return false;
    }

    public boolean deleteDownloadTask(DownloadTask task) {
        SQLiteDatabase db = getWritableDatabase();
        if (task.getQu_hua_ma() != null) {
            long ret = db.delete(TableTools.TABLE_DOWNLOAD_TASK, TableTools.Download_Task.NOW_WORK_QUHUAMA + "=?",
                    new String[] { String.valueOf(task.getQu_hua_ma())});
            if (ret != -1)
                return true;
            return false;
        } else if (task.getNow_work_target() != null) {
            long ret = db.delete(TableTools.TABLE_DOWNLOAD_TASK, TableTools.Download_Task.NOW_WORK_TARGET + "=?",
                    new String[] { String.valueOf(task.getNow_work_target())});
            if (ret != -1)
                return true;
            return false;

        }

        return false;
    }

}
