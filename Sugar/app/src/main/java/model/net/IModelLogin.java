package model.net;

import android.content.Context;

import java.io.File;

import view.activity.MySetting;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModelLogin extends IModelBase {

    void Login(Context context, String username, String password, OkHttpUtils.OnCompleteListener<String> listener);

    void UserRegister(Context context, String username, String nick, String password, OkHttpUtils.OnCompleteListener<String> litener);

    void updateNick(Context context, String username, String nick, OkHttpUtils.OnCompleteListener<String> listener);

    void updateAvatar(MySetting context, String muserName, File file, OkHttpUtils.OnCompleteListener<String> listener);

    void findUserByUserName(Context context, String username, OkHttpUtils.OnCompleteListener<String> listener);
}
