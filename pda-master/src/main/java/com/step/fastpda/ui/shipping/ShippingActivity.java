package com.step.fastpda.ui.shipping;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Splitter;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.step.fastpda.R;
import com.step.fastpda.databinding.ActivityLayoutShippingAddBinding;
import com.step.fastpda.ui.login.BaseResponseInfo;
import com.step.fastpda.utils.StatusBar;
import com.step.fastpda.view.LoadingView;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class ShippingActivity extends AppCompatActivity implements  BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener{
    private static final int RESULT_CODE = 200;
    private ActivityLayoutShippingAddBinding mBinding;
    private BarcodeReader mBarcodeReader;
    private String mLastModifyTime;
    private LoadingView mLoadingView;//加载dailog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_shipping);

        setSupportActionBar(mToolbar);


        StatusBar.lightStatusBar(this, false);
        mBinding = ActivityLayoutShippingAddBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());
        //关闭对话框
        mBinding.iconShippingClose.setOnClickListener(e->{
            setResult(RESULT_CODE);
            finish();
        });
        mBinding.btnTinyShippingSubmit.setOnClickListener(e->{
            String param = mBinding.edShippingOrderSn.getText().toString();

            insertShipping(param);

        });
        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                mBarcodeReader = aidcManager.createBarcodeReader();
                initBarcodeReader(mBarcodeReader);
            }
        });
        mLoadingView= new LoadingView(this,R.style.CustomDialog);
    }

    /***
     * 新增小包标签
     */
    private void insertShipping(String mParam) {
        if(mParam.isEmpty()){
            return;
        }
        List<String> params=Splitter.on("%").splitToList(mParam);
        mLoadingView.show();

        new AsyncTask<String, Void, ApiResponse>() {
            //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
            @Override
            protected ApiResponse doInBackground(String... params) {
                ApiResponse apiResponse=  ApiService.post("/Data/ParsebarcodeMT")
                        .addParam("instockNo",params[0])
                        .addParam("inStockNumber",params[1])
                        .addParam("sumQty",params[2])
                        .addParam("stockNo",params[3])
                        .addParam("position",params[4])
                        .responseType(new TypeReference<BaseResponseInfo>() {
                        }.getType()).execute();

                return apiResponse;
            }

            //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
            // 后台的计算结果将通过该方法传递到UI线程，并且在界面上展示给用户.
            @Override
            protected void onPostExecute(ApiResponse apiResponse) {
                BaseResponseInfo responseInfo=null;

                if(apiResponse!=null&&apiResponse.body!=null){
                    responseInfo= (BaseResponseInfo) apiResponse.body;
                }
                mLoadingView.dismiss();
                mBinding.edShippingOrderSn.setText("");
                if(responseInfo==null||responseInfo.getErrCode().equals("0")){
                    Toast.makeText(ShippingActivity.this, responseInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ShippingActivity.this,"操作成功", Toast.LENGTH_SHORT).show();
            }
        }.execute(params.get(0),params.get(1),params.get(2),params.get(3),params.get(4));

    }

    /***
     * 初始化barcode
     * @param barcodeReader
     */
    private void initBarcodeReader(final BarcodeReader barcodeReader) {

        barcodeReader.addBarcodeListener(this);

        // set the trigger mode to client control
        try {
            barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
        } catch (UnsupportedPropertyException e) {
            Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
        }
        // register trigger state change listener
        barcodeReader.addTriggerListener(this);

        Map<String, Object> properties = new HashMap<String, Object>();
        // Set Symbologies On/Off
        properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
        properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
        properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
        properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
        // Set Max Code 39 barcode length
        properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
        // Turn on center decoding
        properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
        // Enable bad read response
        properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
        // Apply the settings
        barcodeReader.setProperties(properties);
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent event) {
        final String barcodeData = event.getBarcodeData();
        mLastModifyTime= event.getTimestamp();
        mBinding.edShippingOrderSn.setText(barcodeData);
        if(!barcodeData.isEmpty()) {
            insertShipping(barcodeData);
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        try {
            // only handle trigger presses
            // turn on/off aimer, illumination and decoding
            mBarcodeReader.aim(event.getState());
            mBarcodeReader.light(event.getState());
            mBarcodeReader.decode(event.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner is not claimed", Toast.LENGTH_SHORT).show();
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mBarcodeReader != null) {
            try {
                mBarcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mBarcodeReader != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            mBarcodeReader.release();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBarcodeReader != null) {
            // close BarcodeReader to clean up resources.
            mBarcodeReader.removeBarcodeListener(this);
            // unregister trigger state change listener
            mBarcodeReader.removeTriggerListener(this);
            mBarcodeReader.close();
            mBarcodeReader = null;
        }
       StatusBar.lightStatusBar(this, true);

    }



}
