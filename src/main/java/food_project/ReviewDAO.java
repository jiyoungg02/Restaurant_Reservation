package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class ReviewDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertReview(ReviewDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO Review(member_id, reservation_id, review_rating, review_etccomment) VALUES(?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  dto.getMember_id());
			pstmt.setString(2,  dto.getReservation_id());
			pstmt.setInt(3, dto.getReview_rating());
			pstmt.setString(4,  dto.getReview_etccomment());
			
			pstmt.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void insertReview_comment(ReviewDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE Review SET review_comment = ? WHERE reservation_id = ? AND member_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getReview_comment());
			pstmt.setString(2,  dto.getReservation_id());
			pstmt.setString(3,  dto.getMember_id());
			
			pstmt.executeUpdate();
			conn.commit();
		
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
	public void updateReview(ReviewDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			sql = "UPDATE Review SET review_rating = ?, review_etccomment =? WHERE member_id = ? AND reservation_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getReview_rating());
			pstmt.setString(2,  dto.getReview_etccomment());
			pstmt.setString(3,  dto.getMember_id());
			pstmt.setString(4,  dto.getReservation_id());
			
			pstmt.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public void updateReview_comment(ReviewDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE Review SET review_comment = ? WHERE reservation_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getReview_comment());
			pstmt.setString(2,  dto.getReservation_id());
			
			pstmt.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}			
	}
	
	
	
	
	public int deleteReview(String reservation_id, String member_id) throws SQLException {
	    int result = 0;
		PreparedStatement pstmt = null;
	    String sql;

	    try {
	    	conn.setAutoCommit(false);
	    	sql = "DELETE FROM review WHERE reservation_id = ? AND member_id = ?";
	    	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, reservation_id);
	        pstmt.setString(2, member_id);
	        
	        result = pstmt.executeUpdate();
	        conn.commit();
	        
	    } catch(SQLException e) {
	    	DBUtil.rollback(conn);
	    	throw e;
	    } finally {
	        DBUtil.close(pstmt);
	    }
	    return result;
	}
	
	public int deleteReview_comment(String reservation_id, String member_id) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
	    String sql;

	    try {
	    	conn.setAutoCommit(false);
	    	sql = "UPDATE review SET review_comment = null WHERE reservation_id = ? AND member_id = ?";
	    	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, reservation_id);
	        pstmt.setString(2, member_id);
	        
	        result = pstmt.executeUpdate();
	        conn.commit();
	        
	    } catch(SQLException e) {
	    	DBUtil.rollback(conn);
	    	throw e;
	    } finally {
	        DBUtil.close(pstmt);
	    }
	    return result;
	}
	
	public List<ReviewDTO> admin_listReview(String restaurant_name) {
		List<ReviewDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT rv.member_id, rv.reservation_id, restaurant_name, review_rating, review_etccomment, review_comment "
					+ " FROM Review rv "
					+ " JOIN Reservation rsv ON rv.reservation_id = rsv.reservation_id "
					+ " JOIN Restaurant rt ON rsv.restaurant_id = rt.restaurant_id "
					+ " WHERE restaurant_name = ?"
					+ " ORDER BY review_rating ASC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant_name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				
				dto.setReview_rating(rs.getInt("review_rating"));
				dto.setReview_etccomment(rs.getString("review_etccomment"));
				dto.setReview_comment(rs.getString("review_comment"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list; 
	}
	
	public List<ReviewDTO> member_listReview(String restaurant_name) {
		List<ReviewDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT restaurant_name, review_rating, review_etccomment, review_comment "
					+ " FROM Review rv "
					+ " JOIN Reservation rsv ON rv.reservation_id = rsv.reservation_id "
					+ " JOIN Restaurant rt ON rsv.restaurant_id = rt.restaurant_id "
					+ " WHERE restaurant_name = ?"
					+ " ORDER BY review_rating DESC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant_name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				
				dto.setReview_rating(rs.getInt("review_rating"));
				dto.setReview_etccomment(rs.getString("review_etccomment"));
				dto.setReview_comment(rs.getString("review_comment"));
				
				list.add(dto);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}	
		return list; 
	}
	
	public List<ReviewDTO> owner_listReview(String owner_id) {
		List<ReviewDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT rv.member_id, rv.reservation_id, rv.member_id, restaurant_name, review_rating, review_etccomment, review_comment "
					+ " FROM Review rv "
					+ " JOIN Reservation rsv ON rv.reservation_id = rsv.reservation_id "
					+ " JOIN Restaurant rt ON rsv.restaurant_id = rt.restaurant_id "
					+ " JOIN Owner o ON rt.owner_id = o.owner_id "
					+ " WHERE o.owner_id = ?"
					+ " ORDER BY restaurant_name ASC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, owner_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setReview_rating(rs.getInt("review_rating"));
				dto.setReview_etccomment(rs.getString("review_etccomment"));
				dto.setReview_comment(rs.getString("review_comment"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list; 
	}
	
	public List<ReviewDTO> reservation_findById(String reservation_id) {
		List<ReviewDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT rv.member_id, rv.reservation_id, rv.member_id, restaurant_name, review_rating, review_etccomment, review_comment "
					+ " FROM Review rv "
					+ " JOIN Reservation rsv ON rv.reservation_id = rsv.reservation_id "
					+ " JOIN Restaurant rt ON rsv.restaurant_id = rt.restaurant_id "
					+ " JOIN Owner o ON rt.owner_id = o.owner_id "
					+ " WHERE rv.reservation_id = ?"
					+ " ORDER BY restaurant_name ASC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reservation_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setReview_rating(rs.getInt("review_rating"));
				dto.setReview_etccomment(rs.getString("review_etccomment"));
				dto.setReview_comment(rs.getString("review_comment"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list; 
	}
	
	
}

