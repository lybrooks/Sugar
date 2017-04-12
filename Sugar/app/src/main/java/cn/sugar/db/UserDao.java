package cn.sugar.db;
import android.content.Context;

import cn.sugar.bean.UserBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public class UserDao {

    public  static final String USER_TABLE_NAME="t_superwechat_user";
    public  static final String USER_COLUME_NAME="m_user_name";
    public  static final String USER_COLUME_NICK="m_user_nick";
    public  static final String USER_COLUME_AVATAR_ID="m_user_avatar_id";
    public  static final String USER_COLUME_AVATAR_PATH="m_user_avatar_path";
    public  static final String USER_COLUME_AVATAR_SUFFIX="m_user_avatar_suffix";
    public  static final String USER_COLUMEE_AVATAR_TYPE="m_user_avatar_type";
    public  static final String USER_COLUME_AVATAR_LASTUPDATE_TIME="m_user_avatar_lastupdate_time";


    public  UserDao(Context context){
        DBManager.getInstance().onInit(context);
    }
    public boolean savaUser(UserBean user){
        return DBManager.getInstance().saveUser(user);
    }
    public boolean update(UserBean user){
        return DBManager.getInstance().update(user);
    }
    public UserBean getUser(String username){
        return DBManager.getInstance().getuser(username);
    }


}
