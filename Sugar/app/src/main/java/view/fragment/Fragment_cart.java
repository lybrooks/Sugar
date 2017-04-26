package view.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.CartBean;
import cn.sugar.bean.NewGoodsBean;
import cn.sugar.bean.UserBean;
import day.sugar.R;
import model.net.IModelCart;
import model.net.IModleNewGoods;
import model.net.ModelCart;
import model.net.NewGoods;
import model.utils.CommonUtils;
import model.utils.ConvertUtils;
import model.utils.MFGT;
import model.utils.OkHttpUtils;
import view.activity.SugarApplication;
import view.adapter.BoutiqueAdapter;
import view.adapter.CartAdapter;

public class Fragment_cart extends Fragment {

    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;


    ArrayList<CartBean> cartBeanArrayList;
    public CartAdapter mAdapter;
    public LinearLayoutManager layoutManger;
    @Bind(R.id.tv_sums)
    TextView tvSums;
    @Bind(R.id.iv_save)
    TextView tvSave;
    @Bind(R.id.layout_cart)
    LinearLayout layoutCart;
    @Bind(R.id.tv_nothing)
    TextView tvNothing;
    @Bind(R.id.fag_rlv_newgoods)
    RecyclerView fagRlvNewgoods;

    RecyclerView cartChoose;

    updateCartReceiver mReceiver;
    String cartID = "";

    ScrollView mSrollView;

    IModelCart cart = new ModelCart();


    public GridLayoutManager Choosemanager;
    ArrayList<NewGoodsBean> NewGoodsBeanlist;
    public BoutiqueAdapter ChoosemAdapter;

    IModleNewGoods newGoods;

    public Fragment_cart() {
    }

    int PageId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        cartBeanArrayList = new ArrayList<>();
        mAdapter = new CartAdapter(getContext(), cartBeanArrayList);
        layoutManger = new LinearLayoutManager(getContext());
        fagRlvNewgoods.setLayoutManager(layoutManger);
        fagRlvNewgoods.setAdapter(mAdapter);
        fagRlvNewgoods.setNestedScrollingEnabled(false);
        initData(I.ACTION_DOWNLOAD, PageId);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {
        mSrollView = (ScrollView) view.findViewById(R.id.scroll);
        setCarLayout(false);
        cartChoose = (RecyclerView) view.findViewById(R.id.cartrlv);
        Choosemanager = new GridLayoutManager(getContext(), I.COLUM_NUM, GridLayoutManager.VERTICAL, false);
        Choosemanager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == ChoosemAdapter.getItemCount() - 1 ? 2 : 1;
            }
        });
        NewGoodsBeanlist = new ArrayList<>();
        ChoosemAdapter = new BoutiqueAdapter(getContext(), NewGoodsBeanlist);
        cartChoose.setLayoutManager(Choosemanager);
        cartChoose.setHasFixedSize(true);
        cartChoose.setAdapter(ChoosemAdapter);
        cartChoose.setNestedScrollingEnabled(false);
        ChoosemAdapter.setMyOnClick(new BoutiqueAdapter.MyOnClickListener() {
            @Override
            public void OnClick(View view, int position) {
                int goodsId = ChoosemAdapter.contactList.get(position).getGoodsId();
                MFGT.gotoGoodsDtails((Activity) getContext(), goodsId);
            }
        });

    }

    private void setCarLayout(boolean hasCart) {
        layoutCart.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        tvNothing.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        fagRlvNewgoods.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        sumPrice();
    }

    private void setListener() {
        OnRefresh();
        IntentFilter flter = new IntentFilter(I.BROADCAST_UPDATE_CART);
        mReceiver = new updateCartReceiver();
        getContext().registerReceiver(mReceiver, flter);


    }


    private void OnRefresh() {
        if (mSrollView != null) {
            mSrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (srl != null) {
                        srl.setEnabled(mSrollView.getScrollY() == 0);
                        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                srl.setColorSchemeColors(getResources().getColor(R.color.red));
                                srl.setRefreshing(true);
                                tvRefresh.setVisibility(View.VISIBLE);
                                downloadCart();
                            }
                        });
                    }
                }
            });
        }

    }

    private void initData(final int action, int PageId) {
        downloadCart();
        downloadChoose(action, PageId);
    }

    private void downloadChoose(final int action, int PageId) {
        newGoods = new NewGoods();
        newGoods.downloadNewGoods(getContext(), PageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                if (result != null && result.length > 0 && mAdapter.isMore) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    switch (action) {
                        case I.ACTION_DOWNLOAD:
                            ChoosemAdapter.setFooter("加载更多数据");
                            ChoosemAdapter.inintContact(list);
                            break;
                        case I.ACTION_PULL_DOWN:
                            ChoosemAdapter.inintContact(list);
                            break;
                        case I.ACTION_PULL_UP:
                            ChoosemAdapter.AddContactList(list);

                            break;
                    }
                } else {
                    ChoosemAdapter.setFooter("没有更多数据了");
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                srl.setEnabled(false);
            }
        });
    }

    private void downloadCart() {
        final UserBean userBean = SugarApplication.getUserBean();
        if (userBean != null) {
            cart.downUserCart(getContext(), userBean.getMuserName(), new OkHttpUtils.OnCompleteListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    if (result != null) {
                        OkHttpUtils utils = new OkHttpUtils(getContext());
                        ArrayList list = utils.array2List(result);
                        tvRefresh.setVisibility(View.GONE);
                        srl.setRefreshing(false);
                        if (list != null && list.size() > 0) {
                            cartBeanArrayList.clear();
                            cartBeanArrayList.addAll(list);
                            mAdapter.inintContact(list);
                            setCarLayout(true);
                        } else {
                            setCarLayout(false);
                        }
                    }

                }

                @Override
                public void onError(String error) {
                    setCarLayout(false);
                    tvRefresh.setVisibility(View.GONE);
                    srl.setRefreshing(false);
                }

            });

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void sumPrice() {
        int sumPrice = 0;
        int ranPrice = 0;
        cartID = "";
        if (cartBeanArrayList != null && cartBeanArrayList.size() > 0) {
            for (CartBean c : cartBeanArrayList) {
                if (c.isChecked()) {
                    cartID += c.getId() + ",";
                    sumPrice += getPrice(c.getGoods().getCurrencyPrice()) * c.getCount();
                    ranPrice += getPrice(c.getGoods().getRankPrice()) * c.getCount();
                }
            }
            tvSums.setText("合计:￥" + Double.valueOf(sumPrice));
            tvSave.setText("节省:￥" + Double.valueOf(sumPrice - ranPrice));
        } else {
            cartID = null;
            tvSums.setText("合计:￥0");
            tvSave.setText("节省:￥0");
        }
    }

    private int getPrice(String price) {
        price = price.substring(price.indexOf("￥") + 1);
        return Integer.valueOf(price);
    }

    @OnClick(R.id.Buy)
    public void onClick() {
        if (cartID != null && cartID.length() > 0 && !cartID.equals("")) {
            MFGT.goPayActivity(getContext(), cartID);
        } else {
            CommonUtils.showLongToast("亲,没有选中商品噢");
        }
    }

    class updateCartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
            setCarLayout(cartBeanArrayList != null && cartBeanArrayList.size() > 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadCart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }
}
