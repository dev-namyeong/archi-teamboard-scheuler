package com.example.demo.Schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    private Long id; // 일정 ID (FullCalendar에서 id 필요)
    private String title; // 일정 제목
    private String description;
    private String username; // 작성자 이름 (옵션, 필요에 따라)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start; // 시작일시 (문자열 포맷으로 변환됨)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end; // 종료일시 (문자열 포맷으로 변환됨)

    // Schedule 엔티티로부터 바로 DTO로 변환
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getScheduleId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.start = schedule.getStartDate(); // LocalDateTime 그대로 저장
        this.end = schedule.getEndDate(); // LocalDateTime 그대로 저장
        this.username = schedule.getUser().getUsername(); // user 엔티티에 getUsername()이 있어야함
    }
}
