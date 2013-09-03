package my.edu.mmu.homeserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class HomeMonitorServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Latest Data: ");
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();  
		try {
		    Key tempKey = KeyFactory.createKey("TempKey", "tempKey");
		    Entity temp = datastoreService.get(tempKey);
		    String tempValue = (String)temp.getProperty("temp");
		    if(tempValue!=null) {
		    	resp.getWriter().println(tempValue);
		    }
		} catch (EntityNotFoundException e) {
		    e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	    Key tempKey = KeyFactory.createKey("TempKey", "tempKey");
		Entity temp = new Entity(tempKey);
		temp.setProperty("temp", req.getParameter("temp"));
		datastoreService.put(temp);
		resp.getWriter().append(temp + " successfully uploaded\r\n");

	}
}
