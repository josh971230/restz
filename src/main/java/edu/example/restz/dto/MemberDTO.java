package edu.example.restz.dto;

import edu.example.restz.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String mid;
    private String mpw;
    private String mname;
    private String email;
    private String role;
    private LocalDateTime joinDate;
    private LocalDateTime modifiedDate;

    public MemberDTO(Member member) {
        this.mid = member.getMid();
        this.mpw = member.getMpw();
        this.mname = member.getMname();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.joinDate = member.getJoinDate();
        this.modifiedDate = member.getModifiedDate();
    }

    //JWT 문자열의 내용 반환
    public Map<String, Object> getPayload() {
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("mid", mid);
        payloadMap.put("mname", mname);
        payloadMap.put("eamil", email);
        payloadMap.put("role", role);
        return payloadMap;
    }
}
