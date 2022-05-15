package jpabook.jpashop.repository;

import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.entity.Member;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    List<Member> findByUsernameAndAgeEquals(String username, int age);

    @Query("select m.username from Member m ")
    List<String> findUsernameList();

    @Query("select new jpabook.jpashop.dto.MemberDto (m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


    /**
     *
     * Parameter IN 절도 지원한다.
     *
     */
    @Query("select m from Member m where m.username in  :names ")
    List<Member> findByNames(@Param("names") List<String> name);

    /**
     *
     * List 는 null 일 경우 0으로 조회가 된다.
     *
     */
    List<Member> findListByUsername(String name); //컬렉션


    /**
     *
     * 단건이 조회가 안될 경우 null로 표기 되므로, optional로 받을 수 있다.
     *
     */
    Member findMemberByUsername(String name); //단건

    /**
     *
     * optional 로 반환할 수 있게 제공한다.
     *
     */
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    /**
     *
     * Paging 시에는 카운트 쿼리와 일반 쿼리를 분리하는게 좋다.
     *
     */
    @Query(value = "select m from Member m left join m.team t",
            countQuery =  "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

//    Slice<Member> findByAge(int age, Pageable pageable);


    /**
     *
     * @param excuteUpate 를 실행시키기 위해서는 modifying 이라는 어노테이션을 입력해야한다.
     * 그렇지 않을시 DB에는 수정이 되나, 영속성 컨텍스트에 반영이 되지 않아 에러를 발생하게 한디ㅏ
     * clearAutomatically 옵션은 EntityManager 의 flush 와 clear를 할수 있게한다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);





}
