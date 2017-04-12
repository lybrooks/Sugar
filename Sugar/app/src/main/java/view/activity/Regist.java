package view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.bean.Result;
import cn.sugar.bean.UserBean;
import model.net.IModelLogin;
import model.net.ModelLogin;
import model.utils.CommonUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import model.utils.ResultUtils;
import day.sugar.R;

public class Regist extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.Et_UserName)
    EditText EtUserName;
    @Bind(R.id.ET_Nick)
    EditText ETNick;
    @Bind(R.id.Et_Password_regist)
    EditText EtPasswordRegist;
    @Bind(R.id.ET_rePassword)
    EditText ETRePassword;
    @Bind(R.id.btn_regist)
    Button btnRegist;


    static String username;
    static String nick;
    static String password;
    static String repassword;
    IModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        modelLogin = new ModelLogin();
    }

    private void initData() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        modelLogin.UserRegister(this, username, nick, password, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserBean.class);
                pd.dismiss();
                if (result.getRetData() == null) {
                    CommonUtils.showShortToast(R.string.register_fail);
                } else {
                    if (result.isRetMsg()) {
                        Toast.makeText(Regist.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Regist.this, Login.class);
                        intent.putExtra("username", username);
                        //setResult(RESULT_OK,intent);
                        startActivity(intent);
                        MFGT.finish(Regist.this);
                    } else {
                        CommonUtils.showLongToast(R.string.register_fail_exists);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }


    @OnClick(R.id.btn_regist)
    public void onClick() {
        username = EtUserName.getText().toString().trim();
        nick = ETNick.getText().toString().trim();
        password = EtPasswordRegist.getText().toString().trim();
        repassword = ETRePassword.getText().toString().trim();
        if (username.isEmpty()) {
            EtUserName.setError("用户名不能为空");
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            EtUserName.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z0-9]\\w{5,15}")) {
            CommonUtils.showShortToast(R.string.illegal_user_name);
            EtUserName.requestFocus();
            return;
        } else if (nick.isEmpty()) {
            CommonUtils.showShortToast(R.string.nick_name_connot_be_empty);
            ETNick.requestFocus();
            return;
        } else if (password.isEmpty()) {
            EtPasswordRegist.setError("密码不能为空");
            CommonUtils.showShortToast(R.string.password_connot_be_empty);
            EtPasswordRegist.requestFocus();
            return;
        } else if (repassword.isEmpty()) {
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
            ETRePassword.requestFocus();
            return;
        } else if (!repassword.equals(password)) {
            ETRePassword.setError("密码不一致");
            ETRePassword.requestFocus();
            return;
        }
        initData();

    }
}
