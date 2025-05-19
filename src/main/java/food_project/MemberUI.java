package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


public class MemberUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAO();
	private LoginInfo loginInfo = null;
	private RestaurantUImember restaurantUImember = new RestaurantUImember(loginInfo);
	private ReservationUI reservationUI = new ReservationUI(loginInfo);
	
		public MemberUI(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}
		
	
	public void restaurantList() {
		System.out.println("\n[음식점 리스트 확인]");
		
		try {
			//boolean b = true;
			String ch = "";
			
			while(ch != "8") {
				System.out.print("1.한식 2.일식 3.중식 4.양식 5.분식 6.디저트 7.기타 8.뒤로가기 => ");
				ch = br.readLine().trim();
				
				switch(ch) {
				case "1": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "2": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "3": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "4": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "5": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "6": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "7": restaurantUImember.findByCategoryIdrestaurant(ch); restaurantDetail(ch); break;
				case "8": System.out.println(); return;
				}
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restaurantDetail(String category_id) {
		System.out.println("\n[음식점 상세정보 확인]");
		String ch;
		try {
			
			do {
				System.out.print("1.음식점 상세 정보 검색 2.예약 3.즐겨찾기 등록 4.뒤로가기 => ");
				 ch = br.readLine().trim();
				
				switch(ch) {
				case "1": restaurantUImember.findByrestaurant_details(category_id); break;
				case "2": reservationUI.startReservationMenu(loginInfo.loginMember().getMember_id()); break;
				case "3": favoritesinsert(); break;
				case "4": System.out.println(); return;
				}
				
			} while(ch != "4");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	// 즐겨찾기 등록
	public void favoritesinsert() {
		System.out.println("\n[즐겨찾기 등록]");
		String id;
		try {
			System.out.print("음식점 코드 ? ");
			id = br.readLine();
			
			dao.insertFavorites(loginInfo.loginMember().getMember_id(),id);
			
			if(dao != null) {
				System.out.println("즐겨찾기 등록 완료...");
			} else {
				System.out.println("즐겨찾기 등록 실패...");
			}
			
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("이미 등록된 데이터입니다. 중복된 항목을 확인해주세요.");	
		} catch (NumberFormatException e) {
			System.out.println("잘못입력하셨습니다.");
		} catch (SQLException e) {
			System.out.println(e.toString());
			System.out.println("음식점 코드 잘못 입력하셨습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	// 즐겨찾기리스트확인
	public void favoritesList() {
		System.out.println("\n[즐겨찾기 리스트]");
		int ch;
		while(true) {
			try {
				List<Object> list = dao.FavoritesById(loginInfo.loginMember().getMember_id());
				if (list.size() == 0) {
					System.out.println("등록된 정보가 없습니다.\n");
				} else {
					System.out.println("음식점코드\t음식점명");
					System.out.println("----------------------");
				}
				for(Object obj : list) {
					if(obj instanceof MemberDTO) {
						MemberDTO dto = (MemberDTO)obj; 
						System.out.print(dto.getMember_id() + "\t");
						
					} else if(obj instanceof RestaurantDTO) {
						RestaurantDTO rdto = (RestaurantDTO)obj;
						System.out.print(rdto.getRestaurant_id() + "\t");
						System.out.print(rdto.getRestaurant_name() + "\n");
					}
				}
				System.out.println();
				System.out.print("1.즐겨찾기삭제 2.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
				
				switch(ch) {
				case 1: deleteFavorites(); break;
    			case 2: return;
    			
    			default:
                    System.out.println("잘못된 코드입니다. 다시 선택하세요.");
    			}
				
			} catch (Exception e) {
			}
		}
	}
	// 즐겨찾기 삭제
	public void deleteFavorites() {
		System.out.println("\n[즐겨찾기 삭제]");
		String id;
		try {
			System.out.print("삭제할 음식점 코드 ?");
			id = br.readLine();
			
			dao.deleteFavorites(loginInfo.loginMember().getMember_id(),id);
			
			if(dao != null) {
				System.out.println("즐겨찾기 삭제 완료...\n");
			} else {
				System.out.println("즐겨찾기 삭제 실패...\n");
			}
			
		} catch (NumberFormatException e) {
			System.out.println("잘못입력하셨습니다.");
		} catch (SQLException e) {
			System.out.println(e.toString());
			System.out.println("음식점 코드 잘못 입력하셨습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
		// 개인정보 수정
	public void memberUpdate() { // 추가
		System.out.println("\n[개인정보 수정]");
		try {
			MemberDTO dto = loginInfo.loginMember();
			
			System.out.print("비밀번호 ? ");
			dto.setPwd(br.readLine());

			System.out.print("생년월일 ? ");
			dto.setBirth(br.readLine());

			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());

			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());

			dao.updateMember(dto);

			System.out.println("회원정보가 수정되었습니다.");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { 
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else if(e.getErrorCode()==1840 || e.getErrorCode()==1861) {
				System.out.println("날짜 입력 형식 오류입니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	// 회원탈퇴
	public void memberDelete() { // 추가
		System.out.println("\n[회원탈퇴]");
		char ch;
		
		try {
			System.out.print("회원탈퇴 하시겠습니까?[Y/N]");
			ch = br.readLine().charAt(0);
			
			if(ch == 'y' || ch == 'Y') {
				dao.deleteMember(loginInfo.loginMember().getMember_id());
				System.out.println("탈퇴 완료되었습니다.");
				MainUI ui = new MainUI(); 
				loginInfo.logout();
				ui.menu();
			} else {
				System.out.println("회원탈퇴 취소 혹은 오입력하셨습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}	

}
