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
    /***
     * 入库单号
     */
    public String psnr;
    /***
     * 单据序号
     */
    public String pno;
    /***
     * 入库数量
     */
    public Integer quantity;
    /***
     * 仓库号
     */
    public Integer ckh;
    /***
     * 仓位号
     */
    public Integer cwh;


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof ShippingList)) {
            return false;
        }
        ShippingList newOne = (ShippingList) obj;
        return TextUtils.equals(psnr, newOne.psnr)&&TextUtils.equals(pno, newOne.pno);
    }
}
