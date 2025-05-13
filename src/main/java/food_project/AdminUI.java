package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AdminUI {
	private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAO();
	private LoginInfo loginInfo = null;
	
	public AdminUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void permission() {
		
	}
	
	public void reservationlist() {
		
	}

	public void memberlist() {
		
	}
	
	public void reviewlist() {
		
	}
	
}
