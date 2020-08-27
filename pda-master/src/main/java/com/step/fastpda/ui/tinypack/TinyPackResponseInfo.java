package com.step.fastpda.ui.tinypack;

import com.step.fastpda.ui.login.BaseResponseInfo;

import java.util.List;

/**
 * @author zhushubin
 * @date 2020-08-27.
 * GitHub：
 * email： 604580436@qq.com
 * description： 小包标签
 */
public class TinyPackResponseInfo extends BaseResponseInfo {
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
    }
}
