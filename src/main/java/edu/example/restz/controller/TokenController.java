package edu.example.restz.controller;

import edu.example.restz.dto.MemberDTO;
import edu.example.restz.security.util.JWTUtil;
import edu.example.restz.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Log4j2
public class TokenController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/make") //POST로 /api/v1/token/make 요청을 처리하는 makeToken 메서드
    public ResponseEntity<Map<String, Object>> makeToken(@RequestBody MemberDTO memberDTO) {  //매개변수로 MemberDTO 객체를 전달받아
        log.info("makeToken() ------- ");

        //사용자 정보 가져오기
        MemberDTO foundMemberDTO = memberService.read(memberDTO.getMid(), memberDTO.getMpw());     //데이터베이스에 해당 객체 정보가 존재하는지 확인한
        log.info("--- foundMemberDTO : " + foundMemberDTO);     //데이터를 넘겨받아서 로그 확인

        //토큰 생성
        Map<String, Object> payloadMap = foundMemberDTO.getPayload();
        String accessToken = jwtUtil.createToken(payloadMap, 6000);   //60분 유효
        String refreshToken = jwtUtil.createToken(Map.of("mid", foundMemberDTO.getMid()),
                                                         60 * 24 * 7);     //7일 유효

        log.info("--- accessToken : " + accessToken);
        log.info("--- refreshToken : " + refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken,
                                        "refreshToken", refreshToken));
    }//END makeToken()

    //상태 코드 400과 메시지 전송
    public ResponseEntity<Map<String, String>> sendResponse(String message) {
        return new ResponseEntity<>(Map.of("error", message),
                                    HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/refreshVerify")  //리프레시 토큰 검증 처리
    public ResponseEntity<Map<String, String>> refreshVerify(
                            @RequestHeader("Authorization") String headerAuth,
                            @RequestParam("refreshToken") String refreshToken,
                            @RequestParam("mid") String mid){

        //1.파라미터 값 확인 - 값이 없으면 메시지를 전달하여 400 BAD_REQUEST 반환
        if(headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            return sendResponse("액세스 토큰이 없습니다.");
        }

        if( refreshToken.isEmpty() )    return  sendResponse("리프레시 토큰이 없습니다.");
        if( mid.isEmpty() )             return  sendResponse("아이디가 없습니다.");

        try { //2.액세스 토큰 유효성 검증
            String accessToken = headerAuth.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("--- 1.액세스 토큰 유효");

            //전달받은 데이터 그대로 반환
            return ResponseEntity.ok(Map.of("accessToken", accessToken,
                                            "refreshToken", refreshToken,
                                            "mid", mid));
        } catch(ExpiredJwtException e) {
            log.info("--- 2.액세스 토큰 만료기간 경과");

            try { //3.리프레시 토큰 유효성 검증
                Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
                log.info("--- 3.리프레시 토큰 유효");

                if( !claims.get("mid").equals(mid) ) {  //mid가 일치하지 않는 경우
                    return sendResponse("INVALID REFRESH TOKEN mid");
                }

                log.info("--- 4.새로운 토큰 생성 ");
                MemberDTO foundMemberDTO = memberService.read(mid);
                Map<String, Object> payloadMap = foundMemberDTO.getPayload();
                String newAccessToken = jwtUtil.createToken(payloadMap, 1); //1분 유효
                String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid),
                                                             3);  //3분 유효
                //신규 생성 토큰들과 mid 반환
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken,
                                                "refreshToken", newRefreshToken,
                                                "mid", mid));
            } catch(ExpiredJwtException ee) {
                log.info("--- 5.리프레시 토큰 만료기간 경과");
                return  sendResponse("리프레시 토큰 만료기간 경과" + ee.getMessage());
            }
        } catch(Exception e){
            log.info("--- 리프레시 토큰 처리 기타 예외");
            return sendResponse("리프레시 토큰 처리 예외 : " + e.getMessage());
        }
    }
}























