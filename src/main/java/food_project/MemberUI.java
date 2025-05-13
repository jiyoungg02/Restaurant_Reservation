package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class MemberUI {
	private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAO();
	private LoginInfo loginInfo = null;
	
	public MemberUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}	
	
	public void restaurantlist() {
		
		
	}
	public void restaurantsearch() {
		
		
	}
	public void Favoriteslist() {
		
		
	}
	public void reservationlist() {
		
		
	}
	
	// 개인정보 수정
	public void memberupdate() {
		System.out.println("\n[정보수정]");

		try {
			MemberDTO dto = loginInfo.loginMember();
			
			System.out.print("패스워드 ? ");
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
	// 탈퇴
	public void memberdelete() {
		System.out.println("\n[회원탈퇴]");
		char ch;
		
		try {
			System.out.print("회원탈퇴 하시겠습니까?[Y/N]");
			ch = br.readLine().charAt(0);
			
			if(ch == 'y' || ch == 'Y') {
				dao.deleteMember(loginInfo.loginMember().getMember_id());
				loginInfo.logout();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
}
