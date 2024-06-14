package com.example.locationpromise;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.MapsInitializer;

public class MainActivity extends AppCompatActivity {
    private Button btClientContinue;
    private TextView tvResultContinue;

    private AMapLocationClient locationClientSingle = null;
    private AMapLocationClient locationClientContinue = null;

    private int continueCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btClientContinue = (Button)findViewById(R.id.bt_startClient2);

        tvResultContinue = (TextView)findViewById(R.id.tv_result2);
    }
    // 连续定位
    public void continueLocation(View view) throws Exception {
        Log.d("my", String.valueOf(btClientContinue.getText().toString() == "开始定位"));
        if (btClientContinue.getText().equals("开始定位")) {
            startContinueLocation();
            btClientContinue.setText("停止定位");
            tvResultContinue.setText("正在定位...");
            continueCount = 0;
        } else {
            stopContinueLocation();
            btClientContinue.setText(R.string.startLocation);
        }
    }
    /**
     * 启动连续客户端定位
     */
    void startContinueLocation() throws Exception {
        Log.i("MY","开始定位");
        if(null == locationClientContinue){
            MapsInitializer.updatePrivacyShow(this, true, true);
            MapsInitializer.updatePrivacyAgree(this, true);
            locationClientContinue = new AMapLocationClient(this.getApplicationContext());
        }

        //使用连续的定位方式  默认连续
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        // 地址信息
        locationClientOption.setNeedAddress(true);
        locationClientContinue.setLocationOption(locationClientOption);
        locationClientContinue.setLocationListener(locationContinueListener);
        locationClientContinue.startLocation();
    }

    /**
     * 停止连续客户端
     */
    void stopContinueLocation(){
        if(null != locationClientContinue){
            locationClientContinue.stopLocation();
        }
    }
    /**
     * 连续客户端的定位监听
     */
    AMapLocationListener locationContinueListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            continueCount ++;
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("持续定位完成 " + continueCount +  "\n");
            sb.append("回调时间: " + Utils.formatUTC(callBackTime, null) + "\n");
            if(null == location){
                sb.append("定位失败：location is null!!!!!!!");
            } else {
                sb.append(Utils.getLocationStr(location));
            }

            tvResultContinue.setText(sb.toString());
        }
    };
}