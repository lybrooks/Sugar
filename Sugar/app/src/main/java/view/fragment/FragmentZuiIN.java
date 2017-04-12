package view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sugar.bean.CategoryChildBean;
import view.adapter.CategoryChildAdapter;
import day.sugar.R;
import model.net.IModelCategory;
import model.net.ModelCategory;
import model.utils.ConvertUtils;
import model.utils.OkHttpUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentZuiIN extends Fragment {

    int id;
    IModelCategory category;
    CategoryChildAdapter mAdapter;
    GridLayoutManager manager;
    @Bind(R.id.child_rlv)
    RecyclerView childRlv;
    private ArrayList<CategoryChildBean> categorylist;

    public FragmentZuiIN() {

    }

    public FragmentZuiIN(Integer integer) {
        this.id = integer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        category = new ModelCategory();
        View view = inflater.inflate(R.layout.fragment_zuiin, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mAdapter = new CategoryChildAdapter(id, getContext());
        manager = new GridLayoutManager(getContext(), 2);
        childRlv.setLayoutManager(manager);
        childRlv.setAdapter(mAdapter);
        category.downloadCategoryChild(getContext(), id, new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                if (result != null) {
                    categorylist = ConvertUtils.array2List(result);
                    mAdapter.update(categorylist);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
