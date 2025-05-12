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
	
}
