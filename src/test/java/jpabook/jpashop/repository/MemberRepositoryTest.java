package jpabook.jpashop.repository;

import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.entity.Member;
import jpabook.jpashop.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("user1");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void fincByUsernameAngAgeGreaterThan() throws Exception{


        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaa", 29));

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(29);
        assertThat(result.size()).isEqualTo(1);


        //given

        //when

        //then
    }

    @Test
    public void testQuery() throws Exception{


        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaa", 29));

        List<Member> result = memberRepository.findUser("aaa", 10);
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(10);
        assertThat(result.size()).isEqualTo(1);


        //given

        //when

        //then
    }
    
    
    @Test
    public void findUsernameList() throws Exception{

        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("bbb", 29));
        //given
        memberRepository.findUsernameList();
        List<String> usernameList = memberRepository.findUsernameList();
        for(String s : usernameList){
            System.out.println(" s = " + s);
        }


        //when
        
        //then
    }

    @Test
    public void dtoFind() throws Exception{
        Team team = new Team("a");
        teamRepository.save(team);


        Member member = new Member("aaa", 10);
        member.setTeam(team);
        memberRepository.save(member);


        memberRepository.save(new Member("bbb", 29));
        //given
        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for(MemberDto dto : memberDto){
            System.out.println(" s = " + dto);
        }


        //when

        //then
    }

    @Test
    public void findUsernames() throws Exception{

        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("bbb", 29));
        //given
        List<Member> byNames = memberRepository.findByNames(Arrays.asList("aaa", "bbb"));

        for(Member m : byNames){
            System.out.println(" s = " + m);
        }


        //when

        //then
    }

    @Test
    public void returnType(){
        Member member = new Member("aaa", 10);
        Member member1 = new Member("nnn", 20);

        memberRepository.save(member);
        memberRepository.save(member1);

        Member aaa = memberRepository.findMemberByUsername("aaa");

        System.out.println("findMeber = " + aaa);
    }


    @Test
    public void memberList() throws Exception{
        Member member = new Member("aaa", 10);
        Member member1 = new Member("aaa", 10);
        memberRepository.save(member);
        memberRepository.save(member1);
        List<Member> aaa = memberRepository.findByUsernameAndAgeEquals("aaa", 10);
        for(Member t : aaa){
            System.out.println(t);
        }

        //when

        //then
    }

    @Test
    public void paging() throws Exception{
        //given
        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaaa", 21));
        memberRepository.save(new Member("asdaa", 12));
        memberRepository.save(new Member("aaag", 23));
        memberRepository.save(new Member("aava", 10));
        memberRepository.save(new Member("abaa", 10));
        memberRepository.save(new Member("aabba", 10));
        memberRepository.save(new Member("aaba", 30));

        int age = 10;
//        int offset = 0;
//        int limit = 3;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> dto = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        List<Member> content = page.getContent();

        long totalElements = page.getTotalElements();

        for(Member member : content){
            System.out.println(member);
          }
        System.out.println(totalElements);

        // Slice 로 페이지당 게시물을 수를 제한하여 가져올수 있다.
//        Slice<Member> sliceContent = memberRepository.findByAge(age, pageRequest);

//

        //when

        //then
    }


    @Test
    public void bulkUpdate() throws Exception{
        //given
        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaaa", 21));
        memberRepository.save(new Member("asdaa", 12));
        memberRepository.save(new Member("aaag", 23));
        memberRepository.save(new Member("aava", 10));
        memberRepository.save(new Member("abaa", 10));
        memberRepository.save(new Member("aabba", 10));
        memberRepository.save(new Member("aaba", 30));
        //when
        int result = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear();

        //then
        assertThat(result).isEqualTo(3);
    }


}