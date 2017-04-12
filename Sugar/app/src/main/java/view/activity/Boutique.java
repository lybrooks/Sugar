package view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.NewGoodsBean;
import view.adapter.BoutiqueAdapter;
import day.sugar.R;
import model.net.IModleNewGoods;
import model.net.NewGoods;
import model.utils.ConvertUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;

public class Boutique extends AppCompatActivity {

    @Bind(R.id.back_nomal)
    ImageView backNomal;
    @Bind(R.id.tv_Title)
    TextView tvTitle;
    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.fag_rlv_newgoods)
    RecyclerView fagRlvNewgoods;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;
    int mNewState;
    int PageId = 1;
    public BoutiqueAdapter mAdapter;
    public GridLayoutManager layoutManger;
    ArrayList<NewGoodsBean> NewGoodsBeanlist;
    int CartId;

    IModleNewGoods modleNewGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique);
        ButterKnife.bind(this);
        CartId = initView();
        setListener();
        initData(I.ACTION_DOWNLOAD, CartId, PageId);

    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setEnabled(true);
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_DOWNLOAD, 1, PageId);
                srl.setRefreshing(false);
                srl.setEnabled(false);
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
                    srl.setRefreshing(false);
                    srl.setEnabled(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstposition = layoutManger.findFirstVisibleItemPosition();
                srl.setEnabled(firstposition == 0);
            }
        });

        mAdapter.setMyOnClick(new BoutiqueAdapter.MyOnClickListener() {
            @Override
            public void OnClick(View view, int position) {
                int goodsId = mAdapter.contactList.get(position).getGoodsId();
                MFGT.gotoGoodsDtails(Boutique.this, goodsId);
            }
        });
    }

    private int initView() {
        tvTitle.setText(getIntent().getStringExtra("title"));
        int cartId = getIntent().getIntExtra("cartId", 0);
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
        modleNewGoods = new NewGoods();
        return cartId;
    }


    private void initData(final int action, int cartId, int PageId) {
        modleNewGoods.downloadNewGoods(getApplicationContext(), cartId, PageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
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

    @OnClick(R.id.back_nomal)
    public void onClick() {
        this.finish();
    }

    private static boolean isExit = false;


}