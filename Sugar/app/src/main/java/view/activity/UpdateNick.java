package view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import model.utils.L;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import model.utils.ResultUtils;

public class UpdateNick extends AppCompatActivity {

    @Bind(R.id.et_nick)
    EditText etNick;

    String muserName;
    String usernick;

    UpdateNick mContext;
    UserBean userBean;
    IModelLogin modelLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        modelLogin = new ModelLogin();
        userBean = SugarApplication.getUserBean();
        if (userBean != null) {
            muserName = userBean.getMuserName();
            usernick = userBean.getMuserNick();
        } else {
            finish();
        }
    }

    private void initData() {
        etNick.setText(usernick);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {

        if (userBean != null) {
            usernick = etNick.getText().toString();
            if (usernick == null) {
                CommonUtils.showLongToast("昵称不能为空");
                return;
            } else {
                if (usernick.equals(userBean.getMuserNick())) {
                    CommonUtils.showLongToast("未作出任何修改");
                    return;
                } else {
                    updateNick(usernick);
                    return;
                }
            }
        }

    }

    private void updateNick(String usernick) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.show();
        modelLogin.updateNick(mContext, muserName, usernick, new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = ResultUtils.getResultFromJson(s, UserBean.class);
                        L.e("UpdateNick:"+result.toString());
                        if (result == null) {
                            CommonUtils.showLongToast("修改昵称失败");
                        } else {
                            if (result.isRetMsg()) {
                                L.e(result.toString());
                                UserBean U = (UserBean) result.getRetData();
                                UserDao dao = new UserDao(mContext);
                                boolean isSuccess = dao.update(U);
                                if (isSuccess) {
                                    SugarApplication.getInstance().setUserBean(U);
                                    L.e("Update:SugarApplication" + U.toString());
                                    CommonUtils.showShortToast("修改昵称成功");
                                    MFGT.finish(mContext);
                                } else {
                                    if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                                        CommonUtils.showLongToast("update_fail");
                                    } else if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                        CommonUtils.showLongToast("update_fail");
                                    } else {
                                        CommonUtils.showLongToast("update_fail");
                                    }
                                }

                            }

                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showLongToast("Update:" + error);
                    }
                }
        );
    }
}
