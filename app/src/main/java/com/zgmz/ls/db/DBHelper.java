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
import com.zgmz.ls.model.District;
import com.zgmz.ls.model.FamilyIncome;
import com.zgmz.ls.model.FamilyProperty;
import com.zgmz.ls.model.FamilySituation;
import com.zgmz.ls.model.FamilySpending;
import com.zgmz.ls.model.FingerPrint;
import com.zgmz.ls.model.IdCard;
import com.zgmz.ls.model.PreviewInfo;
import com.zgmz.ls.model.Signature;
import com.zgmz.ls.model.UserInfo;
import com.zgmz.ls.utils.BitmapUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

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
		preview.setYearPhoto(getYearPhoto(userId));
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

	// Attachment
	public boolean insertAttachment(Attachment attachment) {
		if (attachment == null || attachment.getUserId() <= 0)
			return false;
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AttachmentColumn.USER_ID, attachment.getUserId());
		values.put(AttachmentColumn.NAME, attachment.getName());
		values.put(AttachmentColumn.PATH, attachment.getPath());

		long time = System.currentTimeMillis();
		values.put(AttachmentColumn.CREATED_AT, time);
		values.put(AttachmentColumn.UPDATED_AT, time);
		long ret = db.insert(TableTools.TABLE_ATTACHMENT, null, values);
		if (ret != -1) {
			return true;
		}
		return false;
	}
	
	
	
	public void updateAttachmentFlag(int userId) {
		updateUserFlag(userId, UserInfoColumn.F_ATTACH, true);
	}
	
	
	public void updateYearPhotoFlag(int userId) {
		updateUserFlag(userId, UserInfoColumn.F_YEAR_PHOTO, true);
	}
	
	public int getAttachmentCount(int userId) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ATTACHMENT, TableTools.ATTACHMENT_PROJECTION, AttachmentColumn.USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		
		return count;
	}

	public boolean deleteAttachment(Attachment attachment) {
		if (attachment == null)
			return false;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		int ret = db.delete(TableTools.TABLE_ATTACHMENT,
				AttachmentColumn.USER_ID + "=? and " + AttachmentColumn.PATH + "=? ",
				new String[] { String.valueOf(attachment.getUserId()), attachment.getPath() });
		if (ret > 0) {
			if(getAttachmentCount(attachment.getUserId()) <= 0)
				updateUserFlag(attachment.getUserId(), UserInfoColumn.F_ATTACH, false);
			return true;
		}
		return false;
	}
	
	
	public Attachment getYearPhoto(int userId) {
		if (userId <= 0) return null;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ATTACHMENT, TableTools.ATTACHMENT_PROJECTION, AttachmentColumn.USER_ID + "=? and "+ AttachmentColumn.NAME +" = ?",
				new String[] { String.valueOf(userId), new String("year"+userId) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			Attachment info;
			info = new Attachment();
			info.setUserId(cursor.getInt(0));
			info.setName(cursor.getString(1));
			info.setPath(cursor.getString(2));
			cursor.close();
			return info;
		}
		return null;
	}
	
	public List<Attachment> getAttachments(int userId) {
		if (userId <= 0) return null;
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query(TableTools.TABLE_ATTACHMENT, TableTools.ATTACHMENT_PROJECTION, AttachmentColumn.USER_ID + "=? and "+ AttachmentColumn.NAME +" != ?",
				new String[] { String.valueOf(userId), new String("year"+userId) }, null, null, null);
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

	public List<UserInfo> getUncheckedUser(boolean avatar) {
		return getUncheckedUser(100, avatar);
	}

	public List<UserInfo> getUncheckedUser(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION, UserInfoColumn.CHECKED + "=?",
				new String[] { String.valueOf(0) }, null, null, UserInfoColumn._ID + " asc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(0));
				info.setName(cursor.getString(1));
				info.setIdNumber(cursor.getString(2));
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

	public List<UserInfo> getCheckedUser(boolean avatar) {
		return getCheckedUser(100, avatar);
	}

	public List<UserInfo> getCheckedUser(int count, boolean avatar) {
		SQLiteDatabase db = mSQLiteHelper.getReadableDatabase();
		if (count > 100 || count < 0)
			count = 100;
		Cursor cursor = db.query(TableTools.TABLE_USER, TableTools.USER_INFO_PROJECTION, UserInfoColumn.CHECKED + "!=?",
				new String[] { String.valueOf(0) }, null, null, UserInfoColumn.UPDATED_AT + " desc LIMIT " + count);
		if (cursor != null) {
			List<UserInfo> list = new ArrayList<UserInfo>();
			UserInfo info;
			while (cursor.moveToNext() && list.size() <= count) {
				info = new UserInfo();
				info.setUserId(cursor.getInt(0));
				info.setName(cursor.getString(1));
				info.setIdNumber(cursor.getString(2));
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

}
