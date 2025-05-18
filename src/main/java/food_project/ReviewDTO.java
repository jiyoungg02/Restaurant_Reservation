package food_project;

public class ReviewDTO {
	private int review_rating;
	private String review_etccomment;
	private String review_comment;
	
	public int getReview_rating() {
		return review_rating;
	}
	public void setReview_rating(int review_rating) {
		this.review_rating = review_rating;
	}
	public String getReview_etccomment() {
		return review_etccomment;
	}
	public void setReview_etccomment(String review_etccomment) {
		this.review_etccomment = review_etccomment;
	}
	public String getReview_comment() {
		return review_comment;
	}
	public void setReview_comment(String review_comment) {
		this.review_comment = review_comment;
	}
	
	
	private String reservation_id;
	private String restaurant_name;
	
	public String getReservation_id() {
	    return reservation_id;
	}
	public void setReservation_id(String reservation_id) {
	    this.reservation_id = reservation_id;
	}

	public String getRestaurant_name() {
	    return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name) {
	    this.restaurant_name = restaurant_name;
	}
	
	
	private String member_id;
	public String Owner_id;
	public String getMember_id() {
	    return member_id;
	}
	public void setMember_id(String member_id) {
	    this.member_id = member_id;
	}
	public String getOwner_id() {
	    return Owner_id;
	}
	public void setOwner_id(String Owner_id) {
	    this.Owner_id = Owner_id;
	}
	
}