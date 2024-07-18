package example.datajpa.controller;

import example.datajpa.entity.Member;
import example.datajpa.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberController(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    @GetMapping("members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // id는 pk값이므로 Domain Class Converter라는 기능을 사용 -> '간단한 조회용'으로만 사용
    // 조금 더 복잡해지면 이 기능을 사용하지 못한다. (사용을 그렇게까지 권장하진 않음)
    @GetMapping("members/v2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("MemberA"));
    }

}
