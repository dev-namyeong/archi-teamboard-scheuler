package com.example.demo.memo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    Optional<Memo> findFirstByOrderByIdAsc();
}
