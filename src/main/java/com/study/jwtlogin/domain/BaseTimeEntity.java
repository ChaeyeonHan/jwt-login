package com.study.jwtlogin.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// JPA Auditing : Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능(자동으로 시간을 매칭해 db의 테이블에 넣어준다)

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

     @Column(name = "created_at")
     @CreatedDate
     private LocalDateTime  createdAt;  // 엔티티가 생성된 시간


     @Column(name = "update_at")
     @LastModifiedDate
     private LocalDateTime modifiedAt;  // 엔티티 값을 변경한 시간

    // LocalDateTime : 날짜와 시간 정보가 모두 필요할 때 (2022-11-12T12:32:22.000003333)
    // LocalDate : 날짜 정보만 (2022-10-9)

}
