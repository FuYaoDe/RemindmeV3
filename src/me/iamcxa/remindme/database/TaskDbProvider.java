/**
 * 
 */
package me.iamcxa.remindme.database;

import java.util.HashMap;

import me.iamcxa.remindme.CommonUtils;
import me.iamcxa.remindme.CommonUtils.TaskCursor;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * @author cxa 資料庫操作方法
 * 
 */

public class TaskDbProvider extends ContentProvider {
	// 資料庫名稱常數
	public static final String DATABASE_NAME = "Remindme_Task.db";
	// 資料庫版本常數
	public static final int DATABASE_VERSION = 2;
	// 資料表名稱常數
	public static final String TASK_LIST_TABLE_NAME = "RemindmeTask";
	// 查詢欄位集合
	private static HashMap<String, String> sTaskListProjectionMap;
	// 查詢、更新條件
	private static final int TASKS = 1;
	private static final int TASK_ID = 2;
	// Uri工具類別
	private static final UriMatcher sUriMatcher;
	// 資料庫工具類別實例
	private DatabaseHelper mOpenHelper;

	// 內部工具類別，建立或開啟資料庫、建立或刪除資料表
	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		// 建立資料表
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ TASK_LIST_TABLE_NAME
					+ " ("
					+ TaskCursor.KeyColumns.KEY_ID // 0
					+ " INTEGER PRIMARY KEY autoincrement,"
					+ TaskCursor.KeyColumns.Tittle // 2
					+ " TEXT," // 2
					+ TaskCursor.KeyColumns.StartDate
					+ " TEXT," 
					+ TaskCursor.KeyColumns.EndDate
					+ " TEXT," 
					+ TaskCursor.KeyColumns.StartTime // 3
					+ " TEXT,"
					+ TaskCursor.KeyColumns.EndTime
					+ " TEXT," 
					+ TaskCursor.KeyColumns.Is_Repeat
					+ " INTEGER,"
					+ TaskCursor.KeyColumns.Is_AllDay
					+ " INTEGER," 
					+ TaskCursor.KeyColumns.LocationName
					+ " TEXT," 
					+ TaskCursor.KeyColumns.Coordinate
					+ " TEXT," 
					+ TaskCursor.KeyColumns.Distance
					+ " TEXT," 
					+ TaskCursor.KeyColumns.CONTENT
					+ " TEXT," // 13
					+ TaskCursor.KeyColumns.CREATED
					+ " TEXT," // 14
					+ TaskCursor.KeyColumns.Is_Alarm_ON
					+ " INTEGER,"// 15
					+ TaskCursor.KeyColumns.Is_Hide_ON
					+ " INTEGER," // 16
					+ TaskCursor.KeyColumns.Is_PW_ON
					+ " INTEGER," // 17
					+ TaskCursor.KeyColumns.Password
					+ " TEXT," // 18
					+ TaskCursor.KeyColumns.Priority
					+ " INTEGER," // 19
					+ TaskCursor.KeyColumns.Collaborator + " TEXT," // 20
					+ TaskCursor.KeyColumns.GoogleCalSyncID 
					+ " TEXT,"
					+ TaskCursor.KeyColumns.CalendarID
					+ " TEXT," // 12
					+ TaskCursor.KeyColumns.Other + " TEXT," // 21
					+ TaskCursor.KeyColumns.Level + " TEXT," // 21
					+ TaskCursor.KeyColumns.Is_Fixed + " TEXT" // 21				
					
