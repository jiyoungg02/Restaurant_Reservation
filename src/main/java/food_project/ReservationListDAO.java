package food_project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class ReservationListDAO {
	private Connection conn = DBConn.getConnection();

	// 삭제
	public void deleteReservation(String reservation_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "DELETE FROM Reservation WHERE reservation_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reservation_id);

			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);

			throw e;
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// 이용한 가게
	public List<ReservationDTO> getUsedReservations(String memberId) {
		List<ReservationDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.*, res.restaurant_name " + "FROM Reservation r "
					+ "JOIN Restaurant res ON r.restaurant_id = res.restaurant_id "
					+ "WHERE r.is_used = 'Y' AND r.member_id = ? " + "ORDER BY r.reservation_date, r.reservation_time";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setReservation_date(rs.getDate("reservation_date"));
				dto.setReservation_time(rs.getString("reservation_time"));
				dto.setIs_available(rs.getString("is_available"));
				dto.setNumber_of_people(rs.getInt("number_of_people"));
				dto.setIs_used(rs.getString("is_used"));
				dto.setRestaurant_id(rs.getString("restaurant_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setMember_id(rs.getString("member_id"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 이용예정 가게
	public List<ReservationDTO> getPlanReservations(String memberId) {
		List<ReservationDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.*, res.restaurant_name " + "FROM Reservation r "
					+ "JOIN Restaurant res ON r.restaurant_id = res.restaurant_id "
					+ "WHERE r.is_used = 'N' AND r.member_id = ? " + "ORDER BY r.reservation_date, r.reservation_time";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setReservation_date(rs.getDate("reservation_date"));
				dto.setReservation_time(rs.getString("reservation_time"));
				dto.setIs_available(rs.getString("is_available"));
				dto.setNumber_of_people(rs.getInt("number_of_people"));
				dto.setIs_used(rs.getString("is_used"));
				dto.setRestaurant_id(rs.getString("restaurant_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setMember_id(rs.getString("member_id"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 예약번호가 존재하는지 확인하는 메소드
	public boolean isValidReservation(String reservation_id, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT reservation_id FROM Reservation WHERE reservation_id = ? AND member_id = ? AND is_used = 'N'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reservation_id);
			pstmt.setString(2, memberId);
			rs = pstmt.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}

	// 예약 수정
	public void updateReservation(String reservation_id, Date reservation_date, String reservation_time,
			String number_of_people, String member_Id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT member_id FROM Reservation WHERE reservation_id = ?";

		try {
			conn.setAutoCommit(false);

			// 1. 예약 날짜가 오늘보다 이전인지 확인
			java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
			if (reservation_date.before(today)) {
				throw new SQLException("예약 불가능한 날짜입니다. 다시 입력해주세요.");
			}

			// 예약 ID에 해당하는 예약이 존재하는지 확인
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reservation_id);
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				throw new SQLException("예약 ID에 해당하는 데이터가 없습니다.");
			}

			String ownerId = rs.getString("member_id");

			// 로그인한 회원 ID와 예약의 회원 ID가 일치하는지 확인
			if (!ownerId.equals(member_Id)) {
				throw new SQLException("자신의 예약만 수정할 수 있습니다.");
			}

			// 예약 수정 SQL
			sql = "UPDATE Reservation SET reservation_date = ?, reservation_time = ?, number_of_people = ? WHERE reservation_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, reservation_date);
			pstmt.setString(2, reservation_time);
			pstmt.setString(3, number_of_people);
			pstmt.setString(4, reservation_id);

			int updated = pstmt.executeUpdate();

			if (updated == 0) {
				throw new SQLException("예약 수정에 실패했습니다.");
			}

			conn.commit();

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			System.out.println(e.getMessage()); // 예외 메시지를 출력 (사용자가 다시 날짜를 입력할 수 있도록 유도)
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	// 이용한가게로 정보 수정
	public void updateExpiredReservations() {
		PreparedStatement pstmt = null;
		String sql = "UPDATE Reservation " + "SET is_used = 'Y' " + "WHERE is_used = 'N' "
				+ "AND (reservation_date < TRUNC(SYSDATE) " + "OR (reservation_date = TRUNC(SYSDATE) "
				+ "AND TO_NUMBER(SUBSTR(reservation_time, 1, 2)) < TO_NUMBER(TO_CHAR(SYSDATE, 'HH24'))))";

		try {
			pstmt = conn.prepareStatement(sql);
			int updatedRows = pstmt.executeUpdate();
			if (updatedRows > 0) {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 전체 예약 점주 
	public List<ReservationDTO> getReservationsByOwner(String ownerId) {
		List<ReservationDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.reservation_id, r.reservation_date, r.reservation_time, r.number_of_people, "
					+ "r.is_used, m.name AS member_name, res.restaurant_name " + "FROM Reservation r "
					+ "JOIN Restaurant res ON r.restaurant_id = res.restaurant_id "
					+ "JOIN Member m ON r.member_id = m.member_id " + "WHERE res.owner_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ownerId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setReservation_date(rs.getDate("reservation_date"));
				dto.setReservation_time(rs.getString("reservation_time"));
				dto.setNumber_of_people(rs.getInt("number_of_people"));
				dto.setIs_used(rs.getString("is_used"));
				dto.setMember_name(rs.getString("member_name"));
				dto.setRestaurant_name(rs.getString("restaurant_name")); 

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	// 점주 가게 예약 예정
	public List<ReservationDTO> getReservationsByOwnerAndRestaurant(String ownerId, int restaurantId) {
		List<ReservationDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT r.reservation_id, m.name AS member_name, rs.restaurant_name, "
			           + "r.reservation_date, r.reservation_time, r.number_of_people "
			           + "FROM Reservation r "
			           + "JOIN Member m ON r.member_id = m.member_id "
			           + "JOIN Restaurant rs ON r.restaurant_id = rs.restaurant_id "
			           + "ORDER BY r.reservation_date, r.reservation_time";

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ownerId);          
	        pstmt.setInt(2, restaurantId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReservationDTO dto = new ReservationDTO();
				dto.setReservation_id(rs.getString("reservation_id"));
				dto.setMember_name(rs.getString("member_name"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setReservation_date(rs.getDate("reservation_date"));
				dto.setReservation_time(rs.getString("reservation_time"));
				dto.setNumber_of_people(rs.getInt("number_of_people"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	// 점주 가게 예약 후
	  public List<ReservationDTO> getReservationsByUsedStatus(String ownerId, String restaurantId, String isUsed) throws SQLException {
	        List<ReservationDTO> list = new ArrayList<>();
	    	PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

	        try{

		        sql = "SELECT r.reservation_id, m.name AS member_name, rs.restaurant_name, "
		                   + "r.reservation_date, r.reservation_time, r.number_of_people "
		                   + "FROM Reservation r "
		                   + "JOIN Member m ON r.member_id = m.member_id "
		                   + "JOIN Restaurant rs ON r.restaurant_id = rs.restaurant_id "
		                   + "WHERE rs.owner_id = ? AND rs.restaurant_id = ? AND r.is_used = ? "
		                   + "ORDER BY r.reservation_date, r.reservation_time";
		        
		        pstmt = conn.prepareStatement(sql);
		        
		        pstmt.setString(1, ownerId);
	            pstmt.setString(2, restaurantId);
	            pstmt.setString(3, isUsed);
	            
	            rs = pstmt.executeQuery();
 
	                while (rs.next()) {
	                    ReservationDTO dto = new ReservationDTO();
	                    dto.setReservation_id(rs.getString("reservation_id"));
	                    dto.setMember_name(rs.getString("member_name"));
	                    dto.setRestaurant_name(rs.getString("restaurant_name"));
	                    dto.setReservation_date(rs.getDate("reservation_date"));
	                    dto.setReservation_time(rs.getString("reservation_time"));
	                    dto.setNumber_of_people(rs.getInt("number_of_people"));
	                    list.add(dto);
	                }
	        } catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return list;
		}
	}


