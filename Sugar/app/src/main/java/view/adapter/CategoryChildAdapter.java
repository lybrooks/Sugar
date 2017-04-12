package view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import cn.sugar.bean.CategoryChildBean;
import day.sugar.R;
import model.utils.ImageLoader;
import model.utils.MFGT;

/**
 * All rights Reserved, Designed By www.tydic.com
 *
 * @version V1.0
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}(用一句话描述该文件做什么)
 * Created by guowang on 2017/4/11.
 * @date: ${date} ${time}
 * @Copyright: ${year} www.tydic.com Inc. All rights reserved.
 */

public class CategoryChildAdapter extends RecyclerView.Adapter {
    int id;
    //    IModelCategory category = new ModelCategory();
    private ArrayList<CategoryChildBean> categorylist;

    public CategoryChildAdapter(int id, Context context) {
        this.id = id;
        this.context = context;
        categorylist = new ArrayList<>();
//        category.downloadCategoryChild(context, id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
//            @Override
//            public void onSuccess(CategoryChildBean[] result) {
//                if (result != null) {
//                    categorylist = ConvertUtils.array2List(result);
//                    update(categorylist);
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//            }
//        });
    }

    public void update(ArrayList<CategoryChildBean> categorylist) {
        this.categorylist.addAll(categorylist);
        notifyDataSetChanged();
    }

    Context context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChildViewHolder(inflater.inflate(R.layout.item_child, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder hodler = (ChildViewHolder) holder;
            hodler.name.setText(categorylist.get(position).getName());
            ImageLoader.downloadImg(getContext(), hodler.iv, categorylist.get(position).getImageUrl());
            //   Glide.with(getContext()).load(categorylist.get(position).getImageUrl()).into(hodler.iv);

            hodler.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategoryDtails((Activity) getContext(), categorylist.get(position).getId(), categorylist, categorylist.get(position).getName());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return categorylist == null ? 0 : categorylist.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv;
        TextView name;

        public ChildViewHolder(View itemView) {
            super(itemView);
            iv = (RoundedImageView) itemView.findViewById(R.id.iv_child);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setCornerRadius(30);
            name = (TextView) itemView.findViewById(R.id.name_child);
        }
    }
}
