package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CategoryUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private CategoryDAO dao = new CategoryDAO();
	
	
	public void insert() {
		System.out.println("\n[카테고리 추가]");
		
		try {
			CategoryDTO dto = new CategoryDTO();
			
			System.out.print("카테고리 아이디 ? ");
			dto.setCategory_id(br.readLine());
			
			System.out.print("카테고리 이름 ? ");
			dto.setCategory_name(br.readLine());
			
			dao.insertCategory(dto);
			
			System.out.println("카테고리가 추가되었습니다.");
			
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, NOT NULL등의 제약조건 위반에 의한 예외 발생-무결성 제약 조건 위반
			if(e.getErrorCode()==1) {
				System.out.println("아이디 중복입니다.");
			} else if(e.getErrorCode()==1400){ // INSERT-NOT NULL 위반
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void update() {
		System.out.println("\n[카테고리 수정]");
		String id;
		
		try {
			System.out.print("수정할 카테고리 아이디 ? ");
			id = br.readLine();
			
			CategoryDTO dto = dao.findById(id);
			
			if(dto == null) {
				System.out.println("존재하지 않는 아이디입니다.\n");
				return;
			}
			
			System.out.print("카테고리 이름 ? ");
			dto.setCategory_name(br.readLine());
			
			dao.updateCategory(dto);
			
			System.out.println("카테고리가 수정되었습니다.");
			
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { // UPDATE - NOT NULL 위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");	
			}else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void delete() {
		System.out.println("\n[카테고리 삭제]");
		String id;
		char ch;
		
		try {
			System.out.print("삭제할 카테고리 아이디 ? ");
			id = br.readLine();
			
			CategoryDTO dto = dao.findById(id);
			
			if(dto == null) {
				System.out.println("존재하지 않는 아이디입니다.\n");
				return;
			}
			
			System.out.print("카테고리를 삭제 하시겠습니까[Y/N] ? (주의 ! 해당 카테고리 번호를 가지고 있는 음식점 모두 삭제됩니다.) ");
			ch = (br.readLine().trim()).charAt(0);
			
			if (ch == 'Y' || ch == 'y') {
				dao.deleteCategory(id);
				System.out.println("카테고리가 삭제되었습니다.");
			} else {
				System.out.println("카테고리 삭제 취소...");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
}
