package food_project;

import java.sql.Date;

public class ReservationDTO {
	private String reservation_id;
	 private Date reservation_date;
	 private String reservation_time;
	 private String is_available;
	 private int  number_of_people;
	 private String is_used;
	 private String restaurant_id;
	 private String member_id;
	 private String restaurant_name;
	 private String member_name;
	 
	public String getReservation_id() {
		return reservation_id;
	}
	public void setReservation_id(String reservation_id) {
		this.reservation_id = reservation_id;
	}
	public Date getReservation_date() {
		return reservation_date;
	}
	public void setReservation_date(Date reservation_date) {
		this.reservation_date = reservation_date;
	}
	public String getReservation_time() {
		return reservation_time;
	}
	public void setReservation_time(String reservation_time) {
		this.reservation_time = reservation_time;
	}
	public String getIs_available() {
		return is_available;
	}
	public void setIs_available(String is_available) {
		this.is_available = is_available;
	}
	public int getNumber_of_people() {
		return number_of_people;
	}
	public void setNumber_of_people(int number_of_people) {
		this.number_of_people = number_of_people;
	}
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public String getRestaurant_id() {
		return restaurant_id;
	}
	public void setRestaurant_id(String restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	
	public String getRestaurant_name() {
	    return restaurant_name;
	}
	
	public void setRestaurant_name(String restaurant_name) {
	    this.restaurant_name = restaurant_name;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	 
	

}
