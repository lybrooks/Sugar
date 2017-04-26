package view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.sugar.bean.UserBean;
import day.sugar.R;
import model.utils.ImageLoader;
import model.utils.MFGT;
import model.utils.SharedPerfenceUtils;
import view.activity.SugarApplication;

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
    DrawerLayout dl;
    RecyclerView rlv;

    public PersonAdapter(Context context, UserBean bean, DrawerLayout dl, RecyclerView rlv) {
        this.context = context;
        this.mBean = bean;
        this.dl = dl;
        this.rlv = rlv;
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
    private int[] Img = new int[]{R.mipmap.personal,
            R.mipmap.collect,
            R.mipmap.quit};

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder && position == 0) {
            final HeaderHolder holder1 = (HeaderHolder) holder;
            if (mBean != null && mBean.getMavatarSuffix() != null) {
                dl.setDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset == 1) {
                            Glide.with(getContext()).load(ImageLoader.getAcatarUrl(mBean)).into(holder1.Avatar);
                            if (mBean.getMuserNick() != null) {
                                holder1.Nick.setText(mBean.getMuserNick());
                            }

                        }
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });

            }
            ((HeaderHolder) holder).Avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.goSettingActivity((Activity) getContext());
                    dl.closeDrawer(rlv);
                }
            });
        }
        if (holder instanceof ItemHolder && position > 0) {
            ItemHolder holder2 = (ItemHolder) holder;
            holder2.title.setText(Titles[position - 1]);
            holder2.imageView.setImageResource(Img[position - 1]);
            holder2.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position - 1) {
                        case 0:
                            MFGT.goSettingActivity((Activity) getContext());
                            dl.closeDrawer(rlv);
                            break;
                        case 1:
                            MFGT.gotoCollection((Activity) getContext());
                            dl.closeDrawer(rlv);
                            break;
                        case 2:
                            showNormalDialog();

                            break;


                    }
                }
            });
        }

    }

    private void showNormalDialog() {

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.mipmap.unhappy);
        normalDialog.setTitle("是否离开sugar");
        // normalDialog.setMessage("");
        normalDialog.setPositiveButton("再看看",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   logout();

                    }
                });
        normalDialog.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
        // 显示
        normalDialog.show();
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
        ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_set);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }


}
