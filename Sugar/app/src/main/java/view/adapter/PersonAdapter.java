package view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.sugar.bean.UserBean;
import view.activity.SugarApplication;
import day.sugar.R;
import model.utils.ImageLoader;
import model.utils.MFGT;
import model.utils.SharedPerfenceUtils;

/**
 * All rights Reserved, Designed By www.tydic.com
 *
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * Created by guowang on 2017/4/7.
 * @date: ${date} ${time}
 * @Copyright: ${year} www.tydic.com Inc. All rights reserved.
 */

public class PersonAdapter extends RecyclerView.Adapter {
    UserBean mBean;

    public PersonAdapter(Context context, UserBean bean) {
        this.context = context;
        this.mBean = bean;
    }

    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private int isHeader = 0;
    private final int Type_Header = 1;
    private final int Type_Item = 2;

    private String[] Titles = new String[]{"个人信息", "宝贝收藏", "退出"};


    public void updateMessage(UserBean userBean) {
        this.mBean = userBean;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case Type_Header:
                return new HeaderHolder(inflater.inflate(R.layout.item_personheader, parent, false));
            case Type_Item:
                return new ItemHolder(inflater.inflate(R.layout.item_personitem, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder && position == 0) {
            HeaderHolder holder1 = (HeaderHolder) holder;
            if (mBean != null && mBean.getMavatarSuffix() != null) {
                Glide.with(getContext()).load(ImageLoader.getAcatarUrl(mBean)).into(holder1.Avatar);
                if (mBean.getMuserNick() != null) {
                    ((HeaderHolder) holder).Nick.setText(mBean.getMuserNick());
                }
            }
            ((HeaderHolder) holder).Avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.goSettingActivity((Activity) getContext());
                }
            });
        }
        if (holder instanceof ItemHolder && position > 0) {
            ItemHolder holder2 = (ItemHolder) holder;
            holder2.title.setText(Titles[position - 1]);
            holder2.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position - 1) {
                        case 0:
                            MFGT.goSettingActivity((Activity) getContext());
                            break;
                        case 1:
                            MFGT.gotoCollection((Activity) getContext());
                            break;
                        case 2:
                            logout();
                            break;


                    }
                }
            });
        }

    }

    private void logout() {
        if (mBean != null) {
            SharedPerfenceUtils.getInstance(getContext()).removeUser();
            SugarApplication.getInstance().setUserBean(null);
            MFGT.gotoChooseActivity((Activity) getContext());
        }
        MFGT.finish((Activity) getContext());
    }

    @Override
    public int getItemCount() {
        return Titles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == isHeader ? Type_Header : Type_Item;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        ImageView Avatar;
        TextView Nick;

        public HeaderHolder(View itemView) {
            super(itemView);
            Avatar = (ImageView) itemView.findViewById(R.id.iv_item_menu_header);
            Nick = (TextView) itemView.findViewById(R.id.tv_nick);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }


}
