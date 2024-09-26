package edu.example.restz.repository;

import edu.example.restz.entity.Member;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.MemberException;
import edu.example.restz.exception.MemberTaskException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert(){   //테스트 데이터 100개 등록
        //user1  ~ user80  role을 USER로, user81 ~ user100   "    ADMIN으로 지정
        IntStream.rangeClosed(1, 100).forEach(i -> {
            //GIVEN - Member 엔티티 객체 생성
            Member member = Member.builder().mid("user" + i)
                                            .mpw( passwordEncoder.encode("1111") )
                                            .mname("USER" + i)
                                            .email("user" + i + "@aaa.com")
                                            .role( i <= 80 ? "USER" : "ADMIN")
                                            .build();

            Member savedMember = memberRepository.save(member); //WHEN - 엔티티 저장

            assertNotNull(savedMember);     //THEN - savedTodo가 널이 아닐 것
            if(i <= 80) assertEquals(savedMember.getRole(), "USER"); //user1 ~ user80은 USER role
            else        assertEquals(savedMember.getRole(), "ADMIN"); //user81 ~ user100은 ADMIN role과 같을 것
        });
    }

    @Test   //SELECT 테스트
    public void testFindById(){
        //given      //@Id 타입의 값으로 엔티티 조회
        String mid = "user1";

        //when
        Optional<Member> foundMember = memberRepository.findById(mid);

        //THEN - foundMember가 널이 아니고 mid는 user1일 것
        assertNotNull(foundMember);
        assertEquals(mid, foundMember.get().getMid());

        log.info("foundMember : " + foundMember);
        log.info("mid : " + foundMember.get().getMid());

        ////////////////////////////////////////////////////////
        try {
            mid = "user111111";
            foundMember = memberRepository.findById(mid);
            foundMember.orElseThrow(MemberException.NOT_FOUND::get);
        } catch(MemberTaskException e) {
            assertEquals(404, e.getCode());
            log.info("e : " + e);
        }
    }

    @Test   //UPDATE 테스트 - 트랜잭션 O
    @Transactional
    @Commit
    public void testUpdateTransactional(){  //회원 수정 테스트
        String mid = "user1";

        //user1 사용자를 데이터베이스에서 조회하여
        Optional<Member> foundMember = memberRepository.findById(mid);

        //조회 결과가 없으면 MemberTaskException으로 NOT_FOUND 예외를 발생시키고
        foundMember.orElseThrow(MemberException.NOT_FOUND::get);

        foundMember.get().changeEmail("bbb@bbb.com");   //email은 bbb@bbb.com으로
        foundMember.get().changePassword( passwordEncoder.encode("2222"));       //mpw는 2222로 변경하고

        assertEquals("bbb@bbb.com", foundMember.get().getEmail());//변경된 이메일이 bbb@bbb.com과 일치하는지 확인
    }

    @Test   //DELETE 테스트 - 트랜잭션 O
    @Transactional
    @Commit
    public void testDelete() {
        String mid = "user100";
        memberRepository.deleteById(mid);

        Optional<Member> foundMember = memberRepository.findById(mid);
        assertTrue( foundMember.isEmpty() );
    }
}











