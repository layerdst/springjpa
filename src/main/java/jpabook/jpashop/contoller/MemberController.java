package jpabook.jpashop.contoller;


import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.entity.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members/{id}/new")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }


    /**
     * pageable을 사용하면 ?page=1 과 같이 요청할시에 페이지당 컨텐츠 목록을 불러 올수 있다.
     * ?sort=id , ?sort=username 같이 id나 사용자 이름 순으로 정렬하여 불러 얼수 있다.
     *  sort 의 기본정렬은 asc 이고, desc 로 바꾸려고 한다면 id.desc, username.desc 로 표기한다
     * page 당 불러올 수 있는 컨텐츠수와 같은 글로벌 설정은 application.yml 에서 가능하며
     * 부분적인 설정은 아래의  @PageDefault 설정으로 가능하다.
     *
     *
     * pageable 은 한계가 존재한다.
     * page 의 순서는 0부터 시작하며, 0페이지와 1페이지는 같다!
     *  -> 이부분을 고려해서 사용하려면, 1페이지를 0으로 설정하는게 좋다.
     * pagealbe 에서의 numbers 속성은 해당 페이지 속성이 아니라 0 페이지 (1페이지) 기준의 속성이므로
     * 값이 변하지 않는다.
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5) Pageable pageable) {
        Page<Member> all = memberRepository.findAll(pageable);
        Page<MemberDto> mem = all.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return mem;
    }

    @PostConstruct
    public void init(){
        for(int i=0; i<100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
