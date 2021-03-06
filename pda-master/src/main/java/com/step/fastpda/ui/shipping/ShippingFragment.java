package com.step.fastpda.ui.shipping;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.step.fastpda.databinding.LayoutShippingHeaderBinding;
import com.step.fastpda.ui.AbsListFragment;
import com.tech.libcommon.extention.AbsPagedListAdapter;
import com.tech.libnavannotation.FragmentDestination;

/**
 * @author zhushubin
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
@FragmentDestination(pageUrl = "main/tabs/shipping",needLogin = true)
public class ShippingFragment extends AbsListFragment<ShippingList,ShippingListViewModel> {
    private static final int REQUEST_CODE = 100;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutShippingHeaderBinding headerBinding = LayoutShippingHeaderBinding.inflate(LayoutInflater.from(getContext()),mRecyclerView, false);
        ((AbsPagedListAdapter)adapter).addHeaderView(headerBinding.getRoot());
        headerBinding.actionShippingAdd.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ShippingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public PagedListAdapter getAdapter() {
        return new ShippingListAdapter(getContext());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<ShippingList> currentList = adapter.getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==200){
            mViewModel.getDataSource().invalidate();
        }

    }
}
