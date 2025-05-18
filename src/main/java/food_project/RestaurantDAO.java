package food_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class RestaurantDAO {
	private Connection conn = DBConn.getConnection();
	
	
	// 점주 음식점 등록 요청
	public int insertRestaurant(RestaurantDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO restaurant(restaurant_id, restaurant_name, restaurant_address, restaurant_tel, "
		            + " restaurant_count, opening_time, closing_time, restaurant_approve, owner_id, category_id) "
		            + " VALUES(?,?,?,?,?,?,?,?,?,?)"; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getRestaurant_id());
			pstmt.setString(2, dto.getRestaurant_name());
			pstmt.setString(3, dto.getRestaurant_address());
			pstmt.setString(4, dto.getRestaurant_tel());
			pstmt.setInt(5, dto.getRestaurant_count());
			pstmt.setString(6, dto.getOpening_time());
			pstmt.setString(7, dto.getClosing_time());
			pstmt.setString(8, "N");
			pstmt.setString(9, dto.getOwner_id());
			pstmt.setString(10, dto.getCategory_id());
			
			result = pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
			throw e;
		} finally {			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
			
			
		}
		return result;
		
	}
	
	// 점주 음식점 상세 정보 수정
	public int updateRestaurant(RestaurantDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE restaurant SET restaurant_name=?, restaurant_address=?, restaurant_tel=?,  "
					+ " restaurant_count=?, opening_time=?, closing_time=?, category_id=? "
					+ " WHERE restaurant_id=? AND owner_id=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getRestaurant_name());
			pstmt.setString(2, dto.getRestaurant_address());
			pstmt.setString(3, dto.getRestaurant_tel());
			pstmt.setInt(4, dto.getRestaurant_count());
			pstmt.setString(5, dto.getOpening_time());
			pstmt.setString(6, dto.getClosing_time());
			pstmt.setString(7, dto.getCategory_id());
			pstmt.setString(8, dto.getRestaurant_id());
			pstmt.setString(9, dto.getOwner_id());
			
			result = pstmt.executeUpdate();
			
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
			DBUtil.close(pstmt);
		}
		return result;
						
	}
	
	// 관리자 음식점 등록 승인
	public int apporveRestaurant(String restaurant_id, String approve) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		

        try {
            conn.setAutoCommit(false);
        	
        	sql  = "UPDATE restaurant SET restaurant_approve = TRIM(UPPER(?)) WHERE restaurant_id = ?";
        	
        	pstmt = conn.prepareStatement(sql);
        	
            pstmt.setString(1, approve); 
            pstmt.setString(2, restaurant_id);

            result = pstmt.executeUpdate();
            
            conn.commit();
        } catch (SQLException e) {
            DBUtil.rollback(conn);
            throw e; 
            
        }finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
        return result; 
    }
	
	// 점주 음식점 삭제 요청
	public int requestDeleteRestaurant(String id) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
				
		try {
			conn.setAutoCommit(false);
			
			sql = "UPDATE restaurant SET restaurant_approve = 'D' WHERE restaurant_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
			throw e;
		} finally {			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
		return result;
	}

	
	// 관리자 음식점 삭제 승인
	public int deleteRestaurant(String id) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
				
		try {
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM restaurant WHERE restaurant_id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
			throw e;
		} finally {			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	// 음식점 아이디 검색
	public RestaurantDTO findByIdrestaurant(String id) {
		RestaurantDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	        
	        sql = "SELECT restaurant_id, restaurant_name, "
	                + " restaurant_address, restaurant_tel, "
	                + " restaurant_count, opening_time, closing_time, "
	                + " restaurant_approve, category_id, owner_id "  
	                + " FROM restaurant "
	                + " WHERE restaurant_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new RestaurantDTO();
				
				 dto.setRestaurant_id(rs.getString("restaurant_id"));
		         dto.setRestaurant_name(rs.getString("restaurant_name"));
		         dto.setRestaurant_address(rs.getString("restaurant_address"));
		         dto.setRestaurant_tel(rs.getString("restaurant_tel"));
		         dto.setRestaurant_count(rs.getInt("restaurant_count"));
		         dto.setOpening_time(rs.getString("opening_time"));
		         dto.setClosing_time(rs.getString("closing_time"));
		         dto.setRestaurant_approve(rs.getString("restaurant_approve"));
		         dto.setCategory_id(rs.getString("category_id"));		            		     
		         dto.setOwner_id(rs.getString("owner_id")); 
				
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 음식점 승인 요청 확인
	public List<RestaurantDTO> RestaurantRequests(String approve) throws SQLException {
	    List<RestaurantDTO> list = new ArrayList<>();
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;


	    try {

	    	sql = "SELECT * FROM restaurant WHERE restaurant_approve = ?";
	    	
	    	pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, approve);  
	        rs = pstmt.executeQuery();

	        
	        
	        while (rs.next()) {
	            RestaurantDTO dto = new RestaurantDTO();
	            dto.setRestaurant_id(rs.getString("restaurant_id"));
	            dto.setRestaurant_name(rs.getString("restaurant_name"));
	            dto.setRestaurant_address(rs.getString("restaurant_address"));
	            dto.setRestaurant_tel(rs.getString("restaurant_tel"));
	            dto.setRestaurant_count(rs.getInt("restaurant_count"));
	            dto.setOpening_time(rs.getString("opening_time"));
	            dto.setClosing_time(rs.getString("closing_time"));
	            dto.setRestaurant_approve(rs.getString("restaurant_approve"));
	            dto.setOwner_id(rs.getString("owner_id"));
	            dto.setCategory_id(rs.getString("category_id"));

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
	
	
	// 음식점 리스트 
	public List<RestaurantDTO> restaurantlist() {
		List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			sql = "SELECT restaurant_id, restaurant_name, "
					+ " restaurant_address, restaurant_tel, "
					+ " restaurant_count, opening_time, closing_time, "
					+ " restaurant_approve, owner_id, category_id "
					+ " FROM restaurant "
					+ " ORDER BY restaurant_id";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_id(rs.getString("restaurant_id"));
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setRestaurant_address(rs.getString("restaurant_address"));
				dto.setRestaurant_tel(rs.getString("restaurant_tel"));
				dto.setRestaurant_count(rs.getInt("restaurant_count"));
				dto.setOpening_time(rs.getString("opening_time"));
				dto.setClosing_time(rs.getString("closing_time"));
				dto.setRestaurant_approve(rs.getString("restaurant_approve"));
				dto.setOwner_id(rs.getString("owner_id"));
				dto.setCategory_id(rs.getString("category_id"));
				
				list.add(dto);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	
	// 음식점 이름 검색
	public List<RestaurantDTO> RestaurantName(String name) {
		List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT restaurant_name, restaurant_address, restaurant_tel, restaurant_count, "
					+ " opening_time, closing_time, category_id "
					+ " FROM restaurant "
					+ " WHERE restaurant_approve = 'Y' AND INSTR(restaurant_name, ?) >= 1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setRestaurant_address(rs.getString("restaurant_address"));
				dto.setRestaurant_tel(rs.getString("restaurant_tel"));
				dto.setRestaurant_count(rs.getInt("restaurant_count"));
				dto.setOpening_time(rs.getString("opening_time"));
				dto.setClosing_time(rs.getString("closing_time"));
				dto.setCategory_id(rs.getString("category_id"));
				
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
	
	// 음식점 주소 검색
	public List<RestaurantDTO> RestaurantAddress(String address) {
		List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT restaurant_name, restaurant_address, restaurant_tel, restaurant_count, "
					+ " opening_time, closing_time, category_id "
					+ " FROM restaurant "
					+ " WHERE restaurant_approve = 'Y' AND INSTR(restaurant_address, ?) >= 1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, address);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setRestaurant_address(rs.getString("restaurant_address"));
				dto.setRestaurant_tel(rs.getString("restaurant_tel"));
				dto.setRestaurant_count(rs.getInt("restaurant_count"));
				dto.setOpening_time(rs.getString("opening_time"));
				dto.setClosing_time(rs.getString("closing_time"));
				dto.setCategory_id(rs.getString("category_id"));
				
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
	
	// 음식점 상세 정보 오픈/마감시간 검색
	public List<RestaurantDTO> RestaurantDetails(String name, String category_id) {
		List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT restaurant_name, opening_time, closing_time "
					+ " FROM restaurant "
					+ " WHERE restaurant_approve = 'Y' AND restaurant_name = ? AND category_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, category_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_name(rs.getString("restaurant_name"));
				dto.setOpening_time(rs.getString("opening_time"));
				dto.setClosing_time(rs.getString("closing_time"));
				
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
	
	// 카테고리 아이디로 음식점 검색
	public List<RestaurantDTO> findByCategoryIdrestaurant(String category_id) {
		List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	        
	        sql = "SELECT restaurant_id, restaurant_name, "
	                + " restaurant_address, restaurant_tel "
	                + " FROM restaurant "
	                + " WHERE restaurant_approve = 'Y' AND category_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, category_id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_id(rs.getString("restaurant_id"));
		        dto.setRestaurant_name(rs.getString("restaurant_name"));
		        dto.setRestaurant_address(rs.getString("restaurant_address"));
		        dto.setRestaurant_tel(rs.getString("restaurant_tel"));
				
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
	
	// 점주 음식점 승인 현황 리스트
	public List<RestaurantDTO> listApprovedRestaurant(String owner_id) throws SQLException {
	    List<RestaurantDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
	    try {
	        
	        sql = "SELECT restaurant_id, restaurant_name, "
	                + " restaurant_address, restaurant_tel, "  
	        		+ " restaurant_approve "
	                + " FROM restaurant "
	                + " WHERE owner_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, owner_id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				RestaurantDTO dto = new RestaurantDTO();
				
				dto.setRestaurant_id(rs.getString("restaurant_id"));
		        dto.setRestaurant_name(rs.getString("restaurant_name"));
		        dto.setRestaurant_address(rs.getString("restaurant_address"));
		        dto.setRestaurant_tel(rs.getString("restaurant_tel"));
		        dto.setRestaurant_approve(rs.getString("restaurant_approve"));
				
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
	
	// 추가
	
	public List<RestaurantDTO> getRestaurantsByOwner(String ownerId) {
	    List<RestaurantDTO> list = new ArrayList<>();
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

	    try {
	    	sql = "SELECT restaurant_id, restaurant_name FROM Restaurant WHERE owner_id = ?";
	    	pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, ownerId);
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	            RestaurantDTO dto = new RestaurantDTO();
	            dto.setRestaurant_id(rs.getString("restaurant_id"));
		        dto.setRestaurant_name(rs.getString("restaurant_name"));
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
	
	public List<RestaurantDTO> getRestaurantsByOwnerAll(String ownerId) {
        List<RestaurantDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            sql = "SELECT restaurant_id, restaurant_name, RESTAURANT_ADDRESS, RESTAURANT_TEL, "
                    + "RESTAURANT_COUNT, OPENING_TIME, CLOSING_TIME, RESTAURANT_APPROVE,"
                    + "OWNER_ID, CATEGORY_ID "
                    + "FROM Restaurant "
                    + "WHERE owner_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ownerId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RestaurantDTO dto = new RestaurantDTO();
                dto.setRestaurant_id(rs.getString("restaurant_id"));
                dto.setRestaurant_name(rs.getString("restaurant_name"));
                dto.setRestaurant_address(rs.getString("restaurant_address"));
                dto.setRestaurant_tel(rs.getString("restaurant_tel"));
                dto.setRestaurant_count(rs.getInt("restaurant_count"));
                dto.setOpening_time(rs.getString("opening_time"));
                dto.setClosing_time(rs.getString("closing_time"));
                dto.setRestaurant_approve(rs.getString("restaurant_approve"));
                dto.setOwner_id(rs.getString("owner_id"));
                dto.setCategory_id(rs.getString("category_id"));
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
	
	
