package food_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class MenuUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MenuDAO dao = new MenuDAO();
	
	private LoginInfo2 loginInfo = null;
	
	public MenuUI(LoginInfo2 loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void owner_menu() {
		String ownerId, restaurant_name;
		System.out.println("\n[메뉴 조회]");
		try {
			ownerId = loginInfo.loginOwner().getOwner_id();
			
			System.out.print("음식점 이름 ? ");
			restaurant_name = br.readLine();
			
			List<MenuDTO> list = dao.owner_listMenu(ownerId, restaurant_name);
			
			boolean found = false;
			for (MenuDTO dto : list) {
			    if (dto.getOwner_id().equals(ownerId) && dto.getRestaurant_name().equals(restaurant_name)) {
			        found = true;
			        break;
			    }
			}
			if(!found) {
				System.out.println("해당 음식점 또는 메뉴가 존재하지 않습니다.\n");
				return;
			}
			
			title_owner_menu();
			
			System.out.println("---------------------");
			
			for(MenuDTO dto : list) {
				System.out.print(dto.getMenu_id() + "\t");
				System.out.print(dto.getMenu_name() + "\t");
				System.out.println(dto.getMenu_price());
			}
		} catch (NullPointerException e) {
			System.out.println("찾으실 음식점을 입력해주세요.\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void owner_insertMenu() {
		System.out.println("\n[메뉴 등록]");
		MenuDTO dto = new  MenuDTO ();
		String ownerId, restaurantName, restaurantId ;
				
		try {
			ownerId = loginInfo.loginOwner().getOwner_id();
			
			System.out.print("레스토랑 이름 ? ");
			restaurantName = br.readLine().trim();
			
			restaurantId = dao.getRestaurantId(restaurantName, ownerId);
			if (restaurantId == null) {
	            System.out.println("등록되지 않은 음식점입니다.\n");
	            return;
	        }
			
			 dto.setOwner_id(ownerId);
	        dto.setRestaurant_name(restaurantName);
	        dto.setRestaurant_id(restaurantId);

	        System.out.println("----------------------------------------------");

	        System.out.print("등록할 메뉴 아이디 ? ");
	        dto.setMenu_id(br.readLine().trim());

	        System.out.print("등록할 메뉴 이름 ? ");
	        dto.setMenu_name(br.readLine());

	        System.out.print("가격 ? ");
	        dto.setMenu_price(Integer.parseInt(br.readLine()));

	        dao.insertMenu(dto);

	        System.out.println("메뉴 등록 성공...\n");
			
//			List<MenuDTO> list = dao.owner_listMenu(ownerId, restaurantName);
//			
//			
//			boolean found = false;		
//			for (MenuDTO m : list) {
//				if (ownerId.equals(m.getOwner_id()) && restaurantName.equals(m.getRestaurant_name())) {
//	                found = true;
//	                break;
//	            }
//	        }
//			
//			if (!dao.restaurantExists(ownerId, restaurantName)) {
//			    System.out.println("등록되지 않은 음식점입니다.\n");
//			    return;
//			}
//			
//
//			dto.setOwner_id(ownerId);
//			dto.setRestaurant_name(restaurantName);
//			
//			System.out.println("----------------------------------------------");
//			
//			System.out.print("등록할 메뉴 아이디 ? ");
//			dto.setMenu_id(br.readLine().trim());
//			
//			System.out.print("등록할 메뉴 이름 ? ");
//			dto.setMenu_name(br.readLine());
//			
//			System.out.print("가격 ? ");
//			dto.setMenu_price(Integer.parseInt(br.readLine()));
//			
//			dao.insertMenu(dto);
//			
//			System.out.println("메뉴 등록 성공...\n");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void owner_updateMenu() {
		System.out.println("\n[메뉴 수정]");
		MenuDTO dto = new  MenuDTO ();
		String ownerId, restaurantName, restaurantId, menuId;
				
		try {
			ownerId = loginInfo.loginOwner().getOwner_id();
			
			System.out.print("레스토랑 이름 ? ");
			restaurantName = br.readLine().trim();
			
			restaurantId = dao.getRestaurantId(restaurantName, ownerId);
			if (restaurantId == null) {
	            System.out.println("등록되지 않은 음식점입니다.\n");
	            return;
	        }
			
			dto.setOwner_id(ownerId);
	        dto.setRestaurant_name(restaurantName);
	        dto.setRestaurant_id(restaurantId);
			
			
			System.out.print("메뉴 아이디 ? ");
			menuId = br.readLine();
			dto.setMenu_id(menuId);
			
			if (!dao.menuExists(dto)) {
			    System.out.println("해당 메뉴가 존재하지 않습니다.\n");
			    return;
			}
			
			System.out.println("----------------------------------------------");
			
			System.out.print("수정할 메뉴 이름 ? ");
			dto.setMenu_name(br.readLine());
			
			System.out.print("수정할 가격 ? ");
			dto.setMenu_price(Integer.parseInt(br.readLine()));
			
			dao.updateMenu(dto, restaurantName, ownerId);
			
			System.out.println("메뉴 수정 성공...\n");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteMenu() {
		System.out.println("\n[메뉴 삭제]");
		MenuDTO dto = new  MenuDTO ();
		String ownerId, restaurantName, menuName;
				
		try {
			ownerId = loginInfo.loginOwner().getOwner_id();
			
			System.out.print("레스토랑 이름 ? ");
			restaurantName = br.readLine();
			
			List<MenuDTO> list = dao.owner_listMenu(ownerId, restaurantName);
			
			if (list == null) {
	            System.out.println("등록되지 않은 음식점입니다.\n");
	            return;
	        }
			
			boolean found = false;		
			for (MenuDTO m : list) {
				if (ownerId.equals(m.getOwner_id()) && restaurantName.equals(m.getRestaurant_name().trim())) {
	                found = true;
	                break;
	            }
	        }
			
			if (!found) {
	            System.out.println("등록되지 않은 음식점입니다.\n");
	            return;
	        }
			
			dto.setOwner_id(ownerId);
			dto.setRestaurant_name(restaurantName);
			
			System.out.println("----------------------------------------------");
			
			System.out.print("삭제할 메뉴이름 ? ");
			menuName = br.readLine();
			dto.setMenu_name(menuName);
			
			int result = dao.deleteMenu(dto);
			
			if(result > 0 ) {
				System.out.println("메뉴 삭제 성공...\n");
			} else {
				System.out.println("등록된 메뉴가 없습니다.\n");
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void member_menu() {
		String restaurant_name;
		System.out.println("\n[메뉴 조회]");
		try {
			System.out.print("음식점 이름 ? ");
			restaurant_name = br.readLine();
			
			List<MenuDTO> list = dao.member_listMenu(restaurant_name);
			
			if (list == null) {
			    System.out.println("찾으실 음식점을 입력해주세요.\n");
			    return;
			} 
			
			boolean found = false;
			for (MenuDTO dto : list) {
				if (dto.getRestaurant_name() != null && dto.getRestaurant_name().equals(restaurant_name)) {
	                found = true;
	                break;
	            }
			}
			if(!found) {
				System.out.println("해당 음식점 또는 메뉴가 존재하지 않습니다.\n");
			    return;
			}
			
			title_member_menu();
			
			System.out.println("---------------------");
			
			for(MenuDTO dto : list) {
				System.out.print(dto.getMenu_name() + "\t");
				System.out.println(dto.getMenu_price());
			}
		} catch (NullPointerException e) {
			System.out.println("다시 입력 해주십시오.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void title_owner_menu() {
		System.out.print("메뉴코드" + "\t");
		System.out.print("메뉴명" + "\t");
		System.out.println("가격");
	}
	
	
	public void title_member_menu() {
		System.out.print("메뉴명" + "\t");
		System.out.println("가격");
	}
	
}
