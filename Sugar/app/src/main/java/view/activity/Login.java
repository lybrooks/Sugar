package view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.Result;
import cn.sugar.bean.UserBean;
import cn.sugar.db.UserDao;
import day.sugar.R;
import model.net.IModelLogin;
import model.net.ModelLogin;
import model.utils.CommonUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import model.utils.ResultUtils;
import model.utils.SharedPerfenceUtils;

public class Login extends BaseActivity {


    @Bind(R.id.Et_UserName)
    EditText EtUserName;
    @Bind(R.id.ET_Password)
    EditText ETPassword;

    Login mContext;
    IModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EtUserName.setText(getIntent().getStringExtra("username"));
        mContext = this;
        modelLogin = new ModelLogin();
    }


    @OnClick({R.id.iv_back, R.id.btn_Login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.btn_Login:
                final ProgressDialog pb = new ProgressDialog(mContext);
                pb.show();
                final String user = EtUserName.getText().toString().trim();
                String password = ETPassword.getText().toString().trim();
                modelLogin.Login(this, user, password, new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = ResultUtils.getResultFromJson(s, UserBean.class);
                        if (result == null) {
                            CommonUtils.showLongToast("登录失败");
                        } else {
                            if (result.isRetMsg()) {
                                UserBean user = (UserBean) result.getRetData();
                                UserDao dao = new UserDao(mContext);
                                dao.savaUser(user);
                                boolean isSuccess =dao.savaUser(user);
                                if (isSuccess){
                                    SharedPerfenceUtils.getInstance(mContext).saveuser(user.getMuserName());
                                    SugarApplication.getInstance().setUserBean(user);
                                    MFGT.finish(mContext);
                                }else {
                                        CommonUtils.showLongToast("user_database_error");
                                }
                                MFGT.gotoMainActivity(mContext);
                                MFGT.finish(mContext);
                            } else {
                                if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                    CommonUtils.showLongToast("login_fail_unknown_user");
                                } else if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                    CommonUtils.showLongToast("login_fail_error_password");
                                } else {
                                    CommonUtils.showLongToast("login_fail");
                                }
                            }
                        }
                        pb.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        pb.dismiss();
                    }
                });
                break;
//            case R.id.btn_regist:
//                Intent intent = newgoods Intent(this, Regist.class);
//                startActivity(intent);
//                MFGT.finish(mContext);
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MFGT.finish(mContext);
    }
}
