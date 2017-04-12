package view.activity;

import android.app.Application;

import cn.sugar.bean.UserBean;

/**
 * Created by Administrator on 2016/10/17.
 */
public class SugarApplication extends Application {
    private static SugarApplication instance;
    public static SugarApplication application;

    public String getUsernane() {
        return Usernane;
    }

    public void setUsernane(String usernane) {
        Usernane = usernane;
    }

    String Usernane;

    public static UserBean getUserBean() {
        return userBean;
    }

    public  void setUserBean(UserBean userBean) {
     this.userBean=userBean;
    }

    public static UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }

    public static SugarApplication getInstance() {
        if (instance == null) {
            instance = new SugarApplication();
        }
        return instance;
    }
}
