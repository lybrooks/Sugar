package view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sugar.I;
import cn.sugar.bean.CollectBean;
import cn.sugar.bean.MessageBean;
import view.activity.SugarApplication;
import day.sugar.R;
import model.net.IModelCollections;
import model.net.ModelCollections;
import model.utils.CommonUtils;
import model.utils.ImageLoader;
import model.utils.L;
import model.utils.OkHttpUtils;


public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public CollectAdapter(Context context, ArrayList<CollectBean> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    Context context;
    public ArrayList<CollectBean> contactList;

    ViewGroup parent;
    int sortBy = 0;

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

    public MyOnClickListener getMyOnClick() {
        return myOnClick;
    }

    public void setMyOnClick(MyOnClickListener myOnClick) {
        this.myOnClick = myOnClick;
    }

    MyOnClickListener myOnClick;

    IModelCollections collections;


    public void inintContact(ArrayList<CollectBean> list) {
        this.contactList.clear();
        this.contactList.addAll(list);
        notifyDataSetChanged();
    }

    public void AddContactList(ArrayList<CollectBean> list) {
        this.contactList.addAll(list);
        notifyDataSetChanged();
    }

    private void deleteData(CollectBean goodsBean) {
        this.contactList.remove(goodsBean);
        L.e("Collect,remove:"+goodsBean.getGoodsName());
        notifyDataSetChanged();
    }

    public void setFooter(String footertext) {
        this.footertext = footertext;
        notifyDataSetChanged();
    }

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
                layout = inflater.inflate(R.layout.item_collect, parent, false);
                holder = new ContactViewHolder(layout, myOnClick);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.mtvFooter.setText(footertext);
            return;
        }
        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        final CollectBean goodsBean = contactList.get(position);
        contactViewHolder.tvGoodPrize.setText(goodsBean.getGoodsName());
        ImageLoader.build(I.DOWNLOAD_IMG_URL + goodsBean.getGoodsThumb())
                .height(200)
                .width(200)
                .imageView(contactViewHolder.ivGoodsImg)
                .listener(parent)
                .showImage(context);
        contactViewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCollected(goodsBean);
            }
        });
    }

    private void deleteCollected(final CollectBean goodsBean) {
        collections = new ModelCollections();
        collections.deleteCollect(context, goodsBean.getGoodsId(), SugarApplication.getUserBean().getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    deleteData(goodsBean);
                    CommonUtils.showLongToast("删除收藏成功");
                }else {
                    CommonUtils.showLongToast("您的收藏夹为空");
                }
            }
            @Override
            public void onError(String error) {
                CommonUtils.showLongToast("删除收藏成功");
            }
        });
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


    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_footer)
        TextView mtvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyOnClickListener {
        void OnClick(View view, int position);
    }


    static class ContactViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_goods_img)
        RoundedImageView ivGoodsImg;
        @Bind(R.id.tv_good_prize)
        TextView tvGoodPrize;

        ImageView iv_delete;
        MyOnClickListener MyOnClick;

        ContactViewHolder(View view, final MyOnClickListener MyonClick) {
            super(view);
            ButterKnife.bind(this, view);
            ivGoodsImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivGoodsImg.setCornerRadius(30);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

            this.MyOnClick = MyonClick;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyOnClick != null) {
                        MyOnClick.OnClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}


