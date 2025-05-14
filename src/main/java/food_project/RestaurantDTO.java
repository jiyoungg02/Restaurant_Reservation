package food_project;

public class RestaurantDTO {
	private String restaurant_id; 
	private String restaurant_name; 
	private String restaurant_address;
	private String restaurant_tel;
	private int restaurant_count;
	private String opening_time;
	private String closing_time;
	private String restaurant_approve;
	private String owner_id;
	private String category_id;
	
	
	public String getRestaurant_id() {
		return restaurant_id;
	}
	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	public String getRestaurant_name() {
		return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}
	public String getRestaurant_address() {
		return restaurant_address;
	}
	public void setRestaurant_address(String restaurant_address) {
		this.restaurant_address = restaurant_address;
	}
	public String getRestaurant_tel() {
		return restaurant_tel;
	}
	public void setRestaurant_tel(String restaurant_tel) {
		this.restaurant_tel = restaurant_tel;
	}
	public int getRestaurant_count() {
		return restaurant_count;
	}
	public void setRestaurant_count(int restaurant_count) {
		this.restaurant_count = restaurant_count;
	}
	public String getOpening_time() {
		return opening_time;
	}
	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
	}
	public String getClosing_time() {
		return closing_time;
	}
	public void setClosing_time(String closing_time) {
		this.closing_time = closing_time;
	}
	public String getRestaurant_approve() {
		return restaurant_approve;
	}
	public void setRestaurant_approve(String restaurant_approve) {
		this.restaurant_approve = restaurant_approve;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	
}
