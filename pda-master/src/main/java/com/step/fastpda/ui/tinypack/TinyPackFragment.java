package com.step.fastpda.ui.tinypack;

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
 * description： 小包标签
 */
@FragmentDestination(pageUrl = "main/tabs/tiny")
public class TinyPackFragment extends AbsListFragment<TinyPackList,TinyPackViewModel> {

    @Override
    public PagedListAdapter getAdapter() {
        TinyPackListAdapter listAdapter = new TinyPackListAdapter(getContext());
        return listAdapter;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<TinyPackList> currentList = adapter.getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }
}
