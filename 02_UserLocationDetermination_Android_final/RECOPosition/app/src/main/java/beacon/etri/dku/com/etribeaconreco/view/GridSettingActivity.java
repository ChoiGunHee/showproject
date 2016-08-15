package beacon.etri.dku.com.etribeaconreco.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.SeekBar;
import android.widget.TextView;

import beacon.etri.dku.com.etribeaconreco.R;

/**
 * Created by user on 2016-01-19.
 */
public class GridSettingActivity extends Activity {
    public final static String DISTANCEGAP = "DistanceGap";

    SeekBar seekBar;
    TextView gapSizeTextView;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridsetting);

        gapSizeTextView = (TextView) findViewById(R.id.gapSizeTextView);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gapSizeTextView.setText("Grid Size : " + mSharedPreferences.getInt(DISTANCEGAP, 100));

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress( ((mSharedPreferences.getInt(DISTANCEGAP, 100)-50)/50) );
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                editor = mSharedPreferences.edit();
                editor.putInt(DISTANCEGAP, (50 + i * 50));
                editor.commit();

                gapSizeTextView.setText("Grid Size : " + mSharedPreferences.getInt(DISTANCEGAP, 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
