
package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cs636.music.config.MusicSystemConfig;
import cs636.music.presentation.SystemTest;
import cs636.music.service.CatalogService;
import cs636.music.service.SalesService;
import cs636.music.service.ServiceException;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

@Controller
@SessionAttributes("admin")
public class AdminController {

@Autowired
private CatalogService catalogService;

@Autowired
private SalesService salesService;
	
@Value("${spring.datasource.url}")
private String dbUrl; 
private static final String ADMIN_URL="/adminController/";
private static final String ADMIN_JSP_DIR="/admin/";
	
	
@RequestMapping("Welcome1.html")
public String Welcome(Model model) {
	return "Welcome1";
}
	
@RequestMapping(ADMIN_URL+"adminLoginForm.html")
public String adminLoginPage(Model model) {
	return "admin/adminLogin";
}

@RequestMapping(ADMIN_URL+"adminServices.html")
public String AdminServices(Model model,HttpServletRequest request) {
	 return ADMIN_JSP_DIR+"adminServices";
}

@RequestMapping(ADMIN_URL+"listVariables.html")
public String listVariables(Model model) {	
	model.addAttribute("dbUrl", dbUrl);
	System.out.println("dbUrl from application.properties: " + dbUrl);
	String url = ADMIN_JSP_DIR + "listVariables";
	return url;
}

@RequestMapping(ADMIN_URL+"initializeDB.html")
public String adminInitDB(Model model) {
	String info;
	try {
		salesService.initializeDB();
		catalogService.initializeDB();
		SystemTest test = new SystemTest(catalogService, salesService);
		test.runSystemTest();
		info = "Initialize db: success";
	} catch (Exception e) {
		info = "Initialize db: failed " + MusicSystemConfig.exceptionReport(e);
	}
	model.addAttribute("info", info);
	String url = ADMIN_JSP_DIR + "initializeDB";
	return url;
}

@RequestMapping(ADMIN_URL+"DisplayReport.html")
public String DisplayReport(Model model) {
	return ADMIN_JSP_DIR+"DisplayReport";
}

private boolean checkAdmin(HttpServletRequest request) throws IOException {
	 HttpSession session = request.getSession();
		AdminBean admin = (AdminBean) session.getAttribute("admin");
		return (admin != null);
	}

@RequestMapping(ADMIN_URL+"showAllDownloads.html")
public String showAllDownloads(Model model, @RequestParam(value = "command", required = false) String command){
	Set<DownloadData> allDownload=null;
	String ForwardUrl = null;
	try {
		allDownload=catalogService.getListofDownloads();
		
	}
	catch(ServiceException e) {System.out.println(e);}
	
	if(allDownload!=null) {
		model.addAttribute("allDown",allDownload);
		ForwardUrl=ADMIN_JSP_DIR+"showAllDownloads";
		}
		else {
			String nothing="There are no downloads to show";
			model.addAttribute("nothing", nothing);
			ForwardUrl=ADMIN_JSP_DIR+"showAllDownloads";
		}
	return ForwardUrl;
}

@RequestMapping(ADMIN_URL+"adminWelcome.html")
public String adminLogin(Model model,@RequestParam(value="username",required= false) String UserName,@RequestParam(value="password",required= false) String PassWord
		, HttpServletRequest request) throws ServletException{
		String forwardURL;
		String errorMessage="";
		if (errorMessage.length() > 0) {
			model.addAttribute("errorMessage", errorMessage);
			forwardURL = "error"; // show form again
		} else {		
		AdminBean admin=(AdminBean)request.getSession().getAttribute("admin");
		System.out.println(admin);
		if(admin==null)
			admin=new AdminBean();
		if(admin!=null)
			admin.setUserName(UserName);
		if(admin.getUserName()!=null)
			request.getSession().setAttribute("admin",admin);
	}
	
	try {
		boolean result =false;
		if( checkAdmin(request)){ 
			result=salesService.checkAdminLogin(UserName,PassWord);
			}
		 if(result==true) {
			 forwardURL="/admin/adminServices";
		 	}
		 else {
			 forwardURL="admin/adminLogin";
		 	}
		}
	catch(Exception e) {
		System.out.println("Exception occurred : " +e );
		throw new ServletException("Servlet Exception"+e);
			}
	return forwardURL;
}

@RequestMapping(ADMIN_URL+"processInvoice")
public String ProcessInvoice(Model model, @RequestParam(value = "command", required = false) String command,HttpServletRequest request) {
	Set<InvoiceData> invoices = null;
	String ForwardUrl = null;
	
	
	try {
		invoices=salesService.getListofUnprocessedInvoices();
		
	}
	catch(ServiceException e) {System.out.println(e);}
	
	if(invoices!=null && command==null) {
		model.addAttribute("Invoices",invoices);
		ForwardUrl=ADMIN_JSP_DIR+"ProcessInvoice";
		}
	else {
			String nothing="There are no downloads to show";
			model.addAttribute("nothing", nothing);
			ForwardUrl=ADMIN_JSP_DIR+"ProcessInvoice";
		}
	
	
	return ForwardUrl;
}

@RequestMapping(ADMIN_URL+"process")
public String Process(Model model,@RequestParam(value="id" , required = false) String id ,HttpServletRequest request) {
	String ForwardUrl=null;
	System.out.println("Processing invoice with id :"+ id);
	long id1=Long.parseLong(id);
	try {
		if(id!=null) {
		salesService.processInvoice(id1);
		ForwardUrl=ADMIN_JSP_DIR+"ProcessInvoice";
		}
		else {
			ForwardUrl="forward:processInvoice";
		}
	}
	catch(ServiceException e) {
		System.out.println(e);
		ForwardUrl="forward:processInvoice";
	}
	return ForwardUrl;
}

@RequestMapping(ADMIN_URL+"ToProcessInvoice.html")
public String ToProcessInvoice(Model model, @RequestParam(value = "command", required = false) String command){
	Set<InvoiceData> invoices = null;
	String ForwardUrl = null;
	try {
		invoices=salesService.getListofUnprocessedInvoices();
		
	}
	catch(ServiceException e) {System.out.println(e);}
	
	if(invoices!=null && command==null) {
		model.addAttribute("Invoices",invoices);
		ForwardUrl=ADMIN_JSP_DIR+"ToProcessInvoice";
		
		}
	else {
			String nothing="There are no downloads to show";
			model.addAttribute("nothing", nothing);
			ForwardUrl=ADMIN_JSP_DIR+"ToProcessInvoice";
		}
	
	return ForwardUrl;
}

@RequestMapping(ADMIN_URL+"logout.html")
public String logout(Model model, HttpServletRequest request) {	
	request.getSession().invalidate();  // drop session
	String url = ADMIN_JSP_DIR + "logout";
	return url;
}
}

