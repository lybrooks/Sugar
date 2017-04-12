package view.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sugar.bean.CategoryGroupBean;
import day.sugar.R;
import model.net.IModelCategory;
import model.net.ModelCategory;
import model.utils.ConvertUtils;
import model.utils.OkHttpUtils;

public class Fragment_category extends Fragment {


    @Bind(R.id.tv_refresh)
    TextView tvRefresh;
    @Bind(R.id.ELV)
    ExpandableListView ELV;
    @Bind(R.id.srl)
    SwipeRefreshLayout srl;

//    CategoryAdapter categoryAdapter;
//    ArrayList<CategoryGroupBean> groupList;
//    ArrayList<ArrayList<CategoryChildBean>> chillist;

    IModelCategory category;

    @Bind(R.id.tl_category)
    TabLayout tlCategory;
    @Bind(R.id.vp_category)
    ViewPager vpCategory;

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> mTitle = new ArrayList<>();
    ArrayList<Integer> mId = new ArrayList<>();
    MyViewPage mAdapter;

    public Fragment_category() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        initData();
//        groupList = new ArrayList<>();
//        chillist = new ArrayList<>();
//        categoryAdapter = new CategoryAdapter(getContext(), groupList, chillist, ELV);
//        ELV.setGroupIndicator(null);
//        ELV.setAdapter(categoryAdapter);
        setListener();
        return view;

    }

    private void initView() {
        FragmentZuiIN fragmentzuiin = new FragmentZuiIN(mId.get(0));
        FragmentMeiZhuang fragmentmeizhuang = new FragmentMeiZhuang(mId.get(1));
        FragmentLive fragmetnlive = new FragmentLive(mId.get(2));
        FragmentCloth fragmentcloth = new FragmentCloth(mId.get(3));
        FragmentBag fragmentbag = new FragmentBag(mId.get(4));
        FragmentShose fragmentshose = new FragmentShose(mId.get(5));
        FragmentMather fragmentmather = new FragmentMather(mId.get(6));
        Fragment_peishi fragmentpeishi = new Fragment_peishi(mId.get(7));

        fragmentArrayList.add(fragmentzuiin);
        fragmentArrayList.add(fragmentmeizhuang);
        fragmentArrayList.add(fragmetnlive);
        fragmentArrayList.add(fragmentcloth);
        fragmentArrayList.add(fragmentbag);
        fragmentArrayList.add(fragmentshose);
        fragmentArrayList.add(fragmentmather);
        fragmentArrayList.add(fragmentpeishi);

        mAdapter = new MyViewPage(getFragmentManager(), fragmentArrayList, mTitle);
        vpCategory.setAdapter(mAdapter);
        tlCategory.setTabsFromPagerAdapter(mAdapter);
        tlCategory.setupWithViewPager(vpCategory);
    }

    private void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                srl.setEnabled(true);
            }
        });
    }

    private void initData() {
        category = new ModelCategory();
        category.downloadCategoryGroup(getContext(), new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                ArrayList<CategoryGroupBean> categorylist = ConvertUtils.array2List(result);
                for (CategoryGroupBean bean : categorylist) {
                    mTitle.add(bean.getName());
                    mId.add(bean.getId());
                }
                initView();

               // categoryAdapter.addlist(categorylist);
                srl.setRefreshing(false);
                srl.setEnabled(false);
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    class MyViewPage extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayList;
        // String[] mTitle = new String[]{};
        ArrayList<String> mTitle = new ArrayList<>();

        public MyViewPage(FragmentManager fm, ArrayList<Fragment> arrayList, ArrayList<String> mTitle) {
            super(fm);
            this.arrayList = arrayList;
            this.mTitle = mTitle;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle.get(position);
        }


        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(fragmentArrayList.get(position));
        }
    }
}
