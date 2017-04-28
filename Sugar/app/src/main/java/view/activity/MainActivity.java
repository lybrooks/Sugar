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


        tlBottom.getTabAt(0).setIcon(R.drawable.newgoods).setText(null);
        tlBottom.getTabAt(1).setIcon(R.drawable.jingxuan).setText(null);
        tlBottom.getTabAt(2).setIcon(R.drawable.shop_select).setText(null);
        tlBottom.getTabAt(3).setIcon(R.drawable.cart).setText(null);

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
//            ImageLoader.setAcatar(ImageLoader.getAcatarUrl(userBean), this, ivPersonalAvatar);
//            tvPersonalUserName.setText(userBean.getMuserNick());
            //           downCollectCounts();
            //         updateUserMessages();
        }
    }

//    private static boolean isExit = false;
//
//    Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            isExit = false;
//        }
//    };
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void exit() {
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
//            // 利用handler延迟发送更改状态信息
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_LOGIN && SugarApplication.getUserBean() != null) {
            index = 2;
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
