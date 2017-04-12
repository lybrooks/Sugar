package presenter;

import android.content.Context;

import cn.sugar.bean.NewGoodsBean;
import model.net.IModleNewGoods;
import model.utils.OkHttpUtils;
import view.views.INewGoodsView;

/**
 * All rights Reserved, Designed By www.tydic.com
 *
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * Created by guowang on 2017/4/12.
 * @date: ${date} ${time}
 * @Copyright: ${year} www.tydic.com Inc. All rights reserved.
 */

public class PresenterNewGoods implements IPresenterNewGoods {
    IModleNewGoods mModel;
    INewGoodsView<NewGoodsBean[]> mNewGoodsView;



    @Override
    public void downloadGoodsList(Context context, int catId, int pageId, final int action) {
        mModel.downloadNewGoods(context, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mNewGoodsView.showResult(result, action);
            }

            @Override
            public void onError(String error) {
                mNewGoodsView.showError(error);
            }
        });

    }

    @Override
    public void realse() {

    }
}
