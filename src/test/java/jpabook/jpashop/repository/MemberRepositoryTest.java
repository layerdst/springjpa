package jpabook.jpashop.repository;

import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.entity.Member;
import jpabook.jpashop.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember(){
        Member member = new Member("user1");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void fincByUsernameAngAgeGreaterThan() throws Exception{


        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaa", 29));

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(29);
        Assertions.assertThat(result.size()).isEqualTo(1);


        //given

        //when

        //then
    }

    @Test
    public void testQuery() throws Exception{


        memberRepository.save(new Member("aaa", 10));
        memberRepository.save(new Member("aaa", 29));

        List<Member> result = memberRepository.findUser("aaa", 10);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(10);
        Assertions.assertThat(result.size()).isEqualTo(1);


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


}