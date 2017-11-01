package com.example.tzech.messsim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.Locale;

public class Measure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        initializeWiFiListener();

        TextView value1 = (TextView) findViewById(R.id.value1);
        value1.setText("0");
        TextView value2 = (TextView) findViewById(R.id.value2);
        value2.setText("20.9");
        TextView value3 = (TextView) findViewById(R.id.value3);
        value3.setText("0");
        TextView value4 = (TextView) findViewById(R.id.value4);
        value4.setText("0");

    }

    private void initializeWiFiListener(){
        String connectivity_context = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager)getSystemService(connectivity_context);

        if(!wifi.isWifiEnabled()){
            if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
                wifi.setWifiEnabled(true);
            }
        }

        registerReceiver(new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                WifiInfo info = wifi.getConnectionInfo();
                //TODO: implement methods for action handling
                int rssiVal = info.getRssi();
                TextView value1 = (TextView) findViewById(R.id.value1);
                TextView value2 = (TextView) findViewById(R.id.value2);
                TextView value4 = (TextView) findViewById(R.id.value4);

                double normVal = (200. + (double) rssiVal) / 200.;

                double maxUEG = 1.;
                double ueg = maxUEG * normVal;
                value1.setText(String.format(Locale.ENGLISH, "%.1f", ueg));

                double ueg2percentFactor = 0.03;
                double o2Val = Double.parseDouble(value2.getText().toString());
                o2Val = o2Val - ueg * ueg2percentFactor;
                value1.setText(String.format(Locale.ENGLISH, "%.1f", ueg));


                int pidVal = (int) (ueg * ueg2percentFactor * 10000.);
                if (pidVal < 2000) {
                    value4.setText(Integer.toString(pidVal));
                }
                else {
                    value4.setText(Html.fromHtml("&#8593&#8593&#8593&#8593"));
                }

            }

        }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

}


