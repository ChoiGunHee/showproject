package beacon.etri.dku.com.etribeaconreco.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import beacon.etri.dku.com.etribeaconreco.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startUserPositioning(View view) {
        Intent intent = new Intent(this, UserPositioningActivity.class);
        startActivity(intent);
    }

    public void startUserFaveUpdate(View view) {
        Intent intent = new Intent(this, UserFaveUpdateActivity.class);
        startActivity(intent);
    }

    public void startGridSetting(View view) {
        Intent intent = new Intent(this, GridSettingActivity.class);
        startActivity(intent);
    }
}
