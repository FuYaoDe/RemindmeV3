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
 * @author cxa ��Ʈw�ާ@��k
 * 
 */

public class TaskDbProvider extends ContentProvider {
	// ��Ʈw�W�ٱ`��
	public static final String DATABASE_NAME = "Remindme_Task.db";
	// ��Ʈw�����`��
	public static final int DATABASE_VERSION = 2;
	// ��ƪ�W�ٱ`��
	public static final String TASK_LIST_TABLE_NAME = "RemindmeTask";
	// �d����춰�X
	private static HashMap<String, String> sTaskListProjectionMap;
	// �d�ߡB��s����
	private static final int TASKS = 1;
	private static final int TASK_ID = 2;
	// Uri�u�����O
	private static final UriMatcher sUriMatcher;
	// ��Ʈw�u�����O���
	private DatabaseHelper mOpenHelper;

	// �����u�����O�A�إߩζ}�Ҹ�Ʈw�B�إߩΧR����ƪ�
	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// �إ߸�ƪ�
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ TASK_LIST_TABLE_NAME
					+ " ("
					+ TaskCursor.KEY.KEY_ID + " INTEGER PRIMARY KEY autoincrement,"//0
					+ TaskCursor.KEY.TITTLE + " TEXT," // 1
					+ TaskCursor.KEY.CREATED+ " TEXT," // 2
					+ TaskCursor.KEY.END_DATE + " TEXT,"//3
					+ TaskCursor.KEY.END_TIME + " TEXT,"//4
					+ TaskCursor.KEY.CONTENT+ " TEXT," // 5
					+ TaskCursor.KEY.LOCATION_NAME + " TEXT,"//6
					+ TaskCursor.KEY.COORDINATE + " TEXT,"//
					+ TaskCursor.KEY.DISTANCE+ " TEXT,"//
					+ TaskCursor.KEY.LEVEL	+ " INTEGER," // 21
					+ TaskCursor.KEY.PRIORITY+ " INTEGER," // 19
					+ TaskCursor.KEY.COLLABORATOR+ " TEXT," // 20
					+ TaskCursor.KEY.CAL_ID + " INTEGER," // 12
					+ TaskCursor.KEY.GOOGOLE_CAL_SYNC_ID + " INTEGER,"
					+ TaskCursor.KEY.OTHER + " TEXT," // 21
					+ TaskCursor.KEY.IS_FIXED+ " TEXT," // 21
					+ TaskCursor.KEY.IS_REPERT + " TEXT"
					+ ");");
		}

		// �R����ƪ�
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + TASK_LIST_TABLE_NAME);
			onCreate(db);
		}
	}

	// �إߩζ}�Ҹ�Ʈw
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	// �d��
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		// �d�ߩҦ��u�@
		case TASKS:
			qb.setTables(TASK_LIST_TABLE_NAME);
			qb.setProjectionMap(sTaskListProjectionMap);
			break;
		// �ھ�ID�d��
		case TASK_ID:
			qb.setTables(TASK_LIST_TABLE_NAME);
			qb.setProjectionMap(sTaskListProjectionMap);
			qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Uri���~�I " + uri + "/"
					+ sUriMatcher.match(uri));
		}

		// �ϥιw�]�Ƨ�
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = CommonUtils.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// ���o��Ʈw���
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		// ��^��ж��X
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	// ���o����
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case TASKS:
			return CommonUtils.CONTENT_TYPE;
		case TASK_ID:
			return CommonUtils.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("���~�� URI�I " + uri);
		}
	}

	// �O�s���
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != TASKS) {
			throw new IllegalArgumentException("���~�� URI�I " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		// ���o��Ʈw���
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		// �O�s��ƪ�^���ID
		long rowId = db.insert(TASK_LIST_TABLE_NAME, TaskCursor.KEY.CONTENT,
				values);
		if (rowId > 0) {
			Uri taskUri = ContentUris.withAppendedId(CommonUtils.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(taskUri, null);
			return taskUri;
		}
		throw new SQLException("���J��ƥ��� " + uri);
	}

	// �R�����
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// ���o��Ʈw���
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		// �ھګ��w����R��
		case TASKS:
			count = db.delete(TASK_LIST_TABLE_NAME, where, whereArgs);
			break;
		// �ھګ��w����MID�R��
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
			throw new IllegalArgumentException("���~�� URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// ��s���
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// ���o��Ʈw���
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		// �ھګ��w�����s
		case TASKS:
			count = db.update(TASK_LIST_TABLE_NAME, values, where, whereArgs);
			break;
		// �ھګ��w����MID��s
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
			throw new IllegalArgumentException("���~�� URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		// Uriƥ�ǰt�u�����O
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CommonUtils.AUTHORITY, CommonUtils.TASKLIST, TASKS);
		sUriMatcher.addURI(CommonUtils.AUTHORITY, CommonUtils.TASKLIST + "/#",
				TASK_ID);

		// ��ҤƬd����춰�X
		sTaskListProjectionMap = new HashMap<String, String>();
		// �K�[�d�����
		sTaskListProjectionMap.put(BaseColumns._ID, BaseColumns._ID);
		sTaskListProjectionMap.put(TaskCursor.KEY.GOOGOLE_CAL_SYNC_ID,
				TaskCursor.KEY.GOOGOLE_CAL_SYNC_ID);
		sTaskListProjectionMap
				.put(TaskCursor.KEY.CAL_ID, TaskCursor.KEY.CAL_ID);
		sTaskListProjectionMap
				.put(TaskCursor.KEY.TITTLE, TaskCursor.KEY.TITTLE);
		sTaskListProjectionMap.put(TaskCursor.KEY.END_DATE,
				TaskCursor.KEY.END_DATE);
		sTaskListProjectionMap.put(TaskCursor.KEY.END_TIME,
				TaskCursor.KEY.END_TIME);
		sTaskListProjectionMap.put(TaskCursor.KEY.LOCATION_NAME,
				TaskCursor.KEY.LOCATION_NAME);
		sTaskListProjectionMap.put(TaskCursor.KEY.DISTANCE,
				TaskCursor.KEY.DISTANCE);
		sTaskListProjectionMap.put(TaskCursor.KEY.PRIORITY,
				TaskCursor.KEY.PRIORITY);
		sTaskListProjectionMap.put(TaskCursor.KEY.IS_REPERT,
				TaskCursor.KEY.IS_REPERT);
		sTaskListProjectionMap.put(TaskCursor.KEY.CONTENT,
				TaskCursor.KEY.CONTENT);
		sTaskListProjectionMap.put(TaskCursor.KEY.CREATED,
				TaskCursor.KEY.CREATED);
		sTaskListProjectionMap.put(TaskCursor.KEY.OTHER, TaskCursor.KEY.OTHER);
		sTaskListProjectionMap.put(TaskCursor.KEY.COLLABORATOR,
				TaskCursor.KEY.COLLABORATOR);
		sTaskListProjectionMap.put(TaskCursor.KEY.COORDINATE,
				TaskCursor.KEY.COORDINATE);
		sTaskListProjectionMap.put(TaskCursor.KEY.LEVEL, TaskCursor.KEY.LEVEL);
		sTaskListProjectionMap.put(TaskCursor.KEY.IS_FIXED,
				TaskCursor.KEY.IS_FIXED);

	}
}
