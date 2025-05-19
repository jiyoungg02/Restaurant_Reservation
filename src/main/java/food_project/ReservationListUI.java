package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;


public class ReservationListUI {
	private LoginInfo login = new LoginInfo();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private ReservationListDAO dao = new ReservationListDAO();

	public ReservationListUI(LoginInfo login) {
		this.login = login;
	}
	

	protected void menu() {
		int ch;
		System.out.println("\n[예약 목록 확인]");
		while (true) {
			try {
				System.out.print("1.이용한 예약 목록 2.이용 예정 예약 목록 3.뒤로가기 => ");
				ch = Integer.parseInt(br.readLine());

				if (ch == 3) {
					return;
				}

				switch (ch) {
				case 1:
					reservationlist_use();
					break;
				case 2:
					reservationlist_plan();
					break;
				default:
					System.out.println("잘못된 번호입니다. 다시 선택하세요");
				}
			} catch (NumberFormatException e) {
				System.out.println("원하는 번호를 입력하시오");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void reservationlist_use() {
		System.out.println("\n[이용한 예약 목록]");
		member_ReviewUI mui = new member_ReviewUI(login);
		
		dao.updateExpiredReservations();

		String loginId = login.loginMember().getMember_id();
		List<ReservationDTO> list = dao.getUsedReservations(loginId);

		if (list.isEmpty()) {
			System.out.println("이용한 예약이 없습니다.");
		} else {
			reservation_title();
			for (ReservationDTO dto : list) {
				reservation_print(dto);
			}
		}

		int ch;

		System.out.println("\n[이용한 예약 목록]");
		
		while (true) {
			try {
				System.out.print("1.리뷰작성 2.리뷰 수정 3.리뷰 삭제 4. 뒤로가기 =>");
				ch = Integer.parseInt(br.readLine());

				if (ch == 4) {
					return;
				}

				switch (ch) {
				case 1: mui.insertReview(); break;
				case 2: mui.updateReview(); break;
				case 3: mui.deleteReview(); break;
				default:
					System.out.println("잘못된 번호입니다. 다시 선택하세요");
				}
			} catch (NumberFormatException e) {
				System.out.println("원하는 번호를 입력하시오");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void reservationlist_plan() {
		System.out.println("\n[이용 예정 예약 목록]");

		dao.updateExpiredReservations();

		String loginId = login.loginMember().getMember_id();

		int ch;
		List<ReservationDTO> list = dao.getPlanReservations(loginId);

		if (list.isEmpty()) {
			System.out.println("이용 예정인 예약이 없습니다.");
		} else {
			reservation_title();
			for (ReservationDTO dto : list) {
				reservation_print(dto);
			}
		}

		System.out.println("\n[이용 예정 예약 목록]");

		while (true) {
			try {
				System.out.print("1.예약변경 2.예약취소 3.뒤로가기 =>");
				ch = Integer.parseInt(br.readLine());

				if (ch == 3) {
					return;
				}

				switch (ch) {
				case 1:
					reservationlist_use_update();
					break;
				case 2:
					reservationlist_use_delete();
					break;
				default:
					System.out.println("잘못된 번호입니다. 다시 선택하세요");
				}
			} catch (NumberFormatException e) {
				System.out.print("원하는 번호를 입력하시오 =>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void reservationlist_use_update() {
		System.out.println("\n[예약 변경]");

		String loginId = login.loginMember().getMember_id();

		List<ReservationDTO> list = dao.getPlanReservations(loginId);

		if (list.isEmpty()) {
			System.out.println("이용 예정인 예약이 없습니다.");
		} else {
			reservation_title();
			for (ReservationDTO dto : list) {
				reservation_print(dto);
			}
		}

		try {
			String reservation_id;
			String member_Id = login.loginMember().getMember_id();

			// 예약번호가 유효할 때까지 계속해서 입력 받기
			while (true) {
				System.out.print("변경할 예약코드를 고르시오 (당일예약은 불가능합니다.) => ");
				reservation_id = br.readLine();

				// 예약 번호가 유효한지 확인
				if (!dao.isValidReservation(reservation_id, member_Id)) {
					System.out.println("입력한 예약코드가 존재하지 않습니다. 다시 입력하세요.");
				} else {
					break; // 예약번호가 유효하면 반복문 종료
				}
			}

			java.sql.Date newDate = null;
			boolean validDate = false;
			while (!validDate) {
				System.out.print("새 예약 날짜 입력 (yyyy-MM-dd) => ");
				String dateStr = br.readLine();

				try {
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
					java.util.Date utilDate = sdf.parse(dateStr);
					newDate = new java.sql.Date(utilDate.getTime());

					java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
					if (newDate.before(today)) {
						System.out.println("예약 날짜는 오늘 이후로만 변경 가능합니다.");
						continue;
					}
					validDate = true;
				} catch (java.text.ParseException e) {
					System.out.println("날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식으로 입력해주세요.");
				}
			}

			System.out.print("새 예약 시간 입력 (예: 14:00) => ");
			String newTime = br.readLine();

			System.out.print("새 예약 인원 수 입력 => ");
			String number_of_people = br.readLine();

			dao.updateReservation(reservation_id, newDate, newTime, number_of_people, member_Id);

			System.out.println("예약이 성공적으로 변경되었습니다.");

		} catch (SQLException e) {
			System.out.println("예약 변경 실패: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reservationlist_use_delete() {
		System.out.println("\n[예약 취소]");

		String reservation_id;

		String loginId = login.loginMember().getMember_id();

		List<ReservationDTO> list = dao.getPlanReservations(loginId);

		if (list.isEmpty()) {
			System.out.println("이용 예정인 예약이 없습니다.");
		} else {
			reservation_title();
			for (ReservationDTO dto : list) {
				reservation_print(dto);
			}
		}

		try {
			System.out.print("취소할 예약코드를 고르시오 => ");
			reservation_id = br.readLine();

			dao.deleteReservation(reservation_id);

			System.out.println("예약을 취소했습니다.");

		} catch (SQLException e) {
			if (e.getErrorCode() == 20100) {
				System.out.println("등록된 데이터가 아닙니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	private void reservation_title() {
		System.out.print("예약코드\t\t\t");
		System.out.print("음식점명\t\t");
		System.out.print("예약날짜\t\t");
		System.out.print("예약시간\t\t");
		System.out.println("예약인원");
		System.out.println("------------------------------------------------------------------------------");
	}

	private void reservation_print(ReservationDTO dto) {
		System.out.print(dto.getReservation_id() + "\t\t");
		System.out.print(dto.getRestaurant_name() + "\t\t");
		System.out.print(dto.getReservation_date() + "\t");
		System.out.print(dto.getReservation_time() + "\t\t");
		System.out.println(dto.getNumber_of_people());

	}

}
