package edu.example.restz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_member")
@EntityListeners(value = { AuditingEntityListener.class })
public class Member {       //엔티티 객체 insert/update 시 자동으로 시간 갱신
    @Id
    private String mid;
    private String mpw;
    private String mname;
    private String email;
    private String role;

    @CreatedDate            //등록 일시 자동 저장
    private LocalDateTime joinDate;

    @LastModifiedDate       //수정 일시 자동 저장
    private LocalDateTime modifiedDate;

    public void changePassword(String mpw) {
        this.mpw = mpw;
    }

    public void changeName(String mname) {
        this.mname = mname;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeRole(String role) {
        this.role = role;
    }
}
