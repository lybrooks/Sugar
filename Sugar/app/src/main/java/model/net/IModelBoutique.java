package model.net;

import android.content.Context;

import cn.sugar.bean.BoutiqueBean;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public interface IModelBoutique extends IModelBase {

    void downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener);
}
