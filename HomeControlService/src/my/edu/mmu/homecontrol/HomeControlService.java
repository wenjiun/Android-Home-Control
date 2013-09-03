package my.edu.mmu.homecontrol;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HomeControlService extends IOIOService {
	
	private boolean close = false;
	private BroadcastReceiver lightReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lightReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setClose(intent.getBooleanExtra("light", false));
				if(intent.getBooleanExtra("light", false)) {
					Toast.makeText(HomeControlService.this, "Light off", Toast.LENGTH_SHORT).show();	
				} else {					
					Toast.makeText(HomeControlService.this, "Light on", Toast.LENGTH_SHORT).show();	
				}
			}
		};
		IntentFilter filter = new IntentFilter("light");
		registerReceiver(lightReceiver, filter);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(lightReceiver);
	}
	
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {
			private DigitalOutput light;
			private DigitalOutput bulb;
			
			@Override
			protected void setup() throws ConnectionLostException, InterruptedException {
				light = ioio_.openDigitalOutput(IOIO.LED_PIN);
				bulb = ioio_.openDigitalOutput(32);
			}

			@Override
			public void loop() throws ConnectionLostException, InterruptedException {
				light.write(false);
				bulb.write(close);
				Thread.sleep(1000);
			}
		};
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (intent != null && intent.getAction() != null
				&& intent.getAction().equals("stop")) {
			// User clicked the notification. Need to stop the service.
			nm.cancel(0);
			stopSelf();
		} else {
			// Service starting. Create a notification.
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
			
			PendingIntent pi = PendingIntent.getService(this, 0, new Intent(
					"stop", null, this, this.getClass()), 0);
			builder.setContentTitle("IOIO Home Control Service")
				.setContentText("Click to stop")
				.setTicker("IOIO Home Control Service running")
				.setSmallIcon(R.drawable.ic_launcher)
				.setWhen(System.currentTimeMillis())
				.setContentIntent(pi);
			
			Notification notification = builder.build();
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			nm.notify(0, notification);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
