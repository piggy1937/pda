package com.step.fastpda.ui.tinypack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.step.fastpda.databinding.LayoutTinypackListItemBinding;
import com.tech.libcommon.extention.AbsPagedListAdapter;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：适配器-小包标签
 */
public class TinyPackListAdapter  extends AbsPagedListAdapter<TinyPackList, TinyPackListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;


    public TinyPackListAdapter(Context context) {
        super(new DiffUtil.ItemCallback<TinyPackList>() {

            @Override
            public boolean areItemsTheSame(@NonNull TinyPackList oldItem, @NonNull TinyPackList newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull TinyPackList oldItem, @NonNull TinyPackList newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutTinypackListItemBinding itemBinding = LayoutTinypackListItemBinding.inflate(mInflater, parent, false);
        return new ViewHolder(itemBinding.getRoot(), itemBinding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final TinyPackList item = getItem(position);
        holder.bindData(item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private LayoutTinypackListItemBinding mItemBinding;

        public ViewHolder(@NonNull View itemView, LayoutTinypackListItemBinding itemBinding) {
            super(itemView);
            mItemBinding = itemBinding;
        }

        public void bindData(TinyPackList item) {
            mItemBinding.setTinyPackList(item);
        }
    }

}
