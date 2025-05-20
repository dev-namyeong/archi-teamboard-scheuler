package com.example.demo.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;
    private final MemoHistoryRepository memoHistoryRepository;

    @GetMapping
    public Map<String, String> getMemo() {
        return Map.of("content", memoService.getMemo().getContent());
    }

    @PostMapping
    public ResponseEntity<Void> updateMemo(@RequestBody MemoDto memoDto) {
        memoService.updateMemo(memoDto.getContent());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public List<MemoHistoryDto> getMemoHistory() {
        return memoHistoryRepository.findTop5ByOrderByModifiedAtDesc()
                .stream()
                .map(history -> new MemoHistoryDto(
                        history.getModifiedAt(),
                        history.getModifiedBy(),
                        history.getContent()))
                .collect(Collectors.toList());
    }
}
