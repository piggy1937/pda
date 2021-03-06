package com.step.fastpda.ui.shipping;

import com.step.fastpda.ui.login.BaseResponseInfo;

import java.util.Date;
import java.util.List;

/**
 * @author zhushubin
 * @date 2020-09-03.
 * GitHub：
 * email： 604580436@qq.com
 * description：唛头bean
 */
public class ShippingResponseInfo extends BaseResponseInfo {
    /**
     * errCode : 1
     * errMsg : 成功
     * barcodelist : [{"barcode":"510120190708387"},{"barcode":"510120190708387"},{"barcode":"510120190708387"},{"barcode":"510120190708387"}]
     */


    private List<BarcodelistBean> barcodelist;



    public List<BarcodelistBean> getBarcodelist() {
        return barcodelist;
    }

    public void setBarcodelist(List<BarcodelistBean> barcodelist) {
        this.barcodelist = barcodelist;
    }

    public static class BarcodelistBean {
        /**
         * barcode : 510120190708387
         */
        private Long id;
        private String barcode;
        public Integer quantity;
        /***
         * 最近修改时间
         */
        public Date lastModifyTime;
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
}
