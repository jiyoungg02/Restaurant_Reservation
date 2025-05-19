package food_project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;

public class ReservationUI {
	private LoginInfo loginInfo = null; 
   private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   private static ReservationDAO dao = new ReservationDAO();
	
		public ReservationUI(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}

   public void startReservationMenu(String id) {
	    try {
	        String memberId = id;
	        if (memberId == null) {
	            System.out.println("로그인 정보가 없습니다. 먼저 로그인 해주세요.");
	            return;
	        }

	        String restaurantId = null;
	        boolean validRestaurantId = false;

	        while (!validRestaurantId) {
	            System.out.print("\n예약할 음식점코드를 입력해주세요 (당일 예약은 불가능합니다): ");
	            restaurantId = br.readLine();

	            validRestaurantId = dao.isRestaurantIdValid(restaurantId);

	            if (!validRestaurantId) {
	                System.out.println("유효하지 않은 음식점코드입니다. 다시 입력해주세요.");
	            }
	        }

	        System.out.print("예약 날짜 입력 (yyyy-MM-dd): ");
	        Date reservationDate = null;
	        while (reservationDate == null) {
	            try {
	                String dateInput = br.readLine();
	                Date inputDate = Date.valueOf(dateInput);
	                
	                Date today = new Date(System.currentTimeMillis());
	                if (inputDate.before(today)) {
	                    System.out.print("예약 불가한 날짜입니다 날짜를 다시 입력해주세요(yyyy-MM-dd): ");
	                    continue;
	                }
	                reservationDate = Date.valueOf(dateInput);
	            } catch (IllegalArgumentException e) {
	                System.out.print("잘못 입력하였습니다. 다시 입력해주세요 (yyyy-MM-dd): ");
	            }
	        }

	        int capacity = dao.getRestaurantCapacity(restaurantId);

	        System.out.println("예약 시간대를 선택하세요:");
	        for (int i = 9; i <= 22; i++) {
	            String timeSlot = String.format("%02d:00", i);
	            int reserved = dao.getTotalReservedPeople(restaurantId, reservationDate, timeSlot);
	            int available = capacity - reserved;

	            if (available <= 0) {
	                System.out.println(FontColor.RED + timeSlot + " - 예약 불가 (정원 초과)" + FontColor.RESET);
	            } else {
	                System.out.println(timeSlot + " - 예약 가능 인원: " + available + "명");
	            }
	        }

	        System.out.print("예약 시간 입력 (예: 13:00): ");
            String reservationTime = null;
            while (reservationTime == null) {
                try {
                    String timeInput = br.readLine();
                    String[] parts = timeInput.split(":");

                    if (parts.length != 2) throw new IllegalArgumentException();

                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);

                    if (hour < 9 || hour > 22 || minute != 0) {
                        throw new IllegalArgumentException();
                    }

                    reservationTime = String.format("%02d:00", hour);
                } catch (Exception e) {
                    System.out.print("잘못 입력했습니다. 다시 입력해주세요 (예: 13:00): ");
                }
            }

	        int totalReserved = dao.getTotalReservedPeople(restaurantId, reservationDate, reservationTime);

	        System.out.println("현재 예약된 인원: " + totalReserved + "명 / 최대 수용 인원: " + capacity + "명");

	        if (totalReserved >= capacity) {
	            System.out.println("해당 시간대는 예약이 마감되었습니다.");
	            return;
	        }

	        System.out.print("예약할 인원 수 입력: ");
	        int numberOfPeople = Integer.parseInt(br.readLine());

	        if (totalReserved + numberOfPeople > capacity) {
	            System.out.println("수용 인원을 초과했습니다. 예약이 불가능합니다.");
	            return;
	        }
	        
	        // 예약 ID
	        String reservationId = restaurantId + memberId + System.currentTimeMillis();

	        ReservationDTO dto = new ReservationDTO();
	        dto.setReservation_id(reservationId);
	        dto.setRestaurant_id(restaurantId);
	        dto.setReservation_date(reservationDate);
	        dto.setReservation_time(reservationTime);
	        dto.setIs_available("Y");
	        dto.setNumber_of_people(numberOfPeople);
	        dto.setIs_used("N");
	        dto.setMember_id(memberId);

	        dao.insertReservation(dto);

	        System.out.println("예약이 성공적으로 완료되었습니다!\n");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	class FontColor {
	    public static final String RED = "\u001B[31m";
	    public static final String RESET = "\u001B[0m";
	}
}