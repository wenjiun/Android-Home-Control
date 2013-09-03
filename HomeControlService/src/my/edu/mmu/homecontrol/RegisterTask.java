package my.edu.mmu.homecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gcm.GCMRegistrar;

public class RegisterTask extends AsyncTask<String, Void, Void> {

	private Context context;
	
	public RegisterTask(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		String urlString = "http://android-smart-home.appspot.com/register";
	       
	       try {
	           URL url = new URL(urlString);
	           HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	           connection.setConnectTimeout(10000);
	           connection.setDoOutput(true);
	           
	           String data = "regId=" + params[0];
	           
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
	           if(response.contains("successfully registered")) {
	               GCMRegistrar.setRegisteredOnServer(context, true);
	           }         
	       } catch (MalformedURLException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	
}
