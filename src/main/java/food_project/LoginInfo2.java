package food_project;

public class LoginInfo2 {
	private OwnerDTO loginOwner = null;
	
	public OwnerDTO loginOwner() {
		return loginOwner;
	}
	
	public void login(OwnerDTO loginOwner) {
		this.loginOwner = loginOwner;
	}
	
	public void logout() {
		loginOwner = null;
	}
}
