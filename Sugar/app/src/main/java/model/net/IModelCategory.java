package model.net;

import android.content.Context;

import cn.sugar.bean.CategoryChildBean;
import cn.sugar.bean.CategoryGroupBean;
import cn.sugar.bean.NewGoodsBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModelCategory extends IModelBase {

    void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener);

    void downloadCategoryChild(Context context, int parent_id, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener);

    void downloadCategoryGoods(Context context, int cartId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener);
}
