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

	public int updateMember(MemberDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
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

	public MemberDTO readMember(String member_id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT member_id,pwd,name,TO_CHAR(birth, 'YYYY-MM-DD') birth, tel" 
					+ " FROM member"
					+ " WHERE member_id = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, member_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setMember_id("member_id");
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	public void deleteMember(String member_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "DELETE FROM Member_details WHERE member_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			sql = "DELETE FROM Member WHERE member_id = ?";

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
				dto.setMember_id("member_id");
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

	
	public MemberDTO FavoritesById(String member_id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		
		try {
			sql = "SELECT m.member_id, restaurant_id "
					+ " FROM member m "
					+ " LEFT OUTER JOIN FAVORITES f ON m.member_id = f.member_id "
					+ " WHERE m1.member_id = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();

				dto.setMember_id(rs.getString("member_id"));
				dto.setPwd(rs.getString("pwd"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	
}
