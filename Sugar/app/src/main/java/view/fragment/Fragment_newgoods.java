package view.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sugar.I;
import cn.sugar.bean.NewGoodsBean;
import view.adapter.BoutiqueAdapter;
import model.net.IModleNewGoods;
import model.net.NewGoods;
import model.utils.ConvertUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import day.sugar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_newgoods extends Fragment {
    public GridLayoutManager layoutManger;
    ArrayList<NewGoodsBean> NewGoodsBeanlist;
    public BoutiqueAdapter mAdapter;
    public RecyclerView mrv;

    int mNewState;
    int PageId = 1;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;
    @Bind(R.id.tv_refresh)
    TextView tvRefresh;

    IModleNewGoods newGoods;
    

    public Fragment_newgoods() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newgoods, container, false);
        initView(view);
        initData(I.ACTION_DOWNLOAD, PageId);
        setListener();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        newGoods = new NewGoods();

        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
        mrv = (RecyclerView) view.findViewById(R.id.fag_rlv_newgoods);
        NewGoodsBeanlist = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(getContext(), NewGoodsBeanlist);
        layoutManger = new GridLayoutManager(getContext(), I.COLUM_NUM, GridLayoutManager.VERTICAL, false);
        layoutManger.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == mAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        mrv.setLayoutManager(layoutManger);
        mrv.setHasFixedSize(true);
        mrv.setAdapter(mAdapter);
    }

    private void initData(final int action, int PageId) {
        newGoods.downloadNewGoods(getContext(), PageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
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
                srl.setRefreshing(false);
                srl.setEnabled(false);
            }
        });
    }


    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setEnabled(true);
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_DOWNLOAD, 1);
            }
        });

        mrv.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    initData(I.ACTION_PULL_UP, PageId);
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
                MFGT.gotoGoodsDtails((Activity) getContext(), goodsId);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }




}
