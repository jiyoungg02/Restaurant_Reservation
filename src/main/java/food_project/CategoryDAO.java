package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class CategoryDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertCategory(CategoryDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO Category(category_id, category_name) "
					+ " VALUES(?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getCategory_id());
			pstmt.setString(2, dto.getCategory_name());
			
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
			}
		}
	}
	
	public void updateCategory(CategoryDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE Category SET category_name = ? "
					+ " WHERE category_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getCategory_name());
			pstmt.setString(2, dto.getCategory_id());
			
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
			}
		}
	}
	
	public void deleteCategory(String category_id) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM Category WHERE category_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, category_id);
			
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
			}
		}
	}
	
	public CategoryDTO findById(String category_id) {
		CategoryDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT category_id, category_name "
					+ " FROM Category "
					+ " WHERE category_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, category_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new CategoryDTO();
				
				dto.setCategory_id(rs.getString("category_id"));
				dto.setCategory_name(rs.getString("category_name"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public List<CategoryDTO> listCategory(){
		List<CategoryDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT category_id, category_name "
					+ " FROM Category ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryDTO dto = new CategoryDTO();
				
				dto.setCategory_id(rs.getString("category_id"));
				dto.setCategory_name(rs.getString("category_name"));
				
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
