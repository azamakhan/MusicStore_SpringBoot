package cs636.music.presentation.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

// Stub for CatalogController (rename to MusicController if no SalesController supplied)
@Controller
public class CatalogController {

	// String constants for URLs
	private static final String WELCOME_URL = "/welcome.html";
	private static final String WELCOME_VIEW = "/Welcome1";
	private static final String USER_URL = "/userController/";

	@RequestMapping(WELCOME_URL)
	public String handleWelcome() {
		return WELCOME_VIEW;
	}
	
	@RequestMapping(USER_URL+"")
	public String CatalogDisplay() {
		return "";
	}
	
}






   