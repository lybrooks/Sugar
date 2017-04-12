package view.activity;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.CategoryChildBean;
import cn.sugar.bean.NewGoodsBean;
import view.widget.CatChildFilterButton;
import view.adapter.BoutiqueAdapter;
import day.sugar.R;
import model.net.IModelCategory;
import model.net.ModelCategory;
import model.utils.ConvertUtils;
import model.utils.L;
import model.utils.MFGT;
import model.utils.OkHttpUtils;


public class CategoryDetails extends AppCompatActivity {


    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.fag_rlv_newgoods)
    RecyclerView fagRlvNewgoods;

    SwipeRefreshLayout msrl;

    int mNewState;
    int PageId = 1;
    public BoutiqueAdapter mAdapter;
    public GridLayoutManager layoutManger;
    ArrayList<NewGoodsBean> NewGoodsBeanlist;
    int CartId;

    CatChildFilterButton cfbtn;
    String title;
    ArrayList<CategoryChildBean> childlist;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.bt_SortPrice)
    Button btSortPrice;
    @Bind(R.id.bt_SortTime)
    Button btSortTime;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;

    IModelCategory category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_details);
        ButterKnife.bind(this);
        msrl = (SwipeRefreshLayout) findViewById(R.id.srl);
        cfbtn = (CatChildFilterButton) findViewById(R.id.btnCatChildFilter);
        title = getIntent().getStringExtra(I.CategoryGroup.NAME);
        cfbtn.setText(title);
        CartId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        childlist = (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("childList");
        L.i(String.valueOf(CartId) + "cart_id" + "title:" + title);
        initView();
        setListener();
        initData(I.ACTION_DOWNLOAD, CartId, PageId);
    }

    private void setListener() {


        cfbtn.setOnCatFilterClickListener(title, childlist);

        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setEnabled(true);
                msrl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_DOWNLOAD, CartId, PageId);
            }
        });
        fagRlvNewgoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int Lastposition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mNewState = newState;
                Lastposition = layoutManger.findLastVisibleItemPosition();
                if (Lastposition >= mAdapter.getItemCount() - 1
                        && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mAdapter.isMore) {
                    PageId++;
                    initData(I.ACTION_PULL_UP, CartId, PageId);
                    msrl.setRefreshing(false);
                    msrl.setEnabled(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstposition = layoutManger.findFirstVisibleItemPosition();
                msrl.setEnabled(firstposition == 0);
            }
        });


        mAdapter.setMyOnClick(new BoutiqueAdapter.MyOnClickListener() {
            @Override
            public void OnClick(View view, int position) {
                int goodsId = mAdapter.contactList.get(position).getGoodsId();
                MFGT.gotoGoodsDtails(CategoryDetails.this, goodsId);
            }
        });
    }

    private void initData(final int action, int cartId, int PageId) {
        category.downloadCategoryGoods(this, cartId, PageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                msrl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                if (result != null && result.length > 0 && mAdapter.isMore) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            mAdapter.setFooter("加载更多数据");
                            mAdapter.inintContact(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            mAdapter.inintContact(list);
                            break;
                        case I.ACTION_PULL_UP:
                            mAdapter.AddContactList(list);
                            break;
                    }
                } else {
                    mAdapter.setFooter("没有更多数据了");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {

        NewGoodsBeanlist = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(this, NewGoodsBeanlist);
        layoutManger = new GridLayoutManager(this, I.COLUM_NUM, GridLayoutManager.VERTICAL, false);
        layoutManger.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        fagRlvNewgoods.setLayoutManager(layoutManger);
        fagRlvNewgoods.setHasFixedSize(true);
        fagRlvNewgoods.setAdapter(mAdapter);
        category = new ModelCategory();
    }

    boolean addTimeAsc = false;
    boolean priceAsc = false;
    int sortBy = I.SORT_BY_ADDTIME_ASC;
    Drawable right;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick({R.id.bt_SortPrice, R.id.bt_SortTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_SortPrice:
                if (priceAsc) {
                    sortBy = I.SORT_BY_PRICE_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                priceAsc = !priceAsc;
                break;
            case R.id.bt_SortTime:
                if (addTimeAsc) {
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_up);
                } else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    right = getResources().getDrawable(R.mipmap.arrow_order_down);
                }
                right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
                btSortTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
                addTimeAsc = !addTimeAsc;
                break;
        }
        mAdapter.setSortBy(sortBy);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        MFGT.finish(this);
    }

}
