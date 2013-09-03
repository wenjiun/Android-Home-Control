package my.edu.mmu.homecontrol;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(MainActivity.SENDER_ID);
	}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.d("HomeControl", intent.toString());		
		
		String light = intent.getStringExtra("light");
		
		Intent broadcast = new Intent("light");
		broadcast.setPackage(getPackageName());
		if(light.equals("off")) {
			broadcast.putExtra("light", true);			
		} else {
			broadcast.putExtra("light", false);						
		}
		sendBroadcast(broadcast);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		new RegisterTask(getApplicationContext()).execute(arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
