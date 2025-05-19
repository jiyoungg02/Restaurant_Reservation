package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class RestaurantUImember {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private RestaurantDAO rdao = new RestaurantDAO();
	private LoginInfo loginInfo = null;
	
	public RestaurantUImember(LoginInfo login) {
		this.loginInfo = login;
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
	
	
	
	
	public void findByName() {
		System.out.println("\n[음식점명 검색]");
		String name;
		
		try {
			System.out.print("검색할 음식점명 ? ");
			name = br.readLine();
			
			List<Object> list = rdao.RestaurantName(name);
			
		
			if(list.size() == 0) {
				System.out.println("등록된 음식점이 없습니다.");
				return;
			}
			title2();
			for(Object obj : list) {
				if(obj instanceof RestaurantDTO) {
					RestaurantDTO dto = (RestaurantDTO)obj; 
					System.out.print(dto.getRestaurant_name() + "\t");        
					System.out.print(dto.getRestaurant_address() + "\t");     
					System.out.print(dto.getRestaurant_tel() + "\t");         
					System.out.print(dto.getRestaurant_count() + "\t");      
					System.out.print(dto.getOpening_time() + "\t");           
					System.out.print(dto.getClosing_time() + "\t"); 
					
				} else if(obj instanceof CategoryDTO) {
					CategoryDTO cdto = (CategoryDTO)obj;
					System.out.print(cdto.getCategory_name() + "\t");
					
				}
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
			
			List<Object> list = rdao.RestaurantAddress(address);
			
			if(list.size() == 0) {
				System.out.println("음식점 주소가 없습니다.");
				return;
			}
			
			title2();
			for(Object obj : list) {
				if(obj instanceof RestaurantDTO) {
					RestaurantDTO dto = (RestaurantDTO)obj; 
					System.out.print(dto.getRestaurant_name() + "\t");        
					System.out.print(dto.getRestaurant_address() + "\t");     
					System.out.print(dto.getRestaurant_tel() + "\t");         
					System.out.print(dto.getRestaurant_count() + "\t");      
					System.out.print(dto.getOpening_time() + "\t");           
					System.out.print(dto.getClosing_time() + "\t"); 
					
				} else if(obj instanceof CategoryDTO) {
					CategoryDTO cdto = (CategoryDTO)obj;
					System.out.print(cdto.getCategory_name() + "\t");
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	public void Allrestaurant() {
		System.out.println("\n[음식점 전체 리스트]");
		
		List<RestaurantDTO> list = rdao.restaurantlist();
		
		title();
			
		for(RestaurantDTO dto : list) {
			print(dto);
		}
		System.out.println();
		
	}
	
	
	public void findByCategoryIdrestaurant(String category_id) {
		System.out.println("\n[" + Category(category_id) + "음식점 리스트]");
		
		List<RestaurantDTO> list = rdao.findByCategoryIdrestaurant(category_id);
		
		System.out.println("음식점코드\t음식점명\t위치\t전화번호");
		for(RestaurantDTO dto : list) {
			System.out.print(dto.getRestaurant_id() + "\t");
			System.out.print(dto.getRestaurant_name() + "\t");
			System.out.print(dto.getRestaurant_address() + "\t");
			System.out.print(dto.getRestaurant_tel() + "\n");
		}
		System.out.println();
		
	}
	
	public String Category(String category_id) {
		String s = null;
		switch(category_id) {
		case "1" : s = "한식 ";break;
		case "2" : s = "일식 ";break;
		case "3" : s = "중식 ";break;
		case "4" : s = "양식 ";break;
		case "5" : s = "분식 ";break;
		case "6" : s = "디저트 ";break;
		case "7" : s = "기타 ";break;
	}
	
		return s;
	}
	
	public void findByrestaurant_details(String category_id) {
		System.out.println("\n[음식점 상세 정보 검색]");
		member_ReviewUI mrui = new member_ReviewUI(loginInfo);
		MenuUI mui = new MenuUI(null);
		
		try {
			System.out.print("1.오픈/마감시간 2.메뉴/가격 3.리뷰 4.뒤로가기 => ");
			String ch = br.readLine().trim();
			
			switch(ch) {
			case "1": searchOpenClose(category_id); break;
			case "2": mui.member_menu(); break;
			case "3": mrui.member_restaurantResearch(); break;
			case "4": System.out.println(); return;
			}
			
		} catch (Exception e) {
		}
	}
	
	public void searchOpenClose(String category_id) {
		System.out.println("\n[음식점 오픈/마감시간 검색]");
		
		
		try {
			System.out.print("오픈/마감 시간 검색할 음식점명 ? ");
			String name = br.readLine();
			
			List<RestaurantDTO> list = rdao.RestaurantDetails(name, category_id);
			System.out.println("음식점명\t오픈시간\t마감시간");
			System.out.println("-------------------------");
			for(RestaurantDTO dto : list) {
				System.out.print(dto.getRestaurant_name() + "\t");
				System.out.print(dto.getOpening_time() + "\t");
				System.out.print(dto.getClosing_time() + "\n");
			}
			System.out.println();
			
		} catch (Exception e) {
		}
	}
	
	public void print(RestaurantDTO dto) {
		System.out.print(dto.getRestaurant_id() + "\t");
		System.out.print(dto.getRestaurant_name() + "\t");
		System.out.print(dto.getRestaurant_address() + "\t");
		System.out.print(dto.getRestaurant_tel() + "\t");
		System.out.print(dto.getRestaurant_count() + "\t");
		System.out.print(dto.getOpening_time() + "\t");
		System.out.print(dto.getClosing_time() + "\t");
		System.out.print(dto.getRestaurant_approve() + "\t");
		System.out.print(dto.getOwner_id() + "\t");
		System.out.println(dto.getCategory_id() + "\t");
	}
	
	public void print2(RestaurantDTO dto) {
		System.out.print(dto.getRestaurant_name() + "\t");
		System.out.print(dto.getRestaurant_address() + "\t");
		System.out.print(dto.getRestaurant_tel() + "\t");
		System.out.print(dto.getRestaurant_count() + "\t");
		System.out.print(dto.getOpening_time() + "\t");
		System.out.print(dto.getClosing_time() + "\t");
		System.out.println(dto.getCategory_id() + "\t");
	}
	
	
	public void title() {
		System.out.print("음식점코드\t");
		System.out.print("음식점명\t");
		System.out.print("지역\t");
		System.out.print("전화번호\t\t");		
		System.out.print("수용인원\t");		
		System.out.print("오픈시간\t");		
		System.out.print("마감시간\t");
		System.out.print("승인여부\t");
		System.out.print("점주아이디\t");
		System.out.println("카테고리코드");	
		System.out.println("----------------------------------------------------------------------------------------");
	}
	
	public void title2() {
		System.out.print("음식점명\t");
		System.out.print("지역\t");
		System.out.print("전화번호\t\t");		
		System.out.print("수용인원\t");		
		System.out.print("오픈시간\t");		
		System.out.print("마감시간\t");
		System.out.println("카테고리명");	
		System.out.println("----------------------------------------------------------------");
	}
	
}
