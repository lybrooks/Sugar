package view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.CartBean;
import cn.sugar.bean.GoodsDetailsBean;
import cn.sugar.bean.MessageBean;
import day.sugar.R;
import model.net.IModelCart;
import model.net.ModelCart;
import model.utils.CommonUtils;
import model.utils.ImageLoader;
import model.utils.MFGT;
import model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public CartAdapter(Context context, ArrayList<CartBean> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    static Context context;
    static ArrayList<CartBean> contactList;

    public ArrayList<CartBean> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<CartBean> contactList) {
        this.contactList = contactList;
    }

    ViewGroup parent;

    public String getFootertext() {
        return footertext;
    }

    public void setFootertext(String footertext) {
        this.footertext = footertext;
    }

    String footertext;


    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isMore = true;


    public void inintContact(ArrayList<CartBean> list) {
        if (contactList != null) {
            contactList.clear();
        }
        this.contactList = list;
        notifyDataSetChanged();
    }


    int count;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(layout);
                break;
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_cart, parent, false);
                holder = new ContactViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvItemFooter.setText(footertext);
            return;
        }
        final ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        final CartBean goodsBean = contactList.get(position);
        GoodsDetailsBean goods = goodsBean.getGoods();
        if (goodsBean != null) {
            count = goodsBean.getCount();
            contactViewHolder.tvGoodCount.setText("(" + count + ")");
            contactViewHolder.tvGoodName.setText(goods.getGoodsName());
            contactViewHolder.tvGoodPrize.setText(goods.getCurrencyPrice());
            ImageLoader.downloadImg(context, contactViewHolder.ivGoodsImg, goods.getGoodsThumb());
            contactViewHolder.ivAddcart.setTag(position);
            contactViewHolder.mCbCartSelested.setChecked(false);
            contactViewHolder.mCbCartSelested.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    goodsBean.setChecked(isChecked);
                    getContext().sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }


    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_footer)
        TextView tvItemFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_goods_img)
        RoundedImageView ivGoodsImg;
        @Bind(R.id.tv_good_name)
        TextView tvGoodName;
        @Bind(R.id.iv_addcart)
        ImageView ivAddcart;
        @Bind(R.id.tv_good_count)
        TextView tvGoodCount;
        @Bind(R.id.iv_deleteCart)
        ImageView ivDeleteCart;
        @Bind(R.id.tv_good_prizes)
        TextView tvGoodPrize;
        @Bind(R.id.cb_checkBox)
        CheckBox mCbCartSelested;
        IModelCart modelCart = new ModelCart();


        ContactViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivGoodsImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivGoodsImg.setCornerRadius(80);

        }

        @OnClick({R.id.iv_goods_img, R.id.tv_good_prizes, R.id.tv_good_name})
        public void gotoGoodDetail() {
            int position = (int) ivAddcart.getTag();
            final CartBean cartBean = contactList.get(position);
            MFGT.gotoGoodsDtails((Activity) context, cartBean.getGoodsId());
        }

        @OnClick(R.id.iv_addcart)
        public void addCart() {

            int position = (int) ivAddcart.getTag();
            final CartBean cartBean = contactList.get(position);
            modelCart.updateCar(context, cartBean.getId(), cartBean.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        cartBean.setCount(cartBean.getCount() + 1);
                        context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                        tvGoodCount.setText("(" + cartBean.getCount() + ")");
                        //  tvGoodPrize.setText("￥" + cartBean.getCount() * getPrice(cartBean.getGoods().getCurrencyPrice()));
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

        }

        private int getPrice(String price) {
            price = price.substring(price.indexOf("￥") + 1);
            return Integer.valueOf(price);
        }

        @OnClick(R.id.iv_deleteCart)
        public void deleteCart() {
            final int position = (int) ivAddcart.getTag();
            final CartBean cartBean = contactList.get(position);
            if (cartBean.getCount() > 1) {
                modelCart.updateCar(context, cartBean.getId(), cartBean.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            cartBean.setCount(cartBean.getCount() - 1);
                            context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                            tvGoodCount.setText("(" + cartBean.getCount() + ")");
                            tvGoodPrize.setText("￥" + cartBean.getCount() * getPrice(cartBean.getGoods().getCurrencyPrice()));

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                modelCart.deleteCart(context, cartBean.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result.isSuccess()) {
                            CommonUtils.showShortToast("删除成功");
                            contactList.remove(position);
                            context.sendBroadcast(new Intent(I.BROADCAST_UPDATE_CART));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast("删除失败");
                    }
                });

            }
        }


    }
}
