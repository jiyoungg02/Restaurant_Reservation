package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class OwnerUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private OwnerDAO dao = new OwnerDAO();
	private LoginInfo2 loginInfo = null;
	private ReservationListDAO dao1 = new ReservationListDAO();
	private RestaurantDAO restaurantDAO = new RestaurantDAO();
	
	
	public OwnerUI(LoginInfo2 loginInfo) {
		this.loginInfo  = loginInfo;
	}
	

	
	public void reservation() {
        System.out.println("\n[예약조회]");
        String ownerId = loginInfo.loginOwner().getOwner_id();

        List<RestaurantDTO> restaurants = restaurantDAO.getRestaurantsByOwner(ownerId);

        if (restaurants.isEmpty()) {
            System.out.println("등록된 음식점이 없습니다.");
            return;
        }

        System.out.println("[" + ownerId + "님 보유 음식점 목록]");
        System.out.println("식당 번호\t\t식당 이름");
        System.out.println("-------------------------");
        for (RestaurantDTO r : restaurants) {
            System.out.println(r.getRestaurant_id() + "\t\t" + r.getRestaurant_name());
        }

        try {
            System.out.print("\n조회할 음식점 번호 입력: ");
            int restaurantId = Integer.parseInt(br.readLine());

            while (true) {
                System.out.print("1.사용한 예약 목록  2.예정 중인 예약 목록  3.돌아가기 => ");
                String ch = br.readLine();
                System.out.println();

                String restaurantIdStr = String.valueOf(restaurantId);

                switch (ch) {
                    case "1":
                        printReservationsByUsedStatus(ownerId, restaurantIdStr, "Y");
                        break;
                    case "2":
                        printReservationsByUsedStatus(ownerId, restaurantIdStr, "N");
                        break;
                    case "3":
                        System.out.println("예약조회 메뉴를 종료합니다.");
                        return;
                    default:
                        System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void printReservationsByUsedStatus(String ownerId, String restaurantId, String isUsed) {
	    try {
	        List<ReservationDTO> reservations = dao1.getReservationsByUsedStatus(ownerId, restaurantId, isUsed);

	        String title = isUsed.equals("Y") ? "[사용한 예약 목록]" : "[예정 중인 예약 목록]";
	        System.out.println("\n" + title);

	        if (reservations.isEmpty()) {
	            System.out.println("예약 정보가 없습니다.");
	        } else {
	            reservation_title();
	            for (ReservationDTO dto : reservations) {
	                reservation_print(dto);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("예약 조회 중 오류 발생: " + e.getMessage());
	    }
	}

	
	public void review() {
		System.out.println("\n--리뷰조회--");
		int ch;
		
		owner_ReviewUI rui = new owner_ReviewUI(loginInfo);
		rui.owner_review();
		
		try {
			do {
				System.out.print("1.댓글작성 2.댓글수정 3.댓글삭제 4.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
			} while(ch <1 || ch > 4);	
				switch(ch) {
					case 1: rui.owner_insertReviewComment(); break;
					case 2: rui.owner_updateReviewComment(); break;
					case 3: rui.owner_deleteReviewComment(); break;
					case 4: return;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void menu() {
		System.out.println("\n[메뉴확인]");
		int ch;
		
		MenuUI mui = new MenuUI(loginInfo);
		mui.owner_menu();
		
		try {
			do {
				System.out.print("1.메뉴등록 2.메뉴수정 3.메뉴삭제 4.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
			} while(ch <1 || ch > 4);	
				switch(ch) {
					case 1: mui.owner_insertMenu(); break;
					case 2: mui.owner_updateMenu(); break;
					case 3: mui.deleteMenu(); break;
					case 4: return;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		System.out.println("\n[개인정보 수정]");
		
		try {
			OwnerDTO dto = loginInfo.loginOwner();
			
			System.out.print("패스워드 ? ");
			dto.setOwner_pwd(br.readLine());
			
			System.out.print("생년월일 ? ");
			dto.setOwner_birth(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setOwner_tel(br.readLine());
			
			System.out.print("이메일 ? ");
			dto.setOwner_email(br.readLine());
			
			dao.updateOwner(dto);
			
			System.out.println("개인정보가 수정되었습니다.");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { // UPDATE - NOT NULL 위반
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
	
	public void withdraw() {
		System.out.println("\n[탈퇴]");
		char ch;
		
		try {
			System.out.print("회원을 탈퇴 하시겠습니까[Y/N] ? ");
	        
			ch = (br.readLine().trim()).charAt(0);

			if (ch == 'Y' || ch == 'y') {
				dao.deleteOwner(loginInfo.loginOwner().getOwner_id());
                loginInfo.logout();
                System.out.println("탈퇴되었습니다.\n");
                MainUI ui = new MainUI(); 
				ui.menu();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("탈퇴 실패...");
		}
		System.out.println();
	}
	
	private void reservation_title() {
	    System.out.print("예약번호\t\t\t");
	    System.out.print("예약자이름\t\t");
	    System.out.print("음식점 이름\t");
	    System.out.print("예약날짜\t\t");
	    System.out.print("예약시간\t\t");
	    System.out.println("예약인원\t\t");
	    System.out.println("------------------------------------------------------------------------------");
	}
	    // 예약 내용 출력
	    private void reservation_print(ReservationDTO dto) {
	        System.out.print(dto.getReservation_id() + "\t\t");
	        System.out.print(dto.getMember_name() + "\t\t");
	        System.out.print(dto.getRestaurant_name() + "\t\t");
	        System.out.print(dto.getReservation_date() + "\t");
	        System.out.print(dto.getReservation_time() + "\t\t");
	        System.out.println(dto.getNumber_of_people());
	    }
}
