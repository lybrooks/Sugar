package view.activity;


import android.os.Bundle;

import cn.sugar.bean.UserBean;
import cn.sugar.db.UserDao;
import day.sugar.R;
import model.utils.MFGT;
import model.utils.SharedPerfenceUtils;

public class SplashActivity extends BaseActivity {
    final long splashtime = 2000;
    SplashActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {

        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long start = System.currentTimeMillis();
                long costtime = System.currentTimeMillis() - start;
                if (splashtime - costtime > 0) {
                    try {
                        Thread.sleep(splashtime - costtime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    UserBean user = SugarApplication.getUserBean();
                    String username = SharedPerfenceUtils.getInstance(mContext).getUser();
                    if (user == null && username != null) {
                        UserDao dao = new UserDao(mContext);
                        user = dao.getUser(username);
                        if (user != null) {
                            SugarApplication.getInstance().setUserBean(user);
                            MFGT.gotoMainActivity(SplashActivity.this);
                        } else {
                            MFGT.gotoChooseActivity(SplashActivity.this);
                        }
                    } else if (user == null && username == null) {
                        MFGT.gotoChooseActivity(SplashActivity.this);
                    }
                    finish();
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MFGT.finish(SplashActivity.this);
    }
}
