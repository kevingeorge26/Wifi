package wifi.kevin.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Spinner;

public class WifiStateReceiver extends BroadcastReceiver {

    private static String LOG_PREFIX = "WIFI RECEIVER";
    private static int counter = 0;

    public WifiStateReceiver() {
        Log.i(LOG_PREFIX, "inside the receiver contructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if(networkInfo != null && networkInfo.isConnected()) {
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            if(wifiInfo != null) {
                String newWifiName = wifiInfo.getSSID();
                Log.i(LOG_PREFIX, "**********" + newWifiName);
                if (MainActivity.wifiConfigurationMap.containsKey(newWifiName)) {
                    Spinner profileSpinner = MainActivity.wifiConfigurationMap.get(newWifiName).profileSpinner;
                    Log.i(LOG_PREFIX, (String) profileSpinner.getSelectedItem());
                }
            }

        }

    }


}
