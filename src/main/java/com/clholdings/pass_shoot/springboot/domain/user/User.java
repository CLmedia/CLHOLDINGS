package com.clholdings.pass_shoot.springboot.domain.user;

import com.clholdings.pass_shoot.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    // @Enumerated(EnumType.STRING)
    // JPA 로 베이타베이스로 저장할때 Enum 값을 어떤형태로 저장할지를 결정합니다
    // 기본적으로는 int 로된 숫자가저장됩니다
    // 숫자로저장되면 데이타베이스로 확인할때 그값이 무슨 코드를 의미하는지 알 수가 없습니다
    // 그래서 문자열 (EnumType.STRING) 로 저장될수있도록선언합니다

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
