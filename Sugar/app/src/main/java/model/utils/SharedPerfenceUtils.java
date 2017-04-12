package model.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24.
 */
public class SharedPerfenceUtils {

    private static final String SHARED_NAME = "saveUserInfo";
    private static SharedPerfenceUtils instance;
    private SharedPreferences msharedPreferences;
    private SharedPreferences.Editor meditor;
    public static final String SHARE_KEY_USER_NAME = "share_key_user_name";

    public  SharedPerfenceUtils(Context context) {
        msharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        meditor = msharedPreferences.edit();
    }

    public static SharedPerfenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPerfenceUtils(context);
        }
        return instance;
    }

    public void saveuser(String username) {
        meditor.putString(SHARE_KEY_USER_NAME, username);
        meditor.commit();
    }

    public String getUser() {
        return msharedPreferences.getString(SHARE_KEY_USER_NAME, null);
    }

    public void removeUser() {
        meditor.remove(SHARE_KEY_USER_NAME);
        meditor.commit();
    }

}
