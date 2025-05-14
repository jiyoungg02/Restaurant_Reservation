package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class RestaurantUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private RestaurantDAO rdao = new RestaurantDAO();
	private LoginInfo login;
	private OwnerUI ownerui; 
	
	public RestaurantUI(OwnerUI ownerui) {
		this.ownerui = ownerui;
	}
	
	public void menu() {
		int ch;
		
		while(true) {
			try {
				System.out.print("1.등록 요청 2.삭제 요청 3.뒤로가기 => ");
				ch = Integer.parseInt(br.readLine());
				
				
				switch(ch) {
				case 1: insertRestaurant(); break;
				case 2: deleteRestaurant(); break;
				case 3: System.out.println(); return;
				}
				
			} catch (Exception e) {
			}
		}
	}
	
	public void menu2() {
		int ch;
		
		while(true) {
			try {
				System.out.print("1.이름 검색 2.주소 검색 3.뒤로가기 => ");
				ch = Integer.parseInt(br.readLine());
				
				
				switch(ch) {
				case 1: findByName(); break;
				case 2: findByAddress(); break;
				case 3: System.out.println(); return;
				}
				
			} catch (Exception e) {
			}
		}
	}
	
	
	
	
	
	public void insertRestaurant() {
		System.out.println("\n[음식점 등록 요청]");
		
		RestaurantDTO dto = new RestaurantDTO();
		
		try {
			System.out.print("음식점 코드 ? ");
			dto.setRestaurant_id(br.readLine());
			
			System.out.print("음식점 이름 ? ");
			dto.setRestaurant_name(br.readLine());
			
			System.out.print("음식점 주소 ? ");
			dto.setRestaurant_address(br.readLine());
			
			System.out.print("음식점 번호 ? ");
			dto.setRestaurant_tel(br.readLine());
			
			System.out.print("음식점 수용 인원수 ? ");
			dto.setRestaurant_count(Integer.parseInt(br.readLine()));
			
			System.out.print("음식점 오픈 시간 ? ");
			dto.setOpening_time(br.readLine());
			
			System.out.print("음식점 마감 시간 ? ");
			dto.setClosing_time(br.readLine());
			
			dto.setRestaurant_approve("N");
			
			OwnerDTO owner = ownerui.getLoginInfo().loginOwner();
			if(owner == null) {
				System.out.println("점주 로그인을 하세요");
				return;
			}
			dto.setOwner_id(owner.getOwner_id());
			
			System.out.print("카테고리 코드 ? ");
			dto.setCategory_id(br.readLine());
			
			rdao.insertRestaurant(dto);
			
			System.out.println("음식점 등록 요청이 완료되었습니다. 관리자 승인을 기다리세요.");
			
						
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	public void updateRestaurant() {
		System.out.println("\n[음식점 상세 정보 수정]");
		
		RestaurantDTO dto = new RestaurantDTO();
		
		try {
			OwnerDTO owner = ownerui.getLoginInfo().loginOwner();
			if(owner == null) {
				System.out.println("점주 로그인을 하세요");
				return;
			}
			
			System.out.print("수정할 음식점 코드 ? ");
			dto.setRestaurant_id(br.readLine());
			
			System.out.print("새로운 음식점 이름 ? ");
			dto.setRestaurant_name(br.readLine());
			
			System.out.print("새로운 음식점 주소 ? ");
			dto.setRestaurant_address(br.readLine());
			
			System.out.print("새로운 음식점 번호 ? ");
			dto.setRestaurant_tel(br.readLine());
			
			System.out.print("새로운 음식점 수용 인원수 ? ");
			dto.setRestaurant_count(Integer.parseInt(br.readLine()));
			
			System.out.print("새로운 음식점 오픈 시간 ? ");
			dto.setOpening_time(br.readLine());
			
			System.out.print("새로운 음식점 마감 시간 ? ");
			dto.setClosing_time(br.readLine());
			
			System.out.print("새로운 음식점 카테고리 코드 ? ");
			dto.setCategory_id(br.readLine());
						
			
			rdao.updateRestaurant(dto);
			
		
			System.out.println("수정이 완료 되었습니다.");
			
			
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	public void deleteRestaurant() {
		System.out.println("\n[음식점 삭제 요청]");
		String id;
		
		try {
			System.out.print("삭제 요청할 음식점 ID: ");
	        id = br.readLine();
			
			rdao.requestDeleteRestaurant(id);
						
			System.out.println("음식점 삭제 요청이 완료되었습니다. 관리자 승인을 기다리세요.");
			
						
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
	
	
	
	public void findByName() {
		System.out.println("\n[음식점 이름 검색]");
		String name;
		
		try {
			System.out.print("검색할 음식점 이름 ? ");
			name = br.readLine();
			
			List<RestaurantDTO> list = rdao.RestaurantName(name);
			
			if(list.size() == 0) {
				System.out.println("등록된 음식점이 없습니다.");
				return;
			}
			
			for(RestaurantDTO dto : list) {
				print(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
	public void findByAddress() {
		System.out.println("\n[음식점 주소 검색");
		
		String address;
		
		try {
			System.out.print("검색할 음식점 주소 ? ");
			address = br.readLine();
			
			List<RestaurantDTO> list = rdao.RestaurantAddress(address);
			
			if(list.size() == 0) {
				System.out.println("음식점 주소가 없습니다.");
				return;
			}
			
			for(RestaurantDTO dto : list) {
				print(dto);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void Allrestaurant() {
		System.out.println("\n[음식점 전체 검색]");
		
		List<RestaurantDTO> list = rdao.restaurantlist();
		
		
		
		for(RestaurantDTO dto : list) {
			System.out.print(dto.getRestaurant_name() + "\t");
			System.out.print(dto.getRestaurant_address() + "\t");
			System.out.print(dto.getRestaurant_tel() + "\t");
			System.out.print(dto.getRestaurant_count() + "\t");
			System.out.print(dto.getOpening_time() + "\t");
			System.out.print(dto.getClosing_time() + "\t");
			System.out.println(dto.getCategory_id() + "\t");
		}
		System.out.println();
		
	}
	
	public void findByrestaurant_details() {
		System.out.println("\n[음식점 상세 정보 검색]");
		
	}
	
	public void print(RestaurantDTO dto) {
		System.out.print(dto.getRestaurant_name() + "\t");
		System.out.print(dto.getRestaurant_address() + "\t");
		System.out.print(dto.getRestaurant_tel() + "\t");
		System.out.print(dto.getRestaurant_count() + "\t");
		System.out.print(dto.getOpening_time() + "\t");
		System.out.print(dto.getClosing_time() + "\t");
		System.out.println(dto.getCategory_id() + "\t");
	}
		
	
}
