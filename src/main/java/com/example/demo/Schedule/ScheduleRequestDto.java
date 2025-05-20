package com.example.demo.Schedule;

import com.example.demo.user.entity.SiteUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
public class ScheduleRequestDto {

    private Long scheduleId;
    private String title;
    private String start;
    private String end;
    private String description;

    // toEntity() 메서드 추가 (DTO -> Entity 변환)
    public Schedule toEntity(SiteUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User 정보가 필요합니다.");
        }

        // 날짜 형식 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // 날짜 처리 - start와 end의 값이 이미 '2025-04-08T09:00:00' 같은 형식이라면
        String startDateTime = this.start; // 이미 T 포함된 값이므로 추가로 'T00:00:00'을 붙일 필요 없음
        String endDateTime = this.end;

        // LocalDateTime으로 변환
        Schedule.ScheduleBuilder builder = Schedule.builder()
                .user(user)
                .title(this.title)
                .startDate(LocalDateTime.parse(startDateTime, formatter))
                .endDate(LocalDateTime.parse(endDateTime, formatter)) // 종료일이 날짜만 포함된 경우 처리
                .description(this.description);

        if (this.scheduleId != null) {
            builder.scheduleId(this.scheduleId);
        }
        return builder.build();
    }
}