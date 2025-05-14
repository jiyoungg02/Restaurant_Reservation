package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;;

public class AdminUI {
	private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO memberdao = new MemberDAO();
	private RestaurantDAO restaurantdao = new RestaurantDAO();
	private ReviewDAO reviewdao = new ReviewDAO();
	private LoginInfo loginInfo = null;
	private OwnerDAO ownerdao = new OwnerDAO();
	private CategoryDAO categorydao = new CategoryDAO();
	private CategoryUI categoryUI = new CategoryUI();
	private RestaurantDAO rdao = new RestaurantDAO();
	
	public AdminUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public void permission() {
		int ch;
		System.out.println("\n[음식점 허가]");
		
		try {
			do {
				System.out.print("1.음식점등록 2.음식점삭제 3.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
			} while(ch <1 || ch >3);
			
			switch(ch) {
			case 1: restaurantAdd(); break;
			case 2: restaurantDelete(); break;
			case 3: System.out.println();; return;
			}
		} catch (NumberFormatException e) {
			System.out.println("원하는 번호를 입력하시오");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restaurantAdd() {
		System.out.println("\n[음식점 등록 요청 승인]");
		String id, approve;
		
		try {
			List<RestaurantDTO> list = rdao.RestaurantRequests("N");
			for(RestaurantDTO dto : list) {
				System.out.println("음식점 코드 : " + dto.getRestaurant_id()+ " | " + " 음식점 이름 : " +dto.getRestaurant_name() + " | " 
										+ " 허가 여부 : "  + dto.getRestaurant_approve());
			}
			
			System.out.print("음식점 코드 ? ");
			id = br.readLine();
			
			System.out.print("승인(Y) / 거부(N) ? ");
			approve = br.readLine();
			
			rdao.apporveRestaurant(id, approve);
			if(approve.equalsIgnoreCase("Y")) {
				System.out.println("등록 요청이 승인되었습니다.");
			} else {
				System.out.println("등록 요청이 거부되었습니다.");
			}
								
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void restaurantDelete() {
        System.out.println("\n[음식점 삭제 요청 승인]");
        String id, approve;
		
		try {
			List<RestaurantDTO> list = rdao.RestaurantRequests("D");
			for(RestaurantDTO dto : list) {
				System.out.println("음식점 코드 : " + dto.getRestaurant_id()+ " | " + " 음식점 이름 " +dto.getRestaurant_name() + " | " 
										+ " 허가 여부 "  + dto.getRestaurant_approve());
			}

        System.out.print("삭제 요청을 승인/거부할 음식점 ID: ");
        id = br.readLine();

        System.out.print("삭제 요청을 승인(Y) / 거부(N): ");
        approve = br.readLine();

        
            if (approve.equalsIgnoreCase("Y")) {
            	int result = rdao.deleteRestaurant(id);
            	if(result > 0) {
            		System.out.println("음식점 삭제가 완료되었습니다.");
            	} else {
            		System.out.println("등록된 음식점이 아닙니다.");
            	}              
            } else {
                System.out.println("삭제 요청이 거부되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		System.out.println();
    }
	
	public void reservationlist() {
		System.out.println("\n[음식점 리스트 확인]");
		
	}

	public void memberlist() {
		System.out.println("\n[회원 리스트]");
		
		
	}
	
	public void ownerlist() {
		System.out.println("\n[점주 리스트]");
		
		List<OwnerDTO> list = ownerdao.listOwner();
		
		System.out.println("아이디\t이름\t생년월일\t\t전화번호\t\t이메일");
		System.out.println("---------------------------------------------------------------");
		for(OwnerDTO dto : list) {
			System.out.print(dto.getOwner_id() + "\t");
			System.out.print(dto.getOwner_name() + "\t");
			System.out.print(dto.getOwner_birth() + "\t");
			System.out.print(dto.getOwner_tel() + "\t");
			System.out.print(dto.getOwner_email() + "\n");
		}
		System.out.println();
	}
	
	public void categorylist() {
		System.out.println("\n[카테고리 리스트]");
		
		List<CategoryDTO> list = categorydao.listCategory();
		
		System.out.println("카테고리 아이디\t카테고리 이름");
		System.out.println("----------------------------");
		for(CategoryDTO dto : list) {
			System.out.print(dto.getCategory_id() + "\t\t");
			System.out.print(dto.getCategory_name() + "\n");
		}
		System.out.println();
		
		try {
			int ch;
			
			do {
				System.out.print("1.카테고리 추가 2.카테고리 수정 3.카테고리 삭제 4.뒤로가기 => ");
				ch = Integer.parseInt(br.readLine());
					
				switch(ch) {
				case 1: categoryUI.insert();; break;
				case 2: categoryUI.update(); break;
				case 3: categoryUI.delete(); break;
				case 4: System.out.println(); return;
				}
			} while(ch != 4);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reviewlist() {
		int ch;
		System.out.println("\n[리뷰 관리]");
		
		try {
			do {
				System.out.print("1.음식점검색 2.리뷰삭제 3.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
			} while(ch <1 || ch >3);
			
			switch(ch) {
			case 1: restaurantResearch(); break;
			case 2: reviewDelete(); break;
			case 3: System.out.println();; return;
			}
		} catch (NumberFormatException e) {
			System.out.println("원하는 번호를 입력하시오");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restaurantResearch() {
		
	}
	
	public void reviewDelete() {
		
	}
	
	
}

