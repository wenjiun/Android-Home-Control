package my.edu.mmu.homecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	
	public static final String SENDER_ID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
        	GCMRegistrar.register(this, SENDER_ID);
		} 
		startService(new Intent(this, HomeControlService.class));	
		finish();	
	}
}