package wifi.kevin.wifi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.os.Debug.startMethodTracing;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_PREFIX = "MAIN ACTIVITY";
    private static final List<String> profileList = Arrays.asList("Silent", "Priority", "Normal");
    public static HashMap<String, SavedWifiConfigurations> wifiConfigurationMap = new HashMap<>();

    private static AdapterView.OnItemSelectedListener wifiSpinnerClick, profileSpinnerClick;
    private static ArrayAdapter<String> availableWifiListAdapter, availableSoundProfilesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiSpinnerClick = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_PREFIX, "***************************");
                SavedWifiConfigurations selectedConfiguration = wifiConfigurationMap.get(parent.getItemAtPosition(position).toString());
                if (selectedConfiguration != null) {
                    selectedConfiguration.profileSpinner.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        // get current configured wifi for the device
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        List<String> wifiNameList = new ArrayList<String>();
        for(WifiConfiguration wifi: configuredNetworks) {
            wifiNameList.add(wifi.SSID);
        }

        availableWifiListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                wifiNameList);

        availableSoundProfilesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                profileList);

        LinearLayout savedWifiLayout = (LinearLayout) findViewById(R.id.configured_wifi_wrapper);

        // get wifi saved in the app
//        SharedPreferences savedPreference = getPreferences(Context.MODE_PRIVATE);



    }

    // add a new preference
    private void addNewPreference(LinearLayout savedWifiLayout, String selectedWifi, String selectedProfile) {

        if(selectedWifi == null) {
            for(int i=0; i < availableWifiListAdapter.getCount(); i++) {
                String wifi = availableWifiListAdapter.getItem(i);
                if(wifiConfigurationMap.containsKey(wifi)) {
                    continue;
                } else {
                    // wifi not yet configured
                    selectedWifi = wifi;
                }
            }
        }

        // there are no new wifi to configure
        if(selectedWifi == null) {
            return;
        }

        LinearLayout singleWifiDetails = new LinearLayout(this);
        singleWifiDetails.setPadding(0,30,0,0);
        singleWifiDetails.setOrientation(LinearLayout.HORIZONTAL);

        Spinner wifiSpinner = new Spinner(this);
        wifiSpinner.setOnItemSelectedListener(wifiSpinnerClick);
        wifiSpinner.setAdapter(availableWifiListAdapter);
        wifiSpinner.setPadding(5, 5, 5, 5);
        singleWifiDetails.addView(wifiSpinner);
        wifiSpinner.setSelection(availableWifiListAdapter.getPosition(selectedWifi));

        Spinner profileSpinner = new Spinner(this);
        profileSpinner.setAdapter(availableSoundProfilesAdapter);
        profileSpinner.setPadding(5, 5, 5, 5);
        singleWifiDetails.addView(profileSpinner);
        if(selectedProfile != null) {
            profileSpinner.setSelection(availableSoundProfilesAdapter.getPosition(selectedProfile));
        }
        wifiConfigurationMap.put(selectedWifi, new SavedWifiConfigurations(wifiSpinner, profileSpinner));
        savedWifiLayout.addView(singleWifiDetails);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public class SavedWifiConfigurations {
        public Spinner wifiSpinner, profileSpinner;

        public SavedWifiConfigurations(Spinner wifiSpinner, Spinner profileSpinner) {
            this.wifiSpinner = wifiSpinner;
            this.profileSpinner = profileSpinner;
        }
    }

    public void createNewPreference(View button) {
        LinearLayout savedWifiLayout = (LinearLayout) findViewById(R.id.configured_wifi_wrapper);
        addNewPreference(savedWifiLayout, null, null);
    }
}
