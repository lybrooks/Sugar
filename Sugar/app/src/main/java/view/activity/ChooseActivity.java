package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import day.sugar.R;
import model.utils.MFGT;

public class ChooseActivity extends BaseActivity {
    PagerAdapter mAdapter;
    @Bind(R.id.vp_choose)
    ViewPager vpChoose;

    private View view1, view2, view3;
    private ArrayList<View> viewList;//view数组

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.item_viewpager1, null);
        view2 = inflater.inflate(R.layout.item_viewpager2, null);
        view3 = inflater.inflate(R.layout.item_viewpager3, null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        vpChoose.setAdapter(mAdapter);
    }


    @OnClick({R.id.btn_Regst, R.id.btn_Login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Regst:
                Intent intent = new Intent(this, Regist.class);
                startActivity(intent);
                break;
            case R.id.btn_Login:
                MFGT.gotoLogin(this);
                break;
        }
    }

}
