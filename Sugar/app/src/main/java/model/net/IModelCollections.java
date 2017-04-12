package model.net;

import android.content.Context;

import cn.sugar.bean.CollectBean;
import cn.sugar.bean.MessageBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModelCollections extends IModelBase {
    void downloadCollections(Context context, String username, int pageId, OkHttpUtils.OnCompleteListener<CollectBean[]> listener);

    void collectGoods(Context context, int goodId, String muserName, OkHttpUtils.OnCompleteListener<MessageBean> listener);

    void deleteCollect(Context context, int goodId, String name, OkHttpUtils.OnCompleteListener<MessageBean> listener);

    void isCollected(Context context, int goodId, String name, OkHttpUtils.OnCompleteListener<MessageBean> listener);
}
