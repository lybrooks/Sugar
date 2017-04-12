package model.net;

import android.content.Context;

import cn.sugar.bean.GoodsDetailsBean;
import cn.sugar.bean.NewGoodsBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModleNewGoods extends IModelBase {
    void downloadNewGoods(Context context, int cartId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener);

    void downloadNewGoods(Context context, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener);

    void downloadGoodsDetail(Context context, int goodsId, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener);
}
