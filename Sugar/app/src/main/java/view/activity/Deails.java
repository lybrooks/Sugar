package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sugar.I;
import cn.sugar.bean.AlbumsBean;
import cn.sugar.bean.GoodsDetailsBean;
import cn.sugar.bean.MessageBean;
import cn.sugar.bean.UserBean;
import day.sugar.R;
import model.net.IModelCart;
import model.net.IModelCollections;
import model.net.IModleNewGoods;
import model.net.ModelCart;
import model.net.ModelCollections;
import model.net.NewGoods;
import model.utils.CommonUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import presenter.IPresenterGoodsDetails;
import view.widget.FlowIndicator;
import view.widget.SlideAutoLoopView;


public class Deails extends AppCompatActivity {


    @Bind(R.id.tv_good_EnglishName)
    TextView tvGoodEnglishName;
    @Bind(R.id.tv_good_deails_name)
    TextView tvGoodDeailsName;
    @Bind(R.id.tv_good_prize)
    TextView tvGoodPrize;
    @Bind(R.id.SALV_goods_dials)
    SlideAutoLoopView SALVGoodsDials;
    @Bind(R.id.FID)
    FlowIndicator FID;
    @Bind(R.id.tv_goods_dsc)
    WebView tvGoodsDsc;


    int goodId;

    Deails mComtext;
    UserBean user;
    @Bind(R.id.iv_collect)
    ImageView ivCollect;

    boolean isCollected;
    IPresenterGoodsDetails mPresenter;
    IModelCollections collections;
    IModelCart cart;
    IModleNewGoods newGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deails);
        ButterKnife.bind(this);
        mComtext = this;
        user = SugarApplication.getUserBean();
        Intent intent = getIntent();
        goodId = intent.getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        collections = new ModelCollections();
        cart = new ModelCart();
        newGoods = new NewGoods();
        initView();
        initData();
    }

    private void initView() {
//        mPresenter = new PresenterGoodsDetails<>(this);
//        mPresenter.downloadGoodsDetails(this, goodId);
    }

    private void initData() {

        newGoods.downloadGoodsDetail(this, goodId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodsDetails(result);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showGoodsDetails(GoodsDetailsBean details) {
        tvGoodEnglishName.setText(details.getGoodsEnglishName());
        tvGoodDeailsName.setText(details.getGoodsName());
        tvGoodPrize.setText(details.getCurrencyPrice());
        SALVGoodsDials.startPlayLoop(FID, getAlbumImgUrl(details), getCount(details));
        tvGoodsDsc.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getCount(GoodsDetailsBean details) {
        if (details.getProperties() != null && details.getProperties().length > 0) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] url = new String[]{};
        if (details.getProperties() != null && details.getProperties().length > 0) {
            AlbumsBean[] albumsBeen = details.getProperties()[0].getAlbums();
            url = new String[albumsBeen.length];
            for (int i = 0; i < albumsBeen.length; i++) {
                url[i] = albumsBeen[i].getImgUrl();
            }
        }
        return url;
    }


    @OnClick({R.id.iv_back, R.id.iv_main_cart, R.id.iv_collect, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_main_cart:
                UserBean userBean = SugarApplication.getUserBean();
                if (userBean != null) {
                    cart.addCar(mComtext, goodId, userBean.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                CommonUtils.showLongToast("添加购物车成功");
                            } else {
                                CommonUtils.showShortToast("添加购物车失败");
                            }
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast("添加失败");
                        }
                    });
                } else {
                    MFGT.gotoLogin(mComtext);
                }

                break;
            case R.id.iv_collect:
                if (isCollected) {
                    deleteCollected();
                } else {
                    addCollect();
                }
                break;
            case R.id.iv_share:
                showShare();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
// 启动分享GUI
        oks.show(this);
    }

    private void addCollect() {
        collections.collectGoods(mComtext, goodId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    isCollected = true;
                } else {
                    isCollected = false;
                }
                updateGoodsCollectStatus();
                CommonUtils.showLongToast("添加收藏成功");
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast("添加收藏失败");
            }
        });
    }

    private void deleteCollected() {
        collections.deleteCollect(mComtext, goodId, user.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null && result.isSuccess()) {
                    isCollected = false;
                } else {
                    isCollected = true;
                }
                updateGoodsCollectStatus();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void updateGoodsCollectStatus() {
        if (isCollected) {
            ivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivCollect.setImageResource(R.mipmap.coolected);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserBean userBean = SugarApplication.getUserBean();
        if (userBean != null) {
            collections.isCollected(mComtext, goodId, userBean.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result.isSuccess()) {
                        isCollected = true;
                    } else {
                        isCollected = false;
                    }
                    updateGoodsCollectStatus();
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

//    @Override
//    public void showError(String error) {
//
//    }
//
//    @Override
//    public void showResult(GoodsDetailsBean result) {
//        if (result != null) {
//            showGoodsDetails(result);
//        }
//    }
}
