package com.step.fastpda.ui.shipping;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import java.io.Serializable;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description： 唛头
 */
public class ShippingList extends BaseObservable implements Serializable {
    public Long id ;
    /***
     * 入库单号
     */
    public String inStockNo;
    /***
     * 单据序号
     */
    public String inStockNumber;
    /***
     * 入库数量
     */
    public Integer sumQty;
    /***
     * 仓库号
     */
    public Integer stockNo;
    /***
     * 仓位号
     */
    public Integer position;


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof ShippingList)) {
            return false;
        }
        ShippingList newOne = (ShippingList) obj;
        return  id == newOne.id&&
                TextUtils.equals(inStockNo, newOne.inStockNo)&&TextUtils.equals(inStockNumber, newOne.inStockNumber);
    }
}
