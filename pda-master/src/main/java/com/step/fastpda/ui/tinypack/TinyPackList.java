package com.step.fastpda.ui.tinypack;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class TinyPackList extends BaseObservable implements Serializable {
    public Long id ;
    /***
     * 编号
     */
    public String Barcode;
    /***
     * 标题
     */
    public String title = null;
    public Integer quantity;
    /***
     * 最近修改时间
     */
    public Date lastModifyTime;
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof TinyPackList)) {
            return false;
        }
        TinyPackList newOne = (TinyPackList) obj;
        return id == newOne.id
                && TextUtils.equals(Barcode, newOne.Barcode)
                && TextUtils.equals(title, newOne.title);
    }


}
