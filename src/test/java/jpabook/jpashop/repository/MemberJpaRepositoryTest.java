package jpabook.jpashop.repository;

import jpabook.jpashop.entity.Member;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;


    @Test
    public void testMember() throws Exception{
        //given
        Member member = new Member("memberA");
        //when

        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }


    @Test
    public void fincByUsernameAngAgeGreaterThen() throws Exception{


        memberJpaRepository.save(new Member("aaa", 10));
        memberJpaRepository.save(new Member("aaa", 29));

        List<Member> result = memberJpaRepository.findByUsernameandAndAgeGreaterThen("aaa", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(29);
        Assertions.assertThat(result.size()).isEqualTo(1);


        //given

        //when

        //then
    }
}