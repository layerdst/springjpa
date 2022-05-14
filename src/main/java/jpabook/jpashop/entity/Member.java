package jpabook.jpashop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username", "age"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name) {
        this.username = name;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }

    }

    public Member(String aaa, int i) {
        this.username = aaa;
        this.age = i;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

}
