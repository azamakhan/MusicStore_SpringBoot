package cs636.music.presentation.web;

public class AdminBean {

	
		
		// Properties used from student-oriented JSP pages--
		// roomNo is the only real session variable 
		private String UserName=null; 

		public AdminBean() {}
		
		public String getUserName() {
			return UserName;
		}

		public void setUserName(String UserName) {
			this.UserName = UserName;
		}
		
		public String toString() {
			return "AdminBean: UserName = "+ UserName;
		}
	

}
