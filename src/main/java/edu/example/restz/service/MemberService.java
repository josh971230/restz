package edu.example.restz.service;

import edu.example.restz.dto.MemberDTO;
import edu.example.restz.entity.Member;
import edu.example.restz.exception.MemberException;
import edu.example.restz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO read(String mid, String mpw) {     //mid와 mpw를 매개변수로 넘겨받아 ----------------------------
        Optional<Member> foundMember =  memberRepository.findById(mid);

        //데이터베이스에 존재하지 않는 경우 - MemberTaskException의 BAD_CREDENTIALS 예외를 발생시키고
        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);

        if( !passwordEncoder.matches(mpw, member.getMpw()) ) { //mpw가 데이터베이스의 값과 일치하지 않는 경우
            throw MemberException.BAD_CREDENTIALS.get();       //MemberTaskException의 BAD_CREDENTIALS 예외를 던지고
        }
        //그렇지 않으면 넘겨받은 엔티티를 DTO 객체로 반환
        return new MemberDTO(member);
    }

    public MemberDTO read(String mid) {
        Optional<Member> foundMember =  memberRepository.findById(mid);
        Member member = foundMember.orElseThrow(MemberException.BAD_CREDENTIALS::get);
        return new MemberDTO(member);
    }
}
