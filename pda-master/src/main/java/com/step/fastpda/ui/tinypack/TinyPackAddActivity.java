package com.step.fastpda.ui.tinypack;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.TypeReference;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.step.fastpda.R;
import com.step.fastpda.databinding.ActivityLayoutTinypackAddBinding;
import com.step.fastpda.ui.login.BaseResponseInfo;
import com.step.fastpda.ui.login.UserManager;
import com.step.fastpda.utils.StatusBar;
import com.tech.libnetwork.ApiResponse;
import com.tech.libnetwork.ApiService;
import com.tech.libnetwork.JsonCallback;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhushubin
 * @date 2020-08-14.
 * GitHub：
 * email： 604580436@qq.com
 * description：
 */
public class TinyPackAddActivity extends AppCompatActivity implements  BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener{
    private static final int RESULT_CODE = 200;
    private ActivityLayoutTinypackAddBinding mBinding;
    private BarcodeReader mBarcodeReader;
    private String mLastModifyTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
         Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_tinypack);

        setSupportActionBar(mToolbar);


        StatusBar.lightStatusBar(this, false);
        mBinding= ActivityLayoutTinypackAddBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());
        mBinding.iconTinyPackClose.setOnClickListener(e->{
            setResult(RESULT_CODE);
            finish();
        });


        mBinding.btnTinyPackSubmit.setOnClickListener(e->{
            insertTinyPack();
        });

        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                mBarcodeReader = aidcManager.createBarcodeReader();
                initBarcodeReader(mBarcodeReader);
            }
        });



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

    @Override
    public void onBarcodeEvent(BarcodeReadEvent event) {
        final String barcodeData = event.getBarcodeData();
        mLastModifyTime= event.getTimestamp();
        mBinding.edPackingSn.setText(barcodeData);
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

    /***
     * 新增小包标签
     */
    private void insertTinyPack(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String type="normal";
        if(mBinding.cbTinyPackAttach.isChecked()){
            type ="attach";
        }
        String barcode=mBinding.edPackingSn.getText().toString();
        String txtSL = mBinding.edPackingQuantity.getText().toString();
        ApiService.post("/Data/parsebarcode")
                .addParam("barcode", barcode)
                .addParam("txtSL",txtSL)
                .addParam("creator",UserManager.get().getUser().getName())
                .addParam("type", type)
                .responseType(new TypeReference<BaseResponseInfo>() {
                }.getType())
                .execute(new JsonCallback<BaseResponseInfo>() {
                    @Override
                    public void onSuccess(ApiResponse<BaseResponseInfo> response) {
                        super.onSuccess(response);
                        BaseResponseInfo baseResponseInfo = response.body!=null?response.body:null;
                        if(baseResponseInfo!=null&&baseResponseInfo.getErrCode().equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.edPackingQuantity.setText("0");
                                    mBinding.edPackingSn.setText("");
                                    mBinding.edPackingSn.requestFocus();
                                }
                            });
                        }else{
                            if(baseResponseInfo!=null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         Toast.makeText(TinyPackAddActivity.this, baseResponseInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ApiResponse<BaseResponseInfo> response) {
                        super.onError(response);
                        BaseResponseInfo baseResponseInfo = response.body!=null?response.body:null;
                        if(baseResponseInfo!=null&&!baseResponseInfo.getErrCode().equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.edPackingQuantity.setText("0");
                                    mBinding.edPackingSn.setText("");
                                    mBinding.edPackingSn.requestFocus();
                                    Toast.makeText(TinyPackAddActivity.this, baseResponseInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

}
