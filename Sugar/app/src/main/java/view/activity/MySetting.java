package view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.Result;
import cn.sugar.bean.UserBean;
import model.net.IModelLogin;
import model.net.ModelLogin;
import model.utils.CommonUtils;
import model.utils.ImageLoader;
import model.utils.L;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import model.utils.OnSetAvatarListener;
import model.utils.ResultUtils;
import model.utils.SharedPerfenceUtils;
import day.sugar.R;

public class MySetting extends AppCompatActivity {

    @Bind(R.id.iv_User_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.tv_Username)
    TextView tvUsername;
    @Bind(R.id.iv_update_name)
    ImageView ivUpdateName;
    @Bind(R.id.tv_UserNick)
    TextView tvUserNick;
    @Bind(R.id.iv_update_nick)
    ImageView ivUpdateNick;
    @Bind(R.id.bt_Quit)
    Button btQuit;

    MySetting mContext;
    UserBean userBean;

    OnSetAvatarListener setAvatarListener;

    IModelLogin modelLogin;

    Activity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        modelLogin = new ModelLogin();
        userBean = SugarApplication.getUserBean();
        if (userBean == null) {
            finish();
            return;
        } else {
            showInfo();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }

    private void showInfo() {
        userBean = SugarApplication.getUserBean();
        tvUsername.setText(userBean.getMuserName());
        tvUserNick.setText(userBean.getMuserNick());
        ImageLoader.setAcatar(ImageLoader.getAcatarUrl(userBean), mContext, ivUserAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        setAvatarListener.setAvatar(requestCode, data, ivUserAvatar);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_NICK) {
            CommonUtils.showLongToast("update_user_nick_success");
        }
        if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
            updateAavatar();
        }
    }

    private void updateAavatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext, userBean.getMavatarPath() + "/" + userBean.getMuserName()
                + I.AVATAR_SUFFIX_JPG));
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.show();
        modelLogin.updateAvatar(mContext, userBean.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserBean.class);
                L.e("UpdateAvatar:" + result.toString());
                if (result == null) {
                    CommonUtils.showLongToast("上传用户头像失败");
                } else {
                    UserBean u = (UserBean) result.getRetData();
                    if (result.isRetMsg()) {
                        SugarApplication.getInstance().setUserBean(u);
                        ImageLoader.setAcatar(ImageLoader.getAcatarUrl(u), mContext, ivUserAvatar);
                        CommonUtils.showLongToast("修改用户头像成功");
                    } else {
                        CommonUtils.showLongToast("上传用户头像失败");
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast("上传用户头像失败");
            }
        });
    }

    @OnClick({R.id.RL_UserAvatar, R.id.LL_UpdateUsername, R.id.LL_UpdateNick, R.id.bt_Quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RL_UserAvatar:
                setAvatarListener = new OnSetAvatarListener(mContext, R.id.Setting, userBean.getMuserName(), I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.LL_UpdateUsername:
                CommonUtils.showLongToast("用户名不能被更改");
                break;
            case R.id.LL_UpdateNick:
                MFGT.goUpdateNick(mContext);
                break;
            case R.id.bt_Quit:
                finish();
//                logout();
                break;
        }
    }

    private void logout() {
        if (userBean != null) {
            SharedPerfenceUtils.getInstance(mContext).removeUser();
            SugarApplication.getInstance().setUserBean(null);
            MFGT.gotoChooseActivity(mContext);
        }
        finish();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        this.finish();
    }
}
