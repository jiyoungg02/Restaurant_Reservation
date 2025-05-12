package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OwnerUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private OwnerDAO dao = new OwnerDAO();
	
	public void ownerManage() {
		int ch = 0;
		
		while(true) {
			System.out.println("\n[점주 메뉴]");
			
			try {
				System.out.print("1.예약조회 2.리뷰조회 3.메뉴확인 4.음식점 상세정보 수정 5.음식점 등록/삭제 요청 6.개인정보수정 7.탈퇴 8.로그아웃");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 8) return;
				
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
		System.out.println("\n--예약조회--");
		
	}
	
	public void review() {
		System.out.println("\n--리뷰조회--");
	}
	
	public void menu() {
		System.out.println("\n--메뉴확인--");
	}
	
	public void updateRestaurant() {
		System.out.println("\n--음식점 상세정보 수정--");
	}
	
	public void registrationOrDelete() {
		System.out.println("\n--음식점 등록/삭제 요청--");
	}
	
	public void update() {
		System.out.println("\n--개인정보 수정--");
	}
	
	public void withdraw() {
		System.out.println("\n--탈퇴--");
	}
}
