package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;




public class MainUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private LoginInfo login = new LoginInfo();
	private LoginInfo2 ologin = new LoginInfo2();
	private GuestUI guestUI = new GuestUI(login);
	private OguestUI oguestUI = new OguestUI(ologin);
	private MemberUI memberUI = new MemberUI(login);
	private OwnerUI ownerUI = new OwnerUI(ologin);
	private AdminUI adminUI = new AdminUI(login);
	private RestaurantUI restaurantOnwerUI = new RestaurantUI(ownerUI);
	
	public void menu() {
		while(true) {
			MemberDTO loginMember = login.loginMember();
	        OwnerDTO loginOwner = ologin.loginOwner();
			
			if(loginMember == null && loginOwner == null) {
				menuGuest();
	        } else if(loginMember != null && loginMember.getMember_id().equals("admin")) {
					menuAdmin();
			} else if(loginMember != null){
				menuMember();
			}else if(loginOwner != null){
				menuOwner();
			}
		}  
				
	}
	
	public void menuGuest() {
		int ch;
		
		do {
			ch = 0;
			try {
				System.out.print("1.로그인 2.회원가입 3.점주로그인 4.점주가입 5.종료 => ");
				ch = Integer.parseInt(br.readLine());
				
			} catch (Exception e) {
			}
		} while (ch < 1 || ch > 5);
				
		if(ch == 5) {
			System.out.println("시스템을 종료합니다.");
			DBConn.close();
			System.exit(0);
		}
		switch(ch) {
		case 1: guestUI.login(); break;
		case 2: guestUI.register(); break;
		case 3: oguestUI.login(); break;
		case 4: oguestUI.register(); break;
		}
			
	}
		
	
	
	public void menuMember() {
		int ch;
		try {
			System.out.println("회원 로그인");
			do {
				System.out.println("1.음식점리스트확인 2.음식점검색 3.즐겨찾기리스트확인");
				System.out.print("4.예약목록확인 5.개인정보 수정 6.회원탈퇴. 7.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch <1 || ch > 7);	
				switch(ch) {
					case 1: memberUI.restaurantList();break;
					case 2: restaurantOnwerUI.menu2();break;
					case 3: memberUI.favoritesList();break;
					case 4: memberUI.reservationList();break;
					case 5: memberUI.memberUpdate();break;
					case 6: memberUI.memberDelete();break;
					case 7: login.logout(); System.out.println(); break;
				}
		} catch (Exception e) {
		}
	}
	
	
	
	public void menuOwner() {
		int ch;
		try {
			System.out.println("점주 로그인");
			do {
				System.out.print("1.예약조회 2.리뷰조회 3.메뉴확인 4.음식점 상세정보 수정 5.음식점 등록/삭제 요청 6.개인정보수정 7.탈퇴 8.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 8);	
				switch(ch) {
				case 1: ownerUI.reservation(); break;
				case 2: restaurantOnwerUI.Allrestaurant(); break;
				case 3: ownerUI.menu(); break;
				case 4: restaurantOnwerUI.updateRestaurant(); break;
				case 5: restaurantOnwerUI.menu(); break;
				case 6: ownerUI.update(); break;
				case 7: ownerUI.withdraw(); break;
				case 8: ologin.logout(); System.out.println();break;
				}
			
		} catch (Exception e) {
		}
	}
	
	public void menuAdmin() {
		int ch;
		try {
			System.out.println("관리자 로그인");
			do {
				System.out.println("1.음식점 등록/삭제 허가 2.음식점 리스트");
				System.out.print("3.회원리스트 4.점주 리스트 5.리뷰관리 6.카테고리 리스트 7.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 7);	
				switch(ch) {
				case 1: adminUI.permission();break;
				case 2: restaurantOnwerUI.Allrestaurant();break;
				case 3: adminUI.memberlist();break;
				case 4: adminUI.ownerlist();break;
				case 5: adminUI.reviewlist();break;
				case 6: adminUI.categorylist();break;
				case 7: login.logout(); System.out.println();break;				
				}
				
		} catch (Exception e) {
		}
	}
}