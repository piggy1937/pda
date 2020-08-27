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
    public String barcode;
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
                && TextUtils.equals(barcode, newOne.barcode)
                && TextUtils.equals(title, newOne.title);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
