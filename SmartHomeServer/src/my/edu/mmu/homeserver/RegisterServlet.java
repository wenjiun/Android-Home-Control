package my.edu.mmu.homeserver;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Entity regId = new Entity("RegId");
		if(req.getParameter("regId")!=null && !req.getParameter("regId").equals("")) {
			regId.setProperty("reg_id", req.getParameter("regId"));
			datastoreService.put(regId);
			resp.getWriter().append(regId + " successfully registered\r\n");
		} else {
			resp.getWriter().append("No registration ID\r\n");			
		}
	}

	
	
}
