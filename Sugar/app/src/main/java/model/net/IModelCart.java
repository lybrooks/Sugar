package model.net;

import android.content.Context;

import cn.sugar.bean.CartBean;
import cn.sugar.bean.MessageBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModelCart extends IModelBase {
    /**
     * 下载用户购物车信息
     */
    void downUserCart(Context context, String name, OkHttpUtils.OnCompleteListener<CartBean[]> listener);

    /**
     * 删除购物车信息
     */
    void deleteCart(Context context, int goodId, OkHttpUtils.OnCompleteListener<MessageBean> listener);

    /**
     * 添加购物车
     */
    void addCar(Context context, int goodId, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener);

    void updateCar(Context context, int cartId, int count, OkHttpUtils.OnCompleteListener<MessageBean> listener);

    void downloadCollecCounts(Context context, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener);
}
