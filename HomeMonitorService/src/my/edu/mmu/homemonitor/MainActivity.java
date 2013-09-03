package my.edu.mmu.homemonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private BroadcastReceiver temperatureReceiver;
	private TextView textViewData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textViewData = (TextView)findViewById(R.id.textViewData);
		startService(new Intent(this, HomeMonitorService.class));
		
		temperatureReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				float volts = intent.getFloatExtra("voltage", -1);
				if(volts!=-1) {
					textViewData.setText("" + volts);
				}
			}
		};
		IntentFilter filter = new IntentFilter("thermistor");
		registerReceiver(temperatureReceiver, filter);
		//finish();
	}
}