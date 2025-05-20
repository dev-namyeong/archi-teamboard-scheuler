package com.example.demo.Schedule;

import com.example.demo.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId; // 일정 고유 ID, 데이터베이스에서 각 일정을 구별하기 위한 기본 키

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userid")
    private SiteUser user; // 해당 일정을 작성한 사용자. 각 일정은 특정 사용자와 관련됨

    @Column(name = "title", nullable = false)
    private String title; // 일정 제목(예:연차)

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate; // 일정의 시작 시간 (FullCalendar와 연동할 때 필수)

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // 일정의 종료 시간

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt; // 일정이 생성된 시간, 데이터베이스에 언제 일정이 추가되었는지 추적하기 위함

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 일정이 마지막으로 수정된 시간, 데이터베이스에서 일정의 마지막 업데이트 시간을 추적하기 위함

    @Column(name = "description")
    private String description; // 연차 등의 설명

    // 기본 생성자(필수)
    public Schedule() {
    }

    // 생성자 (일정을 생성할 때 필요한 필드만 입력받아 객체를 초기화)
    @Builder
    public Schedule(Long scheduleId, SiteUser user, String title, LocalDateTime startDate, LocalDateTime endDate, String description) {
        this.scheduleId = scheduleId;
        this.user = user; // 일정을 작성한 사용자 지정
        this.title = title; // 일정 제목 지정
        this.startDate = startDate; // 일정 시작일 지정
        this.endDate = endDate; // 일정 종료일 지정\
        this.description = description;
        onCreate();
    }

    // 엔티티 저장될 때 자동으로 생성/수정 시간 입력
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 엔티티 수정될 때 수정 시간 자동 업데이트
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
