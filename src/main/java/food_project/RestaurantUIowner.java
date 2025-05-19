package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class RestaurantUIowner {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private RestaurantDAO rdao = new RestaurantDAO();
	private LoginInfo2 loginInfo = null;
	private CategoryDAO cdao = new CategoryDAO();
	
	public RestaurantUIowner(LoginInfo2 ologin) {
		this.loginInfo = ologin;
	}
		
	public void menu() {
	int ch;
	
	while(true) {
		try {
			
			listApprovedRestaurant();
			
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

public void listApprovedRestaurant() {
	System.out.println("\n[음식점 등록 승인 여부 현황]");
	String id = loginInfo.loginOwner().getOwner_id();
	
	try {
		
		List<RestaurantDTO> list = rdao.listApprovedRestaurant(id);
		
		System.out.print("음식점코드\t음식점명\t음식점주소\t음식점번호\t승인여부\n");
		System.out.println("---------------------------------------------------");
		
		
		for(RestaurantDTO dto : list) {
			System.out.print(dto.getRestaurant_id() + "\t");
			System.out.print(dto.getRestaurant_name() + "\t");
			System.out.print(dto.getRestaurant_address() + "\t");
			System.out.print(dto.getRestaurant_tel() + "\t");
			System.out.print(dto.getRestaurant_approve() + "\n");
		
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

public void insertRestaurant() {
	System.out.println("\n[음식점 등록 요청]");
	
	RestaurantDTO dto = new RestaurantDTO();
	
	try {
		System.out.print("음식점코드 ? ");
		dto.setRestaurant_id(br.readLine());
		
		System.out.print("음식점명 ? ");
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
		
		
		OwnerDTO owner = loginInfo.loginOwner(); 
		if (owner == null) {
		    System.out.println("점주 로그인을 하세요\n");
		    return;
		}
		dto.setOwner_id(owner.getOwner_id());
		
		
		List<CategoryDTO> list = cdao.listCategory();
		System.out.println("카테고리 코드\t카테고리명");
		System.out.println("----------------------------");
		for(CategoryDTO cdto : list) {
			System.out.print(cdto.getCategory_id() + "\t\t");
			System.out.print(cdto.getCategory_name() + "\n");
		}
		System.out.println();
		
		System.out.print("카테고리 코드 ? ");
		dto.setCategory_id(br.readLine());
		
		rdao.insertRestaurant(dto);
		
		System.out.println("음식점 등록 요청이 완료되었습니다. 관리자 승인을 기다리세요.\n");
		
					
	} catch (SQLException e) {
		System.out.println(e.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	System.out.println();
}

	
public void updateRestaurant() {
        System.out.println("\n[음식점 상세 정보 수정]");
        
        RestaurantDTO dto = new RestaurantDTO();

        String ownerId = loginInfo.loginOwner().getOwner_id();

        List<RestaurantDTO> restaurants = rdao.getRestaurantsByOwnerAll(ownerId);

        if (restaurants.isEmpty()) {
            System.out.println("등록된 음식점이 없습니다.");
            return;
        }

        System.out.println("[" + ownerId + "님 보유 음식점 목록]");
        System.out.println("음식점코드\t\t음식점명\t주소\t전화번호\t\t수용가능인원\t오픈시간\t마감시간\t허가여부\t점주아이디\t카테고리코드");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (RestaurantDTO r : restaurants) {
            System.out.print(r.getRestaurant_id() + "\t\t");
            System.out.print(r.getRestaurant_name() + "\t");
            System.out.print(r.getRestaurant_address() + "\t");
            System.out.print(r.getRestaurant_tel() + "\t");
            System.out.print(r.getRestaurant_count() + "\t\t");
            System.out.print(r.getOpening_time() + "\t");
            System.out.print(r.getClosing_time() + "\t");
            System.out.print(r.getRestaurant_approve() + "\t");
            System.out.print(r.getOwner_id() + "\t");
            System.out.println(r.getCategory_id() + "\t");
        }
        
        try {  
			System.out.print("수정할 음식점 코드 ? ");
			dto.setRestaurant_id(br.readLine());
			
			// 해당 음식점 정보 가져오기
	        dto = rdao.findByIdrestaurant(dto.getRestaurant_id());
	        if (dto == null) {
	            System.out.println("해당 음식점이 존재하지 않습니다.\n");
	            return;
	        }
	        
	        // 음식점에 소유자가 설정되어 있는지 확인
	        if (dto.getOwner_id() == null) {
	            System.out.println("이 음식점에는 소유자가 지정되어 있지 않습니다.\n");
	            return;
	        }
	        
	        
	        // 로그인된 점주의 정보 확인
	        OwnerDTO owner = loginInfo.loginOwner(); 
	        if (owner == null) {
	            System.out.println("점주 로그인을 하세요.");
	            return;
	        }
	        if (!dto.getOwner_id().equals(owner.getOwner_id())) {
	            System.out.println("다른 점주의 음식점 정보는 수정할 수 없습니다.\n");
	            return;
	        }
	        
			
			System.out.print("새로운 음식점명 ? ");
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
		} catch (NumberFormatException e) {
			System.out.println("수용인원은 숫자만 입력 가능합니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
	
	public void deleteRestaurant() {
		System.out.println("\n[음식점 삭제 요청]");
		String id;
		
		try {
			OwnerDTO owner = loginInfo.loginOwner(); // 로그인한 점주 정보 확인
			if (owner == null) {
			    System.out.println("점주 로그인을 하세요.");
			    return;
			}

			// 삭제 요청할 음식점 ID 입력
			System.out.print("삭제 요청할 음식점 코드 : ");
			id = br.readLine();

			// 해당 음식점 정보 확인
			RestaurantDTO dto = rdao.findByIdrestaurant(id);
			if (dto == null) {
			    System.out.println("해당 음식점이 존재하지 않습니다.");
			    return;
			}

			if (!dto.getOwner_id().equals(owner.getOwner_id())) {
			    System.out.println("다른 점주의 음식점은 삭제 요청할 수 없습니다.\n");
			    return;
			}

			rdao.requestDeleteRestaurant(id);
			System.out.println("음식점 삭제 요청이 완료되었습니다. 관리자 승인을 기다리세요.\n");
			
						
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
		
	
}