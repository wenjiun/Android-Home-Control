package my.edu.mmu.homemonitor;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HomeMonitorService extends IOIOService {
	@Override
	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {
			private DigitalOutput led_;
			private AnalogInput in;
			private float volts;
			
			@Override
			protected void setup() throws ConnectionLostException, InterruptedException {
				led_ = ioio_.openDigitalOutput(IOIO.LED_PIN);
				in = ioio_.openAnalogInput(31);
			}

			@Override
			public void loop() throws ConnectionLostException, InterruptedException {
				led_.write(false);
				volts = in.getVoltage();
				Intent i = new Intent("thermistor");
				i.setPackage(getPackageName());
				i.putExtra("voltage", volts);
				Log.d("SmartHome", "analog in: " + volts);
				sendBroadcast(i);
				postData(volts);
				Thread.sleep(60000);
			}
		};
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
			builder.setContentTitle("IOIO Service")
				.setContentText("Click to stop")
				.setTicker("IOIO service running")
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
	
	private void postData(float volts) {
		String urlString = "http://android-smart-home.appspot.com/homemonitor";
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		    connection.setConnectTimeout(10000);
		    connection.setDoOutput(true);
		       
		    String data = "temp=" + volts;
		       
		    OutputStream os = connection.getOutputStream();
		    os.write(data.getBytes());
		    os.flush();
		    os.close();
		    InputStream is = connection.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader reader = new BufferedReader(isr, 8);
		    StringBuilder builder = new StringBuilder();
		    String line = "";
		    while((line = reader.readLine())!=null) {
		     builder.append(line);
		    }
		    reader.close();
		    isr.close();
		    is.close();
		    String response = builder.toString();
			Log.d("SmartHome", response);

		    if(response.contains("successfully uploaded")) {
		    	Log.d("SmartHome", volts + " uploaded");
		    }   
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
