package model.net;

import android.content.Context;

import cn.sugar.I;
import cn.sugar.bean.CartBean;
import cn.sugar.bean.MessageBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ModelCart implements IModelCart {
    @Override
    public void downUserCart(Context context, String name, OkHttpUtils.OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, name)
                .targetClass(CartBean[].class)
                .execute(listener);

    }

    @Override
    public void deleteCart(Context context, int goodId, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID, String.valueOf(goodId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void addCar(Context context, int goodId, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID, String.valueOf(goodId))
                .addParam(I.Cart.USER_NAME, username)
                .addParam(I.Cart.COUNT, 1 + "")
                .addParam(I.Cart.IS_CHECKED, false + "")
                .targetClass(MessageBean.class)
                .execute(listener);

    }

    @Override
    public void updateCar(Context context, int cartId, int count, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, String.valueOf(cartId))
                .addParam(I.Cart.COUNT, String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(0))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void downloadCollecCounts(Context context, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Cart.USER_NAME, username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    @Override
    public void realse() {
        OkHttpUtils.release();

    }
}
