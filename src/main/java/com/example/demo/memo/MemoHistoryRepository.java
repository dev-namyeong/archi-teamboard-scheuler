package com.example.demo.memo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoHistoryRepository extends JpaRepository<MemoHistory, Long> {
    List<MemoHistory> findTop5ByOrderByModifiedAtDesc();
}
