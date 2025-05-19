package food_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class member_ReviewUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private ReviewDAO dao = new ReviewDAO();
	private LoginInfo loginInfo = null;
		
		public member_ReviewUI(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}


	public void insertReview() {
		System.out.println("\n[리뷰 작성]");
		ReviewDTO dto = new ReviewDTO();
		ReservationListDAO rdao = new ReservationListDAO();
		String memberId, reservationid;
				
		try {
			memberId = loginInfo.loginMember().getMember_id();
			
			System.out.print("예약코드 ? ");
			reservationid = br.readLine();
			
			List<ReservationDTO> gurv = rdao.getUsedReservations(memberId);
			boolean found = false;
			for (ReservationDTO res : gurv) {
			    if (res.getReservation_id().equals(reservationid)) {
			        found = true;
			        break;
			    }
			}
			if (!found) {
			    System.out.println("유효하지 않은 예약코드입니다.");
			   return;
			}
			
			List<ReviewDTO> rfbi = dao.reservation_findById(reservationid);
			boolean alreadyReviewed = false;
			for (ReviewDTO rvs : rfbi) {
			    if (rvs.getReview_etccomment() != null) {
			        alreadyReviewed = true;
			        break;
			    }
			}

			if (alreadyReviewed) {
			    System.out.println("이미 등록된 리뷰입니다.\n");
			    return;
			}
			
			dto.setMember_id(memberId);
			dto.setReservation_id(reservationid);
			
			System.out.print("별점 ? ");
			dto.setReview_rating(Integer.parseInt(br.readLine()));
			
			System.out.println("리뷰를 작성해주세요");
			dto.setReview_etccomment(br.readLine());
			
			dao.insertReview(dto);
			
			System.out.println("리뷰 등록 성공...\n");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateReview() {
		System.out.println("\n[리뷰 수정]");
		ReviewDTO dto = new ReviewDTO();
		ReservationListDAO rdao = new ReservationListDAO();
		String memberId, reservationid;
				
		try {
			memberId = loginInfo.loginMember().getMember_id();
			
			System.out.print("예약코드 ? ");
			reservationid = br.readLine();
			
			List<ReservationDTO> ursv = rdao.getUsedReservations(memberId);
			boolean found = false;
			for (ReservationDTO res : ursv) {
			    if (res.getReservation_id().equals(reservationid)) {
			        found = true;
			        break;
			    }
			}
			if (!found) {
			    System.out.println("유효하지 않은 예약코드입니다.\n");
			   return;
			}
			
			dto.setMember_id(memberId);
	        dto.setReservation_id(reservationid);
			
	        System.out.println("별점 ? ");
	        dto.setReview_rating(Integer.parseInt(br.readLine()));
	        
			System.out.println("작성할 내용을 입력해주세요.");
			dto.setReview_etccomment(br.readLine());
			
			dao.updateReview(dto);
			
			System.out.println("수정이 완료 되었습니다.\n");
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteReview() {
		System.out.println("\n[리뷰 삭제]");
		String reservateionId;
		String memberId;
		
		try {
			memberId = loginInfo.loginMember().getMember_id();
			
			System.out.print("예약코드 ? ");
			reservateionId = br.readLine();
			
			int result = dao.deleteReview(reservateionId, memberId);
			
			if(result > 0) {
				System.out.println("리뷰를 삭제 했습니다.");
			} else {
				System.out.println("등록된 리뷰가 아닙니다.");
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public void admin_deleteReview() {
		System.out.println("\n[리뷰 삭제]");
		String reservationId;
		String memberId;
		
		try {
			System.out.print("리뷰 작성한 회원아이디 ? ");
			memberId = br.readLine();
			
			System.out.print("예약코드 ? ");
			reservationId = br.readLine();
			
			int result = dao.deleteReview(reservationId, memberId);
			
			if(result > 0) {
				System.out.println("리뷰를 삭제 했습니다.");
			} else {
				System.out.println("등록된 리뷰가 아닙니다.");
			}
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
// ------------------ 관리자 음식점 검색
	
	public void admin_restaurantResearch() {
        System.out.println("\n[음식점 리뷰]");
        String name;
        
        try {
            System.out.print("검색할 음식점명 ? ");
            name = br.readLine();
            
            if(name == null || name.trim().isEmpty()) {
                System.out.println("음식점명을 입력해주세요.\n");
                return;
            } 
            
            List<ReviewDTO> list = dao.admin_listReview(name);
            boolean found = false;
            for (ReviewDTO dto : list) {
                if (dto.getRestaurant_name().equals(name)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                System.out.println("해당 음식점의 리뷰가 존재하지 않습니다.\n");
                return;
            }
            
            title_admin_restaurantResearch();
            
            for(ReviewDTO dto : list) {
                System.out.print(dto.getMember_id() + "\t");
                System.out.print(dto.getReservation_id() + "\t");
                System.out.print(dto.getRestaurant_name() + "\t");
                System.out.print(dto.getReview_rating() + "\t");
                System.out.print((dto.getReview_etccomment() != null && !"null".equals(dto.getReview_etccomment()) ? dto.getReview_etccomment() : "") + "\t\t");
                System.out.println((dto.getReview_comment() != null && !"null".equals(dto.getReview_comment()) ? dto.getReview_comment() : ""));
            }    
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
	
// ------------------ 회원 음식점 검색
	public void member_restaurantResearch() {
        System.out.println("\n[음식점 리뷰]");
        String name;
        
        try {
            System.out.print("검색할 음식점명 ? ");
            name = br.readLine();
            
            if(name == null) {
                System.out.println("음식점명을 입력해주세요.\n");
                return;
            } 
            
            List<ReviewDTO> list = dao.admin_listReview(name);
            boolean found = false;
            for (ReviewDTO dto : list) {
                if (dto.getRestaurant_name().equals(name)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                System.out.println("해당 음식점 또는 리뷰가 존재하지 않습니다.\n");
                return;
                
            }
            
            title_member_restaurantResearch();
            
            System.out.println("------------------------------------------------------------------------");
            
            for(ReviewDTO dto : list) {
                System.out.print(dto.getRestaurant_name() + "\t");
                System.out.print(dto.getReview_rating() + "\t");
                System.out.print((dto.getReview_etccomment() != null && !"null".equals(dto.getReview_etccomment()) ? dto.getReview_etccomment() : "") + "\t\t");
                System.out.println((dto.getReview_comment() != null && !"null".equals(dto.getReview_comment()) ? dto.getReview_comment() : ""));
            }    
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
	
	public void title_member_restaurantResearch() {
		System.out.print("음식점명" + "\t");
		System.out.print("별점" + "\t");
		System.out.print("기타코멘트" + "\t\t\t\t\t");
		System.out.println("리뷰댓글");
	}
	
	public void title_admin_restaurantResearch() {
		System.out.print("회원아이디" + "\t");
		System.out.print("예약코드" + "\t\t");
		System.out.print("음식점명" + "\t");
		System.out.print("별점" + "\t");
		System.out.print("기타코멘트" + "\t\t\t\t\t");
		System.out.println("리뷰댓글");
	}
	
}
