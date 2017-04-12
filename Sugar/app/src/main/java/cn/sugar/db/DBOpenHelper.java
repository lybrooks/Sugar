package cn.sugar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.sugar.I;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static DBOpenHelper instance;
    private static final int DATABASE_VERSION = 1;
    private static final String FULICENTER_USER_TABLE_CREATE = "CREATE TABLE "
            +UserDao.USER_TABLE_NAME +" ("
            +UserDao.USER_COLUME_NAME+" TEXT PRIMARY KEY, "
            +UserDao.USER_COLUME_NICK+" TEXT, "
            +UserDao.USER_COLUME_AVATAR_ID+" INTEGER, "
            +UserDao.USER_COLUMEE_AVATAR_TYPE+" INTEGER, "
            + UserDao.USER_COLUME_AVATAR_PATH+" TEXT, "
            +UserDao.USER_COLUME_AVATAR_SUFFIX+" TEXT, "
            +UserDao.USER_COLUME_AVATAR_LASTUPDATE_TIME+" TEXT);";

    public static DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DBOpenHelper(Context context) {
        super(context, getUserDataBaseName(), null, DATABASE_VERSION);
    }

    private static String getUserDataBaseName() {
        return I.User.TABLE_NAME + "demo.db";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FULICENTER_USER_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void CloseDB() {
        if (instance != null) {
            SQLiteDatabase db = instance.getWritableDatabase();
            db.close();
            instance = null;
        }
    }
}
