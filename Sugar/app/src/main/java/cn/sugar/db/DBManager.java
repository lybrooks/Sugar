package cn.sugar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.sugar.bean.UserBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private static DBOpenHelper mHelper;

    void onInit(Context context) {
        mHelper = new DBOpenHelper(context);
    }

    public static synchronized DBManager getInstance() {
        return dbMgr;
    }

    public static synchronized void closeDB() {
        if (mHelper != null) {
            mHelper.CloseDB();
        }
    }

    /**
     * 查找用户信息
     */
    public synchronized UserBean getuser(String username) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where "
                + UserDao.USER_COLUME_NAME + " =?";
        UserBean user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToNext()) {
            user = new UserBean();
            user.setMuserName(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUME_NAME)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUME_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMEE_AVATAR_TYPE)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUME_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUME_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUME_AVATAR_LASTUPDATE_TIME)));
            return user;
        }
        return user;
    }

    /**
     * 存储用户信息
     */
    public synchronized boolean saveUser(UserBean user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUME_NAME, user.getMuserName());
        values.put(UserDao.USER_COLUME_NICK, user.getMuserNick());
        values.put(UserDao.USER_COLUME_AVATAR_ID, user.getMavatarId());
        values.put(UserDao.USER_COLUMEE_AVATAR_TYPE, user.getMavatarType());
        values.put(UserDao.USER_COLUME_AVATAR_PATH, user.getMavatarPath());
        values.put(UserDao.USER_COLUME_AVATAR_SUFFIX, user.getMavatarSuffix());
        values.put(UserDao.USER_COLUME_AVATAR_LASTUPDATE_TIME, user.getMavatarLastUpdateTime());
        if (db.isOpen()) {
            return db.replace(UserDao.USER_TABLE_NAME, null, values) != -1;
        }
        return false;
    }

    /**
     * 修改用户信息
     */
    public synchronized boolean update(UserBean user) {
        int resule = -1;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = UserDao.USER_COLUME_NAME + "=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUME_NICK, user.getMuserNick());
        if (db.isOpen()) {
            resule = db.update(UserDao.USER_TABLE_NAME, values, sql, new String[]{user.getMuserName()});
        }
        return resule > 0;

    }
}
