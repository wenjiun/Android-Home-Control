package my.edu.mmu.homeserver;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class HomeControlServlet extends HttpServlet {
	


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");

        ArrayList<String> devices = new ArrayList<String>();        
        Query query = new Query("RegId");
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastoreService.prepare(query);    
        for (Entity regId : preparedQuery.asIterable()) {            
            String reg_id = (String) regId.getProperty("reg_id");
            devices.add(reg_id);
        }
		if(!devices.isEmpty()) {
			Sender sender = new Sender(ServerSetup.API_KEY);
			String light = req.getParameter("light");
			if(light == null) {
				resp.getWriter().println("To switch on light add to the url: ?light=on");				
				resp.getWriter().println("To switch off light add to the url: ?light=off");				
			} else {
				if(light.equals("on")) {
					Message message = new Message.Builder()
						.addData("light", "on")
						.build();					
					sender.send(message, devices, 5);	
					resp.getWriter().println("Light on");
				} else {
					Message message = new Message.Builder()
						.addData("light", "off")
						.build();					
					sender.send(message, devices, 5);	
					resp.getWriter().println("Light off");
				} 
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");

        ArrayList<String> devices = new ArrayList<String>();        
        Query query = new Query("RegId");
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastoreService.prepare(query);    
        for (Entity regId : preparedQuery.asIterable()) {            
            String reg_id = (String) regId.getProperty("reg_id");
            devices.add(reg_id);
        }
		if(!devices.isEmpty()) {
			Sender sender = new Sender(ServerSetup.API_KEY);
			String light = req.getParameter("light");
			if(light.equals("on")) {
				Message message = new Message.Builder()
					.addData("light", "on")
					.build();					
				sender.send(message, devices, 5);	
				resp.getWriter().println("Light on");
			} else {
				Message message = new Message.Builder()
					.addData("light", "off")
					.build();					
				sender.send(message, devices, 5);	
				resp.getWriter().println("Light off");
			}
		}
	}
}
