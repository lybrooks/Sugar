package view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.sugar.bean.CategoryChildBean;
import model.utils.ConvertUtils;
import model.utils.ImageLoader;
import model.utils.MFGT;
import day.sugar.R;
import view.activity.CategoryDetails;

/**
 * 显示分类中当前所属小类的列表
 *
 */
public class CatChildFilterButton extends Button {
    Context mContext;
    CatChildFilterButton mbtnTop;
    PopupWindow mPopupWindow;
    GridView mgvCategory;
    CatFilterAdapter mAdapter;
    OnClickListener mListener;

    /**
     * true:arrow down
     * false:arrow up
     * */
    boolean mExpandOff;

    public CatChildFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mbtnTop=this;
        mExpandOff=true;
        initGridView();
    }

    private void initPopupWindow() {
        mPopupWindow=new PopupWindow();
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        if(mgvCategory.getAdapter().getCount()<36){
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        }else{
//            mPopupWindow.setHeight(ConvertUtils.px2dp(mContext, 200));
//        }
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        mPopupWindow.setContentView(mgvCategory);
        mPopupWindow.showAsDropDown(mbtnTop);
    }

    private void initGridView() {
        mgvCategory=new GridView(mContext);
        mgvCategory.setColumnWidth(ConvertUtils.px2dp(mContext, 1500));
        mgvCategory.setHorizontalSpacing(ConvertUtils.px2dp(mContext, 10));
        mgvCategory.setVerticalSpacing(ConvertUtils.px2dp(mContext, 10));
        mgvCategory.setNumColumns(GridView.AUTO_FIT);
        mgvCategory.setBackgroundColor(Color.TRANSPARENT);
        mgvCategory.setPadding(3, 3, 3, 3);
        mgvCategory.setCacheColorHint(0);
    }

    private void setBtnTopArrow() {
        Drawable right=null;
        if(mExpandOff){
            right=mContext.getResources().getDrawable(R.drawable.arrow2_down);
        }else{
            right=mContext.getResources().getDrawable(R.drawable.arrow2_up);
        }
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        mbtnTop.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
        mExpandOff=!mExpandOff;
    }

    /**
     * 显示分类列表的适配器
     *
     */
    class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> Children;

        public CatFilterAdapter(Context context,
                                ArrayList<CategoryChildBean> list) {
            super();
            this.context = context;
            this.Children = list;
        }

        @Override
        public int getCount() {
            return Children==null?0:Children.size();
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return Children.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View layout, final ViewGroup parent) {
            ViewChildHolder holder=null;
            if(layout==null){
                layout= View.inflate(context, R.layout.item_cat_filter, null);
                holder=new ViewChildHolder();
                holder.layoutItem=(RelativeLayout) layout.findViewById(R.id.layout_category_child);
                holder.ivThumb=(ImageView) layout.findViewById(R.id.ivCategoryChildThumb);
                holder.tvChildName=(TextView) layout.findViewById(R.id.tvCategoryChildName);
                layout.setTag(holder);
            }else{
                holder=(ViewChildHolder) layout.getTag();
            }
            final CategoryChildBean child =getItem(position);
            String name=child.getName();
            holder.tvChildName.setText(name);
            String imgUrl=child.getImageUrl();
            ImageLoader.downloadImg(context,holder.ivThumb,imgUrl);

            holder.layoutItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                 /*   Intent intent=newgoods Intent(mContext, CategoryDetails.class);
                    intent.putExtra(I.CategoryChild.CAT_ID, child.getId());
                    intent.putExtra("childList", Children);
                    intent.putExtra(I.CategoryGroup.NAME, child.getName());
                    mContext.startActivity(intent);*/
                    MFGT.gotoCategoryDtails((Activity) mContext,child.getId(),Children,child.getName());
                    ((CategoryDetails)mContext).finish();
                }
            });
            return layout;
        }

        class ViewChildHolder{
            RelativeLayout layoutItem;
            ImageView ivThumb;
            TextView tvChildName;
        }
    }

    /**
     * 设置分类列表的下拉按钮单击事件监听
     * @param groupName
     * @param childList
     */
    public void setOnCatFilterClickListener(final String groupName,
                                            final ArrayList<CategoryChildBean> childList){
        mbtnTop.setOnClickListener(
                new OnClickListener() {
            @Override
            public void onClick(View v) {
                mbtnTop.setTextColor(Color.WHITE);
                mbtnTop.setText(groupName);
                if(mExpandOff){//若分类列表的窗口未打开，则弹出窗口
                    mAdapter=new CatFilterAdapter(mContext, childList);
                    mgvCategory.setAdapter(mAdapter);
                    initPopupWindow();
                }else{//否则，关闭窗口
                    if(mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                }
                setBtnTopArrow();
            }
        });
    }
}
