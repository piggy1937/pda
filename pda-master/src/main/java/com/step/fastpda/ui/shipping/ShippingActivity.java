package com.step.fastpda.ui.shipping;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.step.fastpda.utils.NetworkDetector;
import com.step.fastpda.utils.StatusBar;
import com.step.fastpda.view.LoadingView;
import com.tech.libcommon.global.CallbackManager;
import com.tech.libcommon.global.CallbackType;
import com.tech.libcommon.global.IGlobalCallback;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;
import com.tech.libnetwork.JsonCallback;

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
    private EditText mEdShippingOrderSn;
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
        mEdShippingOrderSn = findViewById(R.id.ed_shipping_order_sn);
        //关闭对话框
        mBinding.iconShippingClose.setOnClickListener(e->{
            setResult(RESULT_CODE);
            finish();
        });
        mBinding.btnTinyShippingSubmit.setOnClickListener(e->{
            String mparam = mEdShippingOrderSn.getText().toString();
            insertShipping(mparam);

        });
        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                mBarcodeReader = aidcManager.createBarcodeReader();
                initBarcodeReader(mBarcodeReader);
            }
        });
        mLoadingView= new LoadingView(ShippingActivity.this,R.style.CustomDialog);

        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN_SHIPPING_PACK, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String barcode) {

                        insertShipping(barcode);
                    }
                });

    }

    /***
     * 新增小包标签
     */
    private void insertShipping(String mParam) {
        if(!NetworkDetector.detect(this)){
            Toast.makeText(ShippingActivity.this,"当期网络不可用，请稍后再试",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mParam.isEmpty()){
            Toast.makeText(ShippingActivity.this,"参数错误", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> params=Splitter.on("%").splitToList(mParam);
        mLoadingView.show();
        ApiService.post("/Data/ParsebarcodeMT")
                .addParam("instockNo",params.get(0))
                .addParam("inStockNumber",params.get(1))
                .addParam("sumQty",params.get(2))
                .addParam("stockNo",params.get(3))
                .addParam("position",params.get(4))
                .execute(new JsonCallback<BaseResponseInfo>() {
                    @Override
                    public void onSuccess(ApiResponse<BaseResponseInfo> response) {
                        super.onSuccess(response);
                        BaseResponseInfo baseResponseInfo = response.body!=null?response.body:null;
                        if(baseResponseInfo!=null&&baseResponseInfo.getErrCode().equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShippingActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                    mBinding.edShippingOrderSn.setText("");
                                    mLoadingView.dismiss();
                                }
                            });
                        }else{
                            if(baseResponseInfo!=null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ShippingActivity.this, baseResponseInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                        mBinding.edShippingOrderSn.setText("");
                                        mLoadingView.dismiss();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ApiResponse<BaseResponseInfo> response) {
                        super.onError(response);
                        BaseResponseInfo baseResponseInfo = response.body!=null?response.body:null;
                        if(baseResponseInfo!=null&&!baseResponseInfo.getErrCode().equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShippingActivity.this, baseResponseInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                    mBinding.edShippingOrderSn.setText("");
                                    mLoadingView.dismiss();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(ShippingActivity .this,baseResponseInfo.getErrMsg(),Toast.LENGTH_SHORT).show();
                                    mBinding.edShippingOrderSn.setText("");
                                    mLoadingView.dismiss();
                                }
                            });
                        }
                    }
                });

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
        if (Looper.myLooper() != Looper.getMainLooper()) {
            //切换到主进程
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mEdShippingOrderSn.setText(barcodeData);
                    insertShipping(barcodeData);
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEdShippingOrderSn.setText(barcodeData);
                    final IGlobalCallback<String> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_SCAN_SHIPPING_PACK);
                    if (callback != null) {
                        callback.executeCallback(barcodeData);
                    }

                }
            });
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

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