					+ ");");
		}

		// 刪除資料表
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + TASK_LIST_TABLE_NAME);
			onCreate(db);
		}
	}

	// 建立或開啟資料庫
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	// 查詢
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		// 查詢所有工作
		case TASKS:
			qb.setTables(TASK_LIST_TABLE_NAME);
			qb.setProjectionMap(sTaskListProjectionMap);
			break;
		// 根據ID查詢
		case TASK_ID:
			qb.setTables(TASK_LIST_TABLE_NAME);
			qb.setProjectionMap(sTaskListProjectionMap);
			qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Uri錯誤！ " + uri + "/"
					+ sUriMatcher.match(uri));
		}

		// 使用預設排序
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = CommonUtils.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// 取得資料庫實例
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		// 返回游標集合
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	// 取得類型
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case TASKS:
			return CommonUtils.CONTENT_TYPE;
		case TASK_ID:
			return CommonUtils.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("錯誤的 URI！ " + uri);
		}
	}

	// 保存資料
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != TASKS) {
			throw new IllegalArgumentException("錯誤的 URI！ " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		// 取得資料庫實例
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		// 保存資料返回欄位ID
		long rowId = db.insert(TASK_LIST_TABLE_NAME,
				TaskCursor.KeyColumns.CONTENT, values);
		if (rowId > 0) {
			Uri taskUri = ContentUris.withAppendedId(CommonUtils.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(taskUri, null);
			return taskUri;
		}
		throw new SQLException("插入資料失敗 " + uri);
	}

	// 刪除資料
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// 取得資料庫實例
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		// 根據指定條件刪除
		case TASKS:
			count = db.delete(TASK_LIST_TABLE_NAME, where, whereArgs);
			break;
		// 根據指定條件和ID刪除
		case TASK_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(TASK_LIST_TABLE_NAME,
					BaseColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("錯誤的 URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// 更新資料
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// 取得資料庫實例
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		// 根據指定條件更新
		case TASKS:
			count = db.update(TASK_LIST_TABLE_NAME, values, where, whereArgs);
			break;
		// 根據指定條件和ID更新
		case TASK_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(TASK_LIST_TABLE_NAME, values,
					BaseColumns._ID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("錯誤的 URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		// Uriぁ匹配工具類別
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CommonUtils.AUTHORITY, CommonUtils.TASKLIST, TASKS);
		sUriMatcher.addURI(CommonUtils.AUTHORITY, CommonUtils.TASKLIST + "/#",
				TASK_ID);

		// 實例化查詢欄位集合
		sTaskListProjectionMap = new HashMap<String, String>();
		// 添加查詢欄位
		sTaskListProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
		sTaskListProjectionMap.put(
				TaskCursor.KeyColumns.GoogleCalSyncID,
				TaskCursor.KeyColumns.GoogleCalSyncID);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.CalendarID,
				TaskCursor.KeyColumns.CalendarID);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Tittle,
				TaskCursor.KeyColumns.Tittle);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.StartDate,
				TaskCursor.KeyColumns.StartDate);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.EndDate,
				TaskCursor.KeyColumns.EndDate);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.StartTime,
				TaskCursor.KeyColumns.StartTime);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.EndTime,
				TaskCursor.KeyColumns.EndDate);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.LocationName,
				TaskCursor.KeyColumns.LocationName);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Distance,
				TaskCursor.KeyColumns.Distance);
		sTaskListProjectionMap.put(
				TaskCursor.KeyColumns.Priority,
				TaskCursor.KeyColumns.Priority);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_Repeat,
				TaskCursor.KeyColumns.Is_Repeat);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_AllDay,
				TaskCursor.KeyColumns.Is_AllDay);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.CONTENT,
				TaskCursor.KeyColumns.CONTENT);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.CREATED,
				TaskCursor.KeyColumns.CREATED);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_Alarm_ON,
				TaskCursor.KeyColumns.Is_Alarm_ON);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_Hide_ON,
				TaskCursor.KeyColumns.Is_Hide_ON);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_PW_ON,
				TaskCursor.KeyColumns.Is_PW_ON);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Password,
				TaskCursor.KeyColumns.Password);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Other,
				TaskCursor.KeyColumns.Other);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Collaborator,
				TaskCursor.KeyColumns.Collaborator);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Coordinate,
				TaskCursor.KeyColumns.Coordinate);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Level,
				TaskCursor.KeyColumns.Level);
		sTaskListProjectionMap.put(TaskCursor.KeyColumns.Is_Fixed,
				TaskCursor.KeyColumns.Is_Fixed);

	}
}
