package model.utils;

import android.content.Context;

import java.io.File;

import cn.sugar.I;
import cn.sugar.bean.BoutiqueBean;
import cn.sugar.bean.CartBean;
import cn.sugar.bean.CategoryChildBean;
import cn.sugar.bean.CategoryGroupBean;
import cn.sugar.bean.CollectBean;
import cn.sugar.bean.GoodsDetailsBean;
import cn.sugar.bean.MessageBean;
import cn.sugar.bean.NewGoodsBean;
import view.activity.MySetting;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NetDao {
    /**
     * 下载精选二级页面
     */
    public static void downloadNewGoods(Context context, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<NewGoodsBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }


    /**
     * 下载商品详情
     */
    public static void downloadGoodsDetail(Context context, int goodsId, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<GoodsDetailsBean>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID, goodsId + "")
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    /**
     * 下载新品页面
     */
    public static void downloadNewGoods(Context context, int cartId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<NewGoodsBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(cartId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }


    /**
     * 下载精选一级页面
     */
    public static void downloadBoutique(Context context, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener) {
        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils<BoutiqueBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);

    }

    /**
     * 下载分类首页大类的数据
     */
    public static void downloadCategoryGroup(Context context, OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<CategoryGroupBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);

    }

    /**
     * 下载分类首页二级页面数据
     */
    public static void downloadCategoryChild(Context context, int parent_id, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {
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
    public static void downloadCategoryGoods(Context context, int cartId, int pageId, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<NewGoodsBean[]>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID, String.valueOf(cartId))
                .addParam(I.PAGE_ID, String.valueOf(pageId))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    /**
     * 注册请求
     */
    public static void UserRegister(Context context, String username, String nick, String password, OkHttpUtils.OnCompleteListener<String> litener) {
        OkHttpUtils<String> uitls = new OkHttpUtils<String>(context);
        uitls.setRequestUrl(I.REQUEST_REGISTER)
                .post()
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(litener);
    }

    /**
     * 登录请求
     */
    public static void Login(Context context, String username, String password, OkHttpUtils.OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 修改用户昵称
     */
    public static void updateNick(Context context, String username, String nick, OkHttpUtils.OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, nick)
                .targetClass(String.class)
                .execute(listener);

    }

    /**
     * 更改头像
     */
    public static void updateAvatar(MySetting context, String muserName, File file, OkHttpUtils.OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID, muserName)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 下载用户收藏宝贝数量
     */
    public static void downloadCollecCounts(Context context, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Cart.USER_NAME, username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 通过用户帐号查询用户信息
     */
    public static void findUserByUserName(Context context, String username, OkHttpUtils.OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME, username)
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 下载收藏宝贝
     */
    public static void downloadCollections(Context context, String username, int pageId, OkHttpUtils.OnCompleteListener<CollectBean[]> listener) {
        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.PAGE_ID, pageId + "")
                .addParam(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                .targetClass(CollectBean[].class)
                .execute(listener);
    }

    /**
     * 添加收藏
     */
    public static void collectGoods(Context context, int goodId, String muserName, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Collect.GOODS_ID, goodId + "")
                .addParam(I.Collect.USER_NAME, muserName)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 删除收藏
     */
    public static void deleteCollect(Context context, int goodId, String name, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.GOODS_ID, String.valueOf(goodId))
                .addParam(I.Collect.USER_NAME, name)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 判断是否被收藏
     */
    public static void isCollected(Context context, int goodId, String name, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.GOODS_ID, String.valueOf(goodId))
                .addParam(I.Collect.USER_NAME, name)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 下载用户购物车信息
     */
    public static void downUserCart(Context context, String name, OkHttpUtils.OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME, name)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    /**
     * 删除用户购物车中的商品
     */
    public static void deleteCart(Context context, int goodId, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID, String.valueOf(goodId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 添加商品至购物车
     */
    public static void addCar(Context context, int goodId, String username, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID, String.valueOf(goodId))
                .addParam(I.Cart.USER_NAME, username)
                .addParam(I.Cart.COUNT, 1 + "")
                .addParam(I.Cart.IS_CHECKED, false + "")
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    /**
     * 更新购物车中商品的数量
     */
    public static void updateCar(Context context, int cartId, int count, OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, String.valueOf(cartId))
                .addParam(I.Cart.COUNT, String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(0))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

}
