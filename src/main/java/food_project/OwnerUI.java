package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class OwnerUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private OwnerDAO dao = new OwnerDAO();
	private LoginInfo2 loginInfo = null;
	
	public OwnerUI(LoginInfo2 loginInfo) {
		this.loginInfo = loginInfo;
	}

	
	public void ownerManage() {
		int ch = 0;
		
		while(true) {
			System.out.println("\n[점주 메뉴]");
			
			try {
				System.out.print("1.예약조회 2.리뷰조회 3.메뉴확인 4.음식점 상세정보 수정 5.음식점 등록/삭제 요청 6.개인정보수정 7.탈퇴 8.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 8) {
					loginInfo.logout(); System.out.println();
					return;
				}
				
				switch(ch) {
				case 1: reservation(); break;
				case 2: review(); break;
				case 3: menu(); break;
				case 4: updateRestaurant(); break;
				case 5: registrationOrDelete(); break;
				case 6: update(); break;
				case 7: withdraw(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void reservation() {
		System.out.println("\n[예약조회]");
		
	}
	
	public void review() {
		System.out.println("\n[리뷰조회]");
	}
	
	public void menu() {
		System.out.println("\n[메뉴확인]");
	}
	
	public void updateRestaurant() {
		System.out.println("\n[음식점 상세정보 수정]");
	}
	
	public void registrationOrDelete() {
		System.out.println("\n[음식점 등록/삭제 요청]");
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
			// System.out.println(ch);

			if (ch == 'Y' || ch == 'y') {
				dao.deleteOwner(loginInfo.loginOwner().getOwner_id());
                loginInfo.logout();
			}
			
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("탈퇴 실패...");
		}
		System.out.println();
	}
}
