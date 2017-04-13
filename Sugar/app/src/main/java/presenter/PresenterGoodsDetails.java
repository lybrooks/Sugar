package presenter;

import android.content.Context;

import cn.sugar.bean.GoodsDetailsBean;
import model.net.IModleNewGoods;
import model.net.NewGoods;
import model.utils.OkHttpUtils;
import view.views.IGoodsDetailsView;

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

public class PresenterGoodsDetails<T> extends PresenterBaseImage implements IPresenterGoodsDetails<IGoodsDetailsView> {
    IModleNewGoods mModel;
    IGoodsDetailsView<GoodsDetailsBean> mGoodsDetailsView;

    public PresenterGoodsDetails(IGoodsDetailsView<GoodsDetailsBean> goodsDetailsView) {
        super(goodsDetailsView);
        mModel = new NewGoods();
        mGoodsDetailsView = goodsDetailsView;
    }

    @Override
    public void downloadGoodsDetails(Context context, int goodsId) {
        mModel.downloadGoodsDetail(context, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                mGoodsDetailsView.showResult(result);
            }

            @Override
            public void onError(String error) {
                mGoodsDetailsView.showError(error);
            }
        });
    }
}
