package model.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.sugar.I;
import cn.sugar.bean.CategoryChildBean;
import view.activity.Boutique;
import view.activity.BuyActivity;
import view.activity.CategoryDetails;
import view.activity.ChooseActivity;
import view.activity.Collections;
import view.activity.Deails;
import view.activity.Login;
import view.activity.MainActivity;
import day.sugar.R;
import view.activity.MySetting;
import view.activity.UpdateNick;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void gotoChooseActivity(Activity context) {
        startActivity(context, ChooseActivity.class);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoGoodsDtails(Activity context, int Goodsid) {
        Intent intent = new Intent(context, Deails.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, Goodsid);
        startActivity(context, intent);
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoCategoryDtails(Activity context, int id, ArrayList<CategoryChildBean> list, String name) {
        Intent intent = new Intent(context, CategoryDetails.class);
        intent.putExtra(I.CategoryChild.CAT_ID, id);
        intent.putExtra("childList", list);
        intent.putExtra(I.CategoryGroup.NAME, name);
        startActivity(context, intent);
    }

    public static void gotoBoutique(Activity context, String Title, int cariId) {
        Intent intent = new Intent(context, Boutique.class);
        intent.putExtra("title", Title);
        intent.putExtra("cartId", cariId);
        startActivity(context, intent);

    }

    public static void gotoRegister(Activity context) {
        // Intent intent = newgoods Intent(context, Regist.class);
        // startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);
    }


    public static void gotoLogin(Activity mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, Login.class);
        startActivityForResult(mContext, intent, I.REQUEST_CODE_LOGIN);
    }

    private static void startActivityForResult(Activity mContext, Intent intent, int requestCode) {
        mContext.startActivityForResult(intent, requestCode);
        mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void goSettingActivity(Activity mContext) {
        Intent intent = new Intent();
        intent.setClass(mContext, MySetting.class);
        startActivity(mContext, intent);

    }

    public static void goUpdateNick(Activity mContext) {

        startActivityForResult(mContext, new Intent(mContext, UpdateNick.class), I.REQUEST_CODE_NICK);
    }

    public static void gotoCollection(Activity comtext) {
        startActivity(comtext, Collections.class);
    }

    public static void goPayActivity(Context context, String cartId) {
        Intent intent = new Intent(context, BuyActivity.class).putExtra(I.Cart.ID, cartId);
        startActivity((Activity) context, intent);
    }

}
