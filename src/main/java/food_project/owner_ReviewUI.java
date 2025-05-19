package food_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class owner_ReviewUI {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private ReviewDAO dao = new ReviewDAO();
	private LoginInfo2 loginInfo = null;
		
	public owner_ReviewUI(LoginInfo2 loginInfo) {
		this.loginInfo = loginInfo;
	}
	
    public void owner_review() {
        String owner_id;
        System.out.println("\n[리뷰 조회]");
        try {
            owner_id = loginInfo.loginOwner().getOwner_id();
            
            List<ReviewDTO> list = dao.owner_listReview(owner_id);
            if(list.size() == 0) {
                System.out.println("등록된 정보가 없습니다.\n");
                return;
            }
            
            title_owner_review();
            
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

	
// ------------------ 댓글 정리
    public void owner_insertReviewComment() {
        System.out.println("\n[댓글 작성]");
        ReviewDTO dto = new ReviewDTO();
        String ownerId, reservationId, memberId, comment;
                
        try {
            ownerId = loginInfo.loginOwner().getOwner_id();
            
            System.out.print("회원아이디 ? ");
            memberId = br.readLine();
            
            System.out.print("예약코드 ? ");
            reservationId = br.readLine();
            
            if(memberId == null ||  reservationId == null) {
                System.out.println("아이디와 예약코드를 모두 입력해주세요.");
                return;
            }
            
            dto.setMember_id(memberId);
            dto.setReservation_id(reservationId);
            
            List<ReviewDTO> olr = dao.owner_listReview(ownerId);
            boolean found = false;

            for (ReviewDTO rev : olr) {
                if (reservationId.equals(rev.getReservation_id()) && memberId.equals(rev.getMember_id())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("유효하지 않은 리뷰입니다.\n");
                return;
            }
            
            System.out.println("리뷰에 대한 댓글을 달아주세요.");
            comment = br.readLine();
            
            if(comment == null) {
                dto.setReview_comment(null);
            }
            dto.setReview_comment(comment);
            
            dao.insertReview_comment(dto);
            
            System.out.println("댓글 등록 성공...\n");
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public void owner_updateReviewComment() {
        System.out.println("\n[댓글 수정]");
        ReviewDTO dto = new ReviewDTO();
        ReservationDAO rdao = new ReservationDAO();
        String ownerId, reservationId, comment;
                
        try {
            ownerId = loginInfo.loginOwner().getOwner_id();
            
            System.out.print("예약코드 ? ");
            reservationId = br.readLine();
            
            
            List<ReviewDTO> olr = dao.owner_listReview(ownerId);
            boolean found = false;
            
            for (ReviewDTO rev : olr) {
                if (reservationId.equals(rev.getReservation_id())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("유효하지 않은 예약입니다.\n");
                return;
            }
            
            dto.setReservation_id(reservationId);
            
            System.out.println("리뷰에 대한 댓글을 달아주세요.");
            comment = br.readLine();
            
            if(comment == null) {
                dto.setReview_comment(null);
            }
            dto.setReview_comment(comment);
            
            dao.updateReview_comment(dto);
            
            System.out.println("댓글 수정 성공...");
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
	
    public void owner_deleteReviewComment() {
        System.out.println("\n[댓글 삭제]");
        String reservationId, memberId;
        
        try {
            System.out.print("삭제할 댓글 예약코드 ? ");
            reservationId = br.readLine();
            
            System.out.print("삭제할 댓글 회원아이디 ? ");
            memberId = br.readLine();
            
            int result = dao.deleteReview_comment(reservationId, memberId);
            
            if(result > 0) {
                System.out.println("댓글을 삭제 했습니다.");
            } else {
                System.out.println("등록된 데이터가 아닙니다.");
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
	
	public void title_owner_review() {
		System.out.print("회원아이디" + "\t");
		System.out.print("예약코드" + "\t\t");
		System.out.print("음식점명" + "\t");
		System.out.print("별점" + "\t");
		System.out.print("기타코멘트" + "\t\t\t\t\t");
		System.out.println("리뷰댓글");
	}

}
