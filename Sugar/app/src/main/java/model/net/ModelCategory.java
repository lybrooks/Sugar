package model.net;

import android.content.Context;

import cn.sugar.I;
import cn.sugar.bean.CategoryChildBean;
import cn.sugar.bean.CategoryGroupBean;
import cn.sugar.bean.NewGoodsBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ModelCategory implements IModelCategory {
    /**
     * 下载分类首页数据
     */
    @Override
    public void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<CategoryGroupBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    /**
     * 下载分类二级页面
     */

    @Override
    public void downloadCategoryChild(Context context, int parent_id, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {

        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<CategoryChildBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID, parent_id + "")
                .addParam(I.PAGE_ID, I.PAGE_ID_DEFAULT + "")
                .addParam(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    /**
     * 下载分类页面商品详情
     */

    @Override
    public void downloadCategoryGoods(Context context, int cartId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<NewGoodsBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(cartId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    @Override
    public void realse() {
        OkHttpUtils.release();

    }
}
