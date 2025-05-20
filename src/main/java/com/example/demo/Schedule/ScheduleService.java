package com.example.demo.Schedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 일정 추가
    @Transactional
    public void addSchedule(Schedule schedule) {
        try {
            System.out.println("일정 저장 전: " + schedule);  // 일정이 제대로 들어왔는지 확인
            scheduleRepository.save(schedule);  // 일정 저장
            System.out.println("일정 저장됨: " + schedule);  // 저장된 일정 확인
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 일정 수정
    @Transactional
    public void updateSchedule(Schedule schedule) {
        Optional<Schedule> existingSchedule = scheduleRepository.findById(schedule.getScheduleId());
        if (existingSchedule.isPresent()) {
            Schedule currentSchedule = existingSchedule.get();
            currentSchedule.setTitle(schedule.getTitle());
            currentSchedule.setStartDate(schedule.getStartDate());
            currentSchedule.setEndDate(schedule.getEndDate());
            currentSchedule.setDescription(schedule.getDescription());
            scheduleRepository.save(currentSchedule);
        } else {
            throw new IllegalArgumentException("일정을 찾을 수 없습니다.");
        }
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    // 모든 일정 조회
    public List<ScheduleResponseDto> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

}
