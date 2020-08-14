package com.step.fastpda.ui.shipping;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.step.fastpda.ui.AbsListFragment;
import com.tech.libnavannotation.FragmentDestination;

/**
 * @author zhushubin
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
@FragmentDestination(pageUrl = "main/tabs/shipping")
public class ShippingFragment extends AbsListFragment<ShippingList,ShippingListViewModel> {
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
}
