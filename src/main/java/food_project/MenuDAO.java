package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class MenuDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertMenu(MenuDTO dto) throws SQLException {
	    int result = 0;
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	        
			conn.setAutoCommit(false);
			
			sql = " INSERT INTO Menu (menu_id, menu_name, menu_price, restaurant_id) VALUES (?, ?, ?, ?) "; 
			
			pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getMenu_id());
	        pstmt.setString(2, dto.getMenu_name());
	        pstmt.setInt(3, dto.getMenu_price());
	        pstmt.setString(4, dto.getRestaurant_id());
			
			result = pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	public boolean restaurantExists(String ownerId, String restaurantName) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT restaurant_id FROM Restaurant WHERE restaurant_name = ? AND owner_id = ?";
	    
	    try {
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, restaurantName);
	        pstmt.setString(2, ownerId);
	        rs = pstmt.executeQuery();
	        return rs.next();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }
	    return false;
	}
	
	public String getRestaurantId(String restaurantName, String ownerId) throws SQLException {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String restaurantId = null;
	    String sql;

	    try {
	    	sql = " SELECT restaurant_id FROM Restaurant WHERE restaurant_name = ? AND owner_id = ? ";
	    	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, restaurantName);
	        pstmt.setString(2, ownerId);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            restaurantId = rs.getString("restaurant_id");
	        }
	        
	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }
	    return restaurantId;
	}
	
	
	public int updateMenu(MenuDTO dto, String restaurant_name, String owner_id) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE Menu "
				  + "SET menu_id = ?, menu_name = ?, menu_price = ? "
				  + " WHERE menu_id = ? AND restaurant_id = (SELECT restaurant_id FROM Restaurant WHERE restaurant_name = ? AND owner_id = ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenu_id());
			pstmt.setString(2,  dto.getMenu_name());
			pstmt.setInt(3, dto.getMenu_price());
			pstmt.setString(4,  dto.getMenu_id());
			pstmt.setString(5,  dto.getRestaurant_name());
			pstmt.setString(6,  dto.getOwner_id());
			
			result = pstmt.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;	
	}
	
	
	public boolean menuExists(MenuDTO dto) throws SQLException {
	    boolean exists = false;
	    String sql;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	    	
	    	sql = "SELECT COUNT(*) FROM Menu WHERE menu_id = ? AND restaurant_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, dto.getMenu_id());
	        pstmt.setString(2, dto.getRestaurant_id());

	        rs = pstmt.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            exists = true;
	        }

	    } finally {
	        DBUtil.close(rs);
	        DBUtil.close(pstmt);
	    }

	    return exists;
	}
	
	public int deleteMenu(MenuDTO dto) throws SQLException {
		int result = 0;
	    PreparedStatement pstmt = null;
	    String sql;

	    try {
	    	conn.setAutoCommit(false);
	    	
	    	sql = "DELETE FROM Menu "
	    		  + " WHERE menu_id = ? AND restaurant_id = (SELECT restaurant_id FROM Restaurant WHERE restaurant_name = ? AND owner_id = ?) ";
	    	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1,  dto.getMenu_id());
	        pstmt.setString(2, dto.getRestaurant_name());
	        pstmt.setString(3, dto.getOwner_id());
	        
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
	
	public List<MenuDTO> member_listMenu(String restaurant_name) {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT m.restaurant_id, menu_name, menu_price, rt.restaurant_name "
	                + " FROM Menu m "
	                + " JOIN Restaurant rt ON m.restaurant_id = rt.restaurant_id "
	                + " WHERE rt.restaurant_name = ? "
	                + " ORDER BY menu_name ASC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant_name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setMenu_name(rs.getString("menu_name"));
				dto.setMenu_price(rs.getInt("menu_price"));
				
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				
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
	
	public List<MenuDTO> owner_listMenu(String owner_id, String restaurant_name) {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT menu_id, menu_name, menu_price, rt.restaurant_id, o.owner_id, rt.restaurant_name "
					+ " FROM Menu m "
					+ " LEFT JOIN Restaurant rt ON m.restaurant_id = rt.restaurant_id "
					+ " JOIN Owner o ON rt.owner_id = o.owner_id "
					+ " WHERE rt.restaurant_name = ? AND rt.owner_id = ?"
					+ " ORDER BY menu_id ASC ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant_name);
			pstmt.setString(2, owner_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setRestaurant_id(rs.getString("restaurant_id"));
				dto.setMenu_id(rs.getString("menu_id"));
				dto.setMenu_name(rs.getString("menu_name"));
				dto.setMenu_price(rs.getInt("menu_price"));
				
				dto.setOwner_id(rs.getString("owner_id"));
	            dto.setRestaurant_name(rs.getString("restaurant_name"));
				
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

