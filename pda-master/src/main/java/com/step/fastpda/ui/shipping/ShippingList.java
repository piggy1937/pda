package com.step.fastpda.ui.shipping;

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
    /***
     * 最近修改时间
     */
    public Date lastModifyTime;


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof ShippingList)) {
            return false;
        }
        ShippingList newOne = (ShippingList) obj;
        return  id == newOne.id&&
                TextUtils.equals(inStockNo, newOne.inStockNo)&&TextUtils.equals(inStockNumber, newOne.inStockNumber);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInStockNo() {
        return inStockNo;
    }

    public void setInStockNo(String inStockNo) {
        this.inStockNo = inStockNo;
    }

    public String getInStockNumber() {
        return inStockNumber;
    }

    public void setInStockNumber(String inStockNumber) {
        this.inStockNumber = inStockNumber;
    }

    public Integer getSumQty() {
        return sumQty;
    }

    public void setSumQty(Integer sumQty) {
        this.sumQty = sumQty;
    }

    public Integer getStockNo() {
        return stockNo;
    }

    public void setStockNo(Integer stockNo) {
        this.stockNo = stockNo;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
