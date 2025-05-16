package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;


public class MemberDAO {
	private Connection conn = DBConn.getConnection();

	// 회원등록
	public int insertMember(MemberDTO dto) throws SQLException {
	int result = 0;
	PreparedStatement pstmt = null;
	String sql;

	try {

		conn.setAutoCommit(false);

		sql = "INSERT INTO Member(member_id, pwd, name) VALUES (?,?,?)";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, dto.getMember_id());
		pstmt.setString(2, dto.getPwd());
		pstmt.setString(3, dto.getName());
		result = pstmt.executeUpdate();
		pstmt.close();

		sql = "INSERT INTO Member_details(member_id,birth,tel,email) VALUES (?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, dto.getMember_id());
		pstmt.setString(2, dto.getBirth());
		pstmt.setString(3, dto.getTel());
		pstmt.setString(4, dto.getEmail());
		pstmt.executeUpdate();

		conn.commit();

	} catch (SQLException e) {

		if (conn != null) {
			conn.rollback();
		}
		throw e;
	} finally {
		DBUtil.close(pstmt);
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	return result;
}
// 회원수정
public int updateMember(MemberDTO dto) throws SQLException {
	int result = 0;
	PreparedStatement pstmt = null;
	String sql;

	try {
		conn.setAutoCommit(false);
		
		sql = "UPDATE Member SET pwd=?, name=? WHERE member_id=?";
		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, dto.getPwd());
		pstmt.setString(2, dto.getName());
		pstmt.setString(3, dto.getMember_id());

		result = pstmt.executeUpdate();

		pstmt.close();

		sql = "UPDATE Member_details SET birth =?, tel = ?, email =? "
				+ "WHERE member_id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, dto.getBirth());
		pstmt.setString(2, dto.getTel());
		pstmt.setString(3, dto.getEmail());
		pstmt.setString(4, dto.getMember_id());

		pstmt.executeUpdate();

		conn.commit();

	} catch (SQLException e) {

		if (conn != null) {
			conn.rollback();
		}
		throw e;
	} finally {
		DBUtil.close(pstmt);
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	return result;
}

	// 회원 삭제
public void deleteMember(String member_id) throws SQLException {
	PreparedStatement pstmt = null;
	String sql;

	try {
		conn.setAutoCommit(false);

		sql = "DELETE FROM MEMBER_DETAILS WHERE member_id = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member_id);

		pstmt.executeUpdate();
		pstmt.close();
		pstmt = null;

		sql = "DELETE FROM MEMBER WHERE member_id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, member_id);

		pstmt.executeUpdate();

		conn.commit();
	} catch (SQLException e) {

		if (conn != null) {
			conn.rollback();
		}
		throw e;
	} finally {
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

// 회원검색
public MemberDTO findById(String member_id) {
	MemberDTO dto = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql;

	try {
		sql = "SELECT m.member_id, pwd, name, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel, email " 
				+ "FROM Member m "
				+ "LEFT OUTER JOIN Member_details d ON m.member_id = d.member_id " 
				+ "WHERE m.member_id = ?";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, member_id);

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

//회원 즐겨찾기 추가
public int insertFavorites(String member_id,String restaurant_id) throws SQLException {
	int result = 0;
	PreparedStatement pstmt = null;
	String sql;

	try {

		conn.setAutoCommit(false);

		sql = "INSERT INTO Favorites(member_id, restaurant_id) "
				+ "VALUES (?,?)";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, member_id);
		pstmt.setString(2, restaurant_id);
		result = pstmt.executeUpdate();
		pstmt.close();


		conn.commit();

	} catch (SQLException e) {

		if (conn != null) {
			conn.rollback();
		}
		throw e;
	} finally {
		DBUtil.close(pstmt);
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	return result;
}
// 회원 즐겨찾기 삭제	
public void deleteFavorites(String member_id, String restaurant_id) throws SQLException {
	PreparedStatement pstmt = null;
	String sql;

	try {
		conn.setAutoCommit(false);

		sql = "DELETE FROM FAVORITES WHERE member_id = ? and restaurant_id = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member_id);
		pstmt.setString(2, restaurant_id);
	
		pstmt.executeUpdate();
		pstmt.close();
		pstmt = null;

		conn.commit();
	} catch (SQLException e) {

		if (conn != null) {
			conn.rollback();
		}
		throw e;
	} finally {
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

// 즐겨찾기 회원검색
public List<Object> FavoritesById(String member_id) {
	List<Object> list = new ArrayList<>();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql;

	
	try {
		sql = "SELECT f.restaurant_id,restaurant_name "
				+ " FROM FAVORITES f"
				+ " LEFT OUTER JOIN Restaurant r ON f.restaurant_id = r.restaurant_id"
				+ " WHERE member_id = ? ";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member_id); 
		rs = pstmt.executeQuery();

		while (rs.next()) {
			RestaurantDTO rdto = new RestaurantDTO();

			rdto.setRestaurant_id(rs.getString("restaurant_id"));
			rdto.setRestaurant_name(rs.getString("restaurant_name"));
			
			list.add(rdto);
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
			list.add(rdto);
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
