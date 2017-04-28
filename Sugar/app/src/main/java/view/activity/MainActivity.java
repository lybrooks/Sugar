package view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sugar.I;
import cn.sugar.bean.UserBean;
import day.sugar.R;
import view.adapter.PersonAdapter;
import view.fragment.Fragment_boutique;
import view.fragment.Fragment_cart;
import view.fragment.Fragment_category;
import view.fragment.Fragment_newgoods;


public class MainActivity extends BaseActivity {

    FragmentManager fragmentManager;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    //    @Bind(R.id.main_fragment)
    public static ViewPager mVP;

    UserBean userBean;
    int index;

    String[] mTitle;

    @Bind(R.id.rlv_person)
    RecyclerView rlvPerson;
    LinearLayoutManager manager;

    PersonAdapter mAdapter;


    @Bind(R.id.dl_main)
    DrawerLayout dlMain;
    @Bind(R.id.tl_bottom)
    TabLayout tlBottom;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVP = (ViewPager) findViewById(R.id.main_fragment);
        ButterKnife.bind(this);

        initView();
        setListener();
    }

    private void setListener() {


    }

    private void initView() {
        //  dlMain.setScrimColor(Color.TRANSPARENT);

        mTitle = new String[]{"新品", "精选", "分类", "购物车"};
        Fragment_newgoods newgoods = new Fragment_newgoods();
        Fragment_boutique boutique = new Fragment_boutique();
        Fragment_category category = new Fragment_category();
        Fragment_cart cart = new Fragment_cart();
        //  Fragment_personal personal = newgoods Fragment_personal();

        fragmentArrayList.add(newgoods);
        fragmentArrayList.add(category);
        fragmentArrayList.add(boutique);
        fragmentArrayList.add(cart);
        // fragmentArrayList.add(personal);

        fragmentManager = getSupportFragmentManager();
        manager = new LinearLayoutManager(context);
        userBean = SugarApplication.getUserBean();
        mAdapter = new PersonAdapter(context, userBean, dlMain, rlvPerson);
        rlvPerson.setLayoutManager(manager);
        rlvPerson.setAdapter(mAdapter);

        MyViewPage VP_Adapter = new MyViewPage(fragmentManager, fragmentArrayList, mTitle);
        mVP.setAdapter(VP_Adapter);
        tlBottom.setTabsFromPagerAdapter(VP_Adapter);
        tlBottom.setTabMode(TabLayout.GRAVITY_CENTER);
        tlBottom.setupWithViewPager(mVP);
//.setIcon(R.drawable.newgoods)
        tlBottom.getTabAt(0).setText("新品");
//        .setIcon(R.drawable.jingxuan)
        tlBottom.getTabAt(1).setText("分类");
//        .setIcon(R.drawable.shop_select)
        tlBottom.getTabAt(2).setText("推荐");
//        .setIcon(R.drawable.cart)
        tlBottom.getTabAt(3).setText(mTitle[3]);

    }


    private void setViewPage() {
        for (int i = 0; i < 4; i++) {
            if (i == index) {
                mVP.setCurrentItem(i);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (index == 3 && SugarApplication.getUserBean() == null) {
            index = 0;
        }
        setViewPage();
        userBean = SugarApplication.getUserBean();
        if (userBean != null) {
            mAdapter.updateMessage(userBean);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_LOGIN && SugarApplication.getUserBean() != null) {
            index = 2;
        }
    }

    @OnClick(R.id.iv_menu)
    public void onClick() {
        if(dlMain.isDrawerOpen(rlvPerson)){
            dlMain.closeDrawer(rlvPerson);
        }else {
            dlMain.openDrawer(rlvPerson);
        }
    }

    class MyViewPage extends FragmentPagerAdapter {
        ArrayList<Fragment> arrayList;
        String[] mTitle = new String[]{};

        public MyViewPage(FragmentManager fm, ArrayList<Fragment> arrayList, String[] mTitle) {
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
            return mTitle[position];
        }


        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            getSupportFragmentManager().beginTransaction().hide(fragmentArrayList.get(position));
        }
    }

}
