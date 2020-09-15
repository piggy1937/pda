package com.step.fastpda.ui.shipping;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.step.fastpda.ui.AbsViewModel;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class ShippingListViewModel extends AbsViewModel<ShippingList> {
    private int offset;
    private AtomicBoolean loadAfter = new AtomicBoolean();
    private MutableLiveData switchTabLiveData = new MutableLiveData();
    @Override
    public DataSource createDataSource() {
        return new DataSource();
    }
    private class DataSource extends ItemKeyedDataSource<Long, ShippingList> {


        @Override
        public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<ShippingList> callback) {
            loadData(0L, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<ShippingList> callback) {
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<ShippingList> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Long getKey(@NonNull ShippingList item) {
          return item.id;
        }
    }
    private void loadData(Long requestKey, ItemKeyedDataSource.LoadCallback<ShippingList> callback) {
        if (requestKey > 0) {
            loadAfter.set(true);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Integer pageSize = 10;


        ApiResponse<ShippingResponseInfo> response = ApiService.get("/Data/GetBarcodemt")
//                .addParam("creater", UserManager.get().getUserId())
//                .addParam("RwDate",sdf.format(new Date()))
                .addParam("pageSize", pageSize)
                .addParam("offset", offset)
                .responseType(new TypeReference<ShippingResponseInfo>() {
                }.getType())
                .execute();

        List<ShippingList> result= Lists.newArrayList();
        ShippingResponseInfo shippingResponseInfo = response.body == null ?null: response.body;

        if(shippingResponseInfo!=null&&shippingResponseInfo.getBarcodelist()!=null) {
            for(ShippingResponseInfo.BarcodelistBean bean:shippingResponseInfo.getBarcodelist()){
                ShippingList entity = new ShippingList();
                if(bean.getId()==null||bean.getId()==0){
                    continue;
                }
                entity.setId(bean.getId());

                entity.setInStockNo(bean.getBarcode());
                entity.setLastModifyTime(bean.getLastModifyTime());
                entity.setSumQty(bean.getQuantity());
                result.add(entity);
            }
            callback.onResult(result);
        }



        if (requestKey > 0) {
            loadAfter.set(false);
            offset += result.size();
            ((MutableLiveData) getBoundaryPageData()).postValue(result.size() > 0);
        } else {
            offset = result.size();
        }
    }

}
