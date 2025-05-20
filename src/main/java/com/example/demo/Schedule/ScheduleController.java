package com.example.demo.Schedule;

import com.example.demo.menus.MenusService;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.security.SiteUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final MenusService menusService;

    // 일정 관리 페이지를 보여주는 메서드
    @GetMapping("/calendar")
    public String calendarPage(Model model) {
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules();
        model.addAttribute("schedules", schedules);

        // 메뉴 정보 추가
        model.addAttribute("menus", menusService.findAllMenusWithSubMenus());

        return "schedule/calendar";  // 'calendar.html'을 반환
    }

    // 모든 일정 조회
    @GetMapping("/get-events")
    @ResponseBody
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleService.getAllSchedules(); // 모든 일정을 반환
    }


    // 일정 추가
    @PostMapping("/add-event")
    @ResponseBody
    public String addSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto,
                              @AuthenticationPrincipal SiteUserDetails userDetails) {
        try {
            SiteUser user = userDetails.getUser(); // 현재 로그인한 사용자 정보 가져오기
            scheduleService.addSchedule(scheduleRequestDto.toEntity(user));
            return "일정이 추가되었습니다!";
        } catch (Exception e) {
            e.printStackTrace();
            return "일정 추가에 실패했습니다.";
        }
    }

    // 일정 수정
    @PutMapping("/update-event")
    @ResponseBody
    public String updateSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto,
                                 @AuthenticationPrincipal SiteUserDetails userDetails) {
        try {
            SiteUser user = userDetails.getUser();

            // DTO에서 scheduleId가 전달되면 해당 id를 이용하여 수정 처리
            if (scheduleRequestDto.getScheduleId() == null) {
                throw new IllegalArgumentException("일정 ID가 필요합니다.");
            }

            scheduleService.updateSchedule(scheduleRequestDto.toEntity(user));
            return "일정이 수정되었습니다!";
        } catch (Exception e) {
            e.printStackTrace();
            return "일정 수정에 실패했습니다.";
        }
    }

    // 일정 삭제
    @DeleteMapping("/delete-event/{id}")
    @ResponseBody
    public String deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return "일정이 삭제되었습니다!";
        } catch (Exception e) {
            e.printStackTrace();
            return "일정 삭제에 실패했습니다.";
        }
    }
}