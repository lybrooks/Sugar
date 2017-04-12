package model.net;

import android.content.Context;

import cn.sugar.I;
import cn.sugar.bean.BoutiqueBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ModelBoutique implements IModelBoutique {
    /**
     * 下载精选一级页面
     */
    @Override
    public void downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<BoutiqueBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    @Override
    public void realse() {
        OkHttpUtils.release();
    }
}
