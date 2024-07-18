package example.datajpa.controller;

import example.datajpa.dto.MemberDTO;
import example.datajpa.entity.Member;
import example.datajpa.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    // web 확장 - 페이징과 정렬
    @GetMapping("members")
    // Pagable 인터페이스의 기본설정을 application.yml(글로벌 설정) 뿐만 아니라 개별적으로 설정할 수 있다.(개별설정 > 글로벌 설정)
    public Page<MemberDTO> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){
        // 엔티티를 바로 반환하는 것은 매우 위험하니, DTO로 변환하여 리턴
        return memberRepository.findAll(pageable)
                .map(MemberDTO::new);   // :: 메서드 레퍼런스
    }


    @PostConstruct
    public void init(){

        // 페이징 테스트를 위해 100명의 유저 insert
        for(int i = 0; i < 100; i++){
            int age = 10;
            memberRepository.save(new Member("member" + i, age + i));
        }
    }
}
