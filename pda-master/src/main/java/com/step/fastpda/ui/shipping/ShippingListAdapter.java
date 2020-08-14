package com.step.fastpda.ui.shipping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.step.fastpda.databinding.LayoutShippingListItemBinding;
import com.tech.libcommon.extention.AbsPagedListAdapter;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：适配器-小包标签
 */
public class ShippingListAdapter extends AbsPagedListAdapter<ShippingList, ShippingListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;


    public ShippingListAdapter(Context context) {
        super(new DiffUtil.ItemCallback<ShippingList>() {

            @Override
            public boolean areItemsTheSame(@NonNull ShippingList oldItem, @NonNull ShippingList newItem) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(@NonNull ShippingList oldItem, @NonNull ShippingList newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutShippingListItemBinding itemBinding = LayoutShippingListItemBinding.inflate(mInflater, parent, false);
        return new ViewHolder(itemBinding.getRoot(), itemBinding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final ShippingList item = getItem(position);
        holder.bindData(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private LayoutShippingListItemBinding mItemBinding;

        public ViewHolder(@NonNull View itemView, LayoutShippingListItemBinding itemBinding) {
            super(itemView);
            mItemBinding = itemBinding;
        }

        public void bindData(ShippingList item) {
            mItemBinding.setShippingList(item);
        }
    }

}
