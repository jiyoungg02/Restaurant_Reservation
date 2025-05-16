package food_project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.util.DBConn;
import db.util.DBUtil;


public class ReservationDAO {
	private Connection conn = DBConn.getConnection();
	
	// 음식점 코드 조회
	public boolean isRestaurantIdValid(String restaurantId) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT COUNT(*) FROM Restaurant WHERE restaurant_id = ?";

	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, restaurantId);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int count = rs.getInt(1);
	            return count > 0;  
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return false;  
	}
	
	// 음식점 수용 인원 조회
	public int getRestaurantCapacity(String restaurantId) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    int capacity = 0;

	    try {
	        sql = "SELECT restaurant_count FROM Restaurant WHERE restaurant_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, restaurantId);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            capacity = rs.getInt("restaurant_count");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return capacity;
	}
	
	//총 예약 인원 수
	public int getTotalReservedPeople(String restaurant_id, Date reservation_date, String reservation_time) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;
	    int total = 0;

	    try {
	        sql = "SELECT SUM(number_of_people) AS total "
	            + "FROM Reservation "
	            + "WHERE restaurant_id = ? AND reservation_date = ? AND reservation_time = ?";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, restaurant_id);
	        pstmt.setDate(2, reservation_date);
	        pstmt.setString(3, reservation_time);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            total = rs.getInt("total");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return total;
	}
	
	// 예약 추가 
	public void insertReservation(ReservationDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO Reservation(reservation_id, reservation_date,reservation_time,is_available,"
					+ "number_of_people,is_used,restaurant_id, member_id )"
					+ "VALUES(?,?,?,?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getReservation_id());
			pstmt.setDate(2, dto.getReservation_date());
			pstmt.setString(3, dto.getReservation_time());
			pstmt.setString(4, dto.getIs_available());
			pstmt.setInt(5, dto.getNumber_of_people());
			pstmt.setString(6, dto.getIs_used());
			pstmt.setString(7, dto.getRestaurant_id());
			pstmt.setString(8, dto.getMember_id());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		}catch (SQLException e) {
			DBUtil.rollback(conn);
			
			throw e;
		}finally {
			DBUtil.close(pstmt);
			
			try {
				
			} catch (Exception e2) {
				
			}
		}
	}
	
	

}
