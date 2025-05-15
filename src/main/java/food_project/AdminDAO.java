package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class AdminDAO {
	private Connection conn = DBConn.getConnection();

	// 회원검색
		public MemberDTO findById(int ch1, String ch2) {
			MemberDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT m.member_id, pwd, name, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel, email " 
						+ "FROM Member m "
						+ "LEFT OUTER JOIN Member_details d ON m.member_id = d.member_id " 
						+ "WHERE ";

				switch(ch1) {
				case 1 : sql += " m.member_id = ? "; break;
				case 2 : sql += " name = ? "; break;
				case 3 : sql += " birth = ? "; break;
				case 4 : sql += " email = ? "; break;
				case 5 : sql += " tel = ? "; break;
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ch2);
				rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new MemberDTO();

					dto.setMember_id(rs.getString("member_id"));
					dto.setPwd(rs.getString("pwd"));
					dto.setName(rs.getString("name"));
					dto.setBirth(rs.getString("birth"));
					dto.setTel(rs.getString("tel"));
					dto.setEmail(rs.getString("email"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return dto;
		}
	
	
	// 회원 전체리스트
	public List<MemberDTO> listMember() {
		List<MemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT m.member_id, m.pwd, m.name, TO_CHAR(d.birth, 'YYYY-MM-DD') AS birth, d.tel, d.email " + "FROM member m "
					+ "LEFT OUTER JOIN member_details d ON m.member_id = d.member_id";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setMember_id(rs.getString("member_id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));

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
	// 즐겨찾기 회원리스트
	public List<Object> listFavorites() {
		List<Object> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT member_id, f.restaurant_id,restaurant_name "
					+ " FROM FAVORITES f"
					+ " LEFT OUTER JOIN Restaurant r ON f.restaurant_id = r.restaurant_id ";
					
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				RestaurantDTO rdto = new RestaurantDTO();
				
				dto.setMember_id(rs.getString("member_id"));
				rdto.setRestaurant_id(rs.getString("restaurant_id"));
				rdto.setRestaurant_name(rs.getString("restaurant_name"));

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
