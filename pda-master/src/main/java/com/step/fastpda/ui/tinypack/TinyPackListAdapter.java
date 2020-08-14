package com.step.fastpda.ui.tinypack;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.step.fastpda.R;
import com.step.fastpda.databinding.LayoutTinypackListItemBinding;
import com.tech.libcommon.extention.AbsPagedListAdapter;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：适配器-小包标签
 */
public class TinyPackListAdapter  extends AbsPagedListAdapter<TinyPackList, TinyPackListAdapter.ViewHolder>  {
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
        itemBinding.tvDelete.setOnClickListener(v->new MaterialDialog.Builder(mContext)
                .title("删除")
                .content("确定要删除"+itemBinding.tvTinypackTitle.getText()+"吗?")
                .positiveText("确认")
                .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      dialog.dismiss();
                    }
                }).show());

        itemBinding.tvEdite.setOnClickListener(v->new MaterialDialog.Builder(mContext)
                .title(itemBinding.tvTinypackTitle.getText())
                .inputRangeRes(1, 20, R.color.colorAccent)
                //限制输入类型
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("数量", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Log.e("",input.toString());
                    }
                })
                .positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText("取消")
                .show());



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
