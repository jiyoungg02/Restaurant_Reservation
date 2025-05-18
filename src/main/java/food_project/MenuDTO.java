package food_project;

public class MenuDTO {
	private String menu_id;
	private String menu_name;
	private int menu_price;
	
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public int getMenu_price() {
		return menu_price;
	}
	public void setMenu_price(int menu_price) {
		this.menu_price = menu_price;
	}
	
	
	private String restaurant_id;
	public String getRestaurant_id() {
		return restaurant_id;
	}
	public void setRestaurant_id(String restaurant_id) {
	    this.restaurant_id = restaurant_id;
	}
	
	private String owner_id;
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
	    this.owner_id = owner_id;
	}
	
	private String restaurant_name;
	public String getRestaurant_name() {
		return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name) {
	    this.restaurant_name = restaurant_name;
	}
}
