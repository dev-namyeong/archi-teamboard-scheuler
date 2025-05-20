package com.example.demo.memo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemoHistoryDto {
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private String content;
}
