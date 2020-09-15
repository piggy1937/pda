package com.step.fastpda.ui.tinypack;

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
 * @date 2020-08-13.
 * GitHub：
 * email： 604580436@qq.com
 * description： 小包标签bean
 */
public class TinyPackViewModel extends AbsViewModel<TinyPackList> {
    private int offset;
    private AtomicBoolean loadAfter = new AtomicBoolean();
    private MutableLiveData switchTabLiveData = new MutableLiveData();
    @Override
    public DataSource createDataSource() {
        return  new DataSource();
    }
    private class DataSource extends ItemKeyedDataSource<Long, TinyPackList>{

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<TinyPackList> callback) {
            loadData(0L, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<TinyPackList> callback) {
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<TinyPackList> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Long getKey(@NonNull TinyPackList item) {
             if(item!=null){
                 return item.id;
            }else{
                 return 0L;
             }
        }
    }

    private void loadData(Long requestKey, ItemKeyedDataSource.LoadCallback<TinyPackList> callback) {
        if (requestKey > 0) {
            loadAfter.set(true);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Integer pageSize = 10;

        ApiResponse<TinyPackResponseInfo> response = ApiService.get("/Data/GetBarcode")
                .addParam("pageSize", pageSize)
//                .addParam("RwDate",sdf.format(new Date()))
//                .addParam("pageSize", 10)
                .addParam("offset", offset)
                .responseType(new TypeReference<TinyPackResponseInfo>() {
                }.getType())
                .execute();
        List<TinyPackList> result= Lists.newArrayList();
        TinyPackResponseInfo tinyPackResponseInfos = response.body == null ?null: response.body;
        if(tinyPackResponseInfos!=null&&tinyPackResponseInfos.getBarcodelist()!=null) {
            for(TinyPackResponseInfo.BarcodelistBean bean:tinyPackResponseInfos.getBarcodelist()){
                TinyPackList entity = new TinyPackList();
                if(bean.getId()==null||bean.getId()==0){
                    continue;
                }
                entity.setId(bean.getId());

                entity.setBarcode(bean.getBarcode());
                entity.setLastModifyTime(bean.getLastModifyTime());
                entity.setQuantity(bean.getQuantity());
                entity.setTitle("");
                result.add(entity);
            }
            callback.onResult(result);
        }
        if (requestKey > 0) {
            loadAfter.set(false);
            offset += result.size();
            ((MutableLiveData) getBoundaryPageData()).postValue(result.size() > 0);
        } else {
            offset =  result.size();
        }
    }
}
