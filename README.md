## SPRING JPA(MYSQL ver)
   - 프로젝트 생성 : 스프링 부트 스타터(start.spring.io)
   - 프로젝트 라이브러리 
        - spring boot starter web, 
        - spring boot starter jpa,
        - mysql : mysql connector java
        - lombok
       ```
        server:
          port: ##포트번호
        
        spring:
          datasource:
            url: jdbc:mysql://127.0.0.1:3306/##디비스키마?characterEncoding=UTF-8&serverTimezone=UTC&autoReconnection=true # 변경해주세요
            username: ##아이디
            password:  ##비밀번호 
            driver-class-name: com.mysql.cj.jdbc.Driver # mysql 8버전
            # driver-class-name: com.mysql.jdbc.Driver # mysql 5버전
        
          jpa:
            hibernate:
              ddl-auto: create
            properties:
              hibernate:
                default_batch_fetch_size : ## fetch 조인시 한번에 불러올 수 있는 IN 쿼리 수 100~1000사이  
            open-in-view: false
        #        show_sql: true
        #        format_sql: true
        
        
        # 페이지 목록 설정에 관한 내용을 yml 에서 관리할수 있다.
          data:
            web:
              pageable:
                default-page-size: 20
                max-page-size: 2000
        
        logging:
          level:
            org.hibernate.SQL : debug
        #    org.hibernate.type : trace
     ```
    
## JPA -> SPRING JPA
- Spring JPA는 JPA를 사용하면서 공통적으로 많이 사용되는 메서드를 상속받아 사용할 수 있는 인터페이스이다.
- **SAVE**
    ```
    *JPA*
    public Team save(Team team){
        em.persist(team);
        return team;
    }
  
    
    *SPRING JPA*
    
    public interface ... name ...  extends JpaRepository<Team, Long>  
  
    public Team save(Team team);  // 구현하지 않아도 됨
  
  ```


- **FIND**
    ```
    *JPA*
    public Team findById(Long id){
        Team team = em.find(Team.class, id)
        return Optional.ofNullable(team)
    }
  
    public List<Team> findAll(){
        return em.createQuery("select t from Team t, Team.class).getResultList();
    }
  
    public List<Team> findByName(String name){
        return em.createQuery("select t from Team t where t.name = :name" , Team.class)
                .setParameter("name", name)
                .getResultList();
    }
  
    public List<Team> findByIdAndName(Long id, String name){
        return em.createQuery("select t from Team t where t.id = :id and t.name = :name", Team.class)
            .setParameter("id", id)
            .setParameter("name", name)
            .getResultList();
    }
    
    *SPRING JPA*

  
    public interface ... name ...  extends JpaRepository<Team, Long>  
    
    // findBy ... [Distinct][Property] [And, Or, Ignore] [Property] OrderBy [Property] [ASC, DESC]
    위와 같이 조합이 가능하다.    
  
    public Team findById(Long id); // 구현하지 않아도 됨
    public Team findByName(String name)
    public Team findByIdAndName(String name, Long id)
  
## Named Qeury 사용하기
- **엔티티에 NamedQuery 정리하기**
    ```
  @Entity
  @NamedQuery(
    name = "Member.findUsername",
    query = "select m from Member where m.username = :username") 
  )
  public class Member{
    ....
  }
  
  ```
- **JPA 에서 NamedQuery 호출하기** 
    ```   
    public class MemberRepository{
        public List<Member> findByUsername(String name){
        
        List<Member> resultList =
            em.createNamedQuery("Member.findUsername", Member.class)
            .setParameter("username", name)
            .getResultList();
      
    }
    ```

- **Spring JPA NamedQuery 사용**
    ```
    @Query(name="Member.findByUsername")
    List<Member> findByUsername(@Param("username") String name);
  ```
  
- **Spring JPA NamedQuery 를 메서드 @Query 로 대체하기**
    ```
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
  ``` 
- **Spring JPA Entity 대신 Dto 로 조회하기**
    ```
    @Query("select new package.package..Dto(m.id, m.username... )) from Member m join m.team t")
    List<MemberDto> findMemberDto();
    ```
  
## Parameter Binding
Parameter 바인딩은 위치(?) 와 이름기반(:) 으로 나뉘어진다. 주로 이름기반을 사용하는게 좋다
또한 Spring JPA 에서는 in 절 쿼리내 삽입가능한 컬랙션 타입도 지원이 가능하다
    
    
    @Query("select m from Member m where m.username in :names" )
    List<Member> findByNames(@Param("names") List<String> names);
    
## 반환 타입
**스프링 데이터 JPA는 유연한 반환 타입 지원**
    
    List<Member> findByUsername(String name);
    Member findUsername(Sting name);
    Optional<Member> findByUsername(String name);
    
**조회 결과**
    - 컬렉션 : 결과가 없으면 빈컬렉션 반환
    - 단건 : 결과없을시 null, 결과가 2건 이상 : NonUniqueResultExeption 발생
    
    

## 페이징과 정렬
**JPA** 
    
    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .getResultList();
   
    }
    
    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age=:age", Long.class)
                .setParameter("age", age)
                .getSigleResult();
  
    } 
    
**Spring JPA**
    
    public interface MemberRepository extends JpaRepository<Member, Long> {
        // count 쿼리 사용
        Page<Member> findByAge(int age, Pageable pageable);
        
        // count 쿼리 사용안함
        Slice<Member> findByAge(int age, Pageable pageable);
        
        // count 쿼리 사용안함
        List<Member> findByAge(int age, Pageable pageable);
        Page<Member> findByAge(int age, Sort sort);
    }
    
    @Service
    public class MemberService{
        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
    } 

**Spring JPA - CountQuery 추가 사용하기**  
    
    @Query(value = "select m from Member m", countQuery="select count(m.username) from Member m")
    Page<Member> findMemberAllcountCountBy(Pageable pageable); 
    
## 벌크성 수정 쿼리
한번에 대량의 DB를 업데이트 할때 사용하는 방법

**JPA**
    
    int result = em.createQeury("update Member m set m.age = m.age +1 where m.age>= :age")
            .setParameter("age", age)
            .executeUpdate();
   
**Spring JPA**

    @Modifying
    @Query("update Member m set m.age = m.age +1 where m.age >= :age" )
    int bulkAgePlus(@Param("age")int age);

## Fetch Join 조회 : Entity Graph 조회
**JPQL 페치조인**

    @Query("select m from Member m left join fetch m.team" )
    List<Member> findMemberFetchJoin();
    
    //Override 
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    
    //JPQL + 엔티티그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();
    
    //기본 메서드 + 엔티티그래프
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username)
    
## 인터페이스 커스텀
특정 기능을 별도로 만들고 싶을때, 인터페이스를 만들어 상속하게 한다.
interface -> class 상속
    
    publc interface ...interfaceName...{
        List<Member> findMemberCustom();
    }
    
    @RequiredArgsConstructor
    public class MemberRepositoryImpl implements ...interfaceName... {
        @Override
        List<Member> findMemberCustom(){
            return em.createQuery("select m from Member m").getResultList();
        } 
    }
    
    public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
        
    }

## 
    
    
    



- JPA FIND 
- JPA FIND 
- JPA FIND 
- JPA FIND 
- JPA FIND 

 
   