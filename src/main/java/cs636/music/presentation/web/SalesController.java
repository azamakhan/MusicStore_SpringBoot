package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import cs636.music.service.SalesService;
import cs636.music.service.data.UserData;

@Controller
@SessionAttributes("user")
public class SalesController {

	@Autowired
	private SalesService salesService;
	
	private static final String USER_URL = "/userController/";
	private static final String USER_JSP_DIR = "/user/";

	@RequestMapping(USER_URL+"userWelcome.html")
	public String welcomeuser(Model model) {
	return USER_JSP_DIR+"UserWelcome";
	}
	
	@RequestMapping(USER_URL+"download")
	public String downloadPage(Model model) {
	return "forward:"+USER_URL+"registrationPage.html";
	}
	
	@RequestMapping(USER_URL+"Page.html")
	public String RegisterUser(Model model,@RequestParam(value = "email", required = false) String email,
			@RequestParam(value="fName" , required=false) String firstname, @RequestParam(value = 
			"lName", required = false) String lastname, @RequestParam(value="address" , required=false) String address,
			HttpServletRequest request,@RequestParam(value="productCode" , required = false) String productCode) throws ServletException {
		
		String forwardURL = USER_JSP_DIR+"UserLogin";
		String errorMessage="";
		if (errorMessage.length() > 0) {
			model.addAttribute("errorMessage", errorMessage);
			forwardURL = "error"; // show form again
		} else {
			UserBean user=(UserBean)request.getSession().getAttribute("user");
			System.out.println(user);
			if(user==null)
				user=new UserBean();
			if(user!=null)
				user.setEmailAddress(email);
			if(user.getEmailAddress()!=null)
				request.getSession().setAttribute("user",user);
		}
		
		try {
			UserData userdata=null;
			if(email!=null && salesService.userIsCustomer(email)==false)
			{ 
				salesService.registerUser(firstname, lastname, email);
				userdata=salesService.getUserInfoByEmail(email);
				salesService.addUserAddress(userdata.getId(), address);
				model.addAttribute("productCode", productCode);
				forwardURL = showProduct(model,productCode,request);
			   }
			
			else {
				model.addAttribute("productCode", productCode);
				forwardURL = showProduct(model,productCode,request);
			}
			 
		}
		catch(Exception e) {
			System.out.println("Exception occurred : " +e );
			throw new ServletException("Servlet Exception"+e);
		}
		return forwardURL;
	}
	
	@RequestMapping(USER_URL+"showProduct.html")
	public String showProduct(Model model,@RequestParam(value="productCode" , required = false) String productcode,HttpServletRequest request) {
		String url=null;
		String productCode=null;
		if(productcode==null) {
		 productCode=request.getParameter("productcode");
		}
		else {
			productCode=productcode;
		}
		try {
			if (checkUser(request)) {
				// user is logged in, can see product page
				model.addAttribute("productCode", productCode);
				url = determineProductView(model, productCode);
				//url = "forward:/userController/determineProductView";
			} else { // need to log in
				// remember chosen productCode for later
				request.setAttribute("productCode", productCode);
				model.addAttribute("pro",productCode);
				url = USER_JSP_DIR+"UserLogin";
			}
		} catch (IOException e) {
			request.setAttribute("error", "error accessing user: " + e);
			url = "/error.jsp";
		}
		
	return url;
	}
	
	@RequestMapping(USER_URL+"determineProductView")
	String determineProductView(Model model,@RequestParam(value="productCode" , required = false) String productCode) {
		String url = null;
		url = USER_JSP_DIR+ productCode + "_download";
		return url;
	}
	
	private boolean checkUser(HttpServletRequest request) throws IOException {
		 HttpSession session = request.getSession();
			UserBean user = (UserBean) session.getAttribute("user");
			return (user != null);
		}	
}
