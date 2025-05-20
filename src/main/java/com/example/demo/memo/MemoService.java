package com.example.demo.memo;

import com.example.demo.user.security.SiteUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemoHistoryRepository memoHistoryRepository;

    public Memo getMemo() {
        return memoRepository.findById(1L).orElseGet(() -> {
            Memo memo = new Memo();
            memo.setContent("공용 메모를 여기에 작성해보세요.");
            return memoRepository.save(memo);
        });
    }

    @Transactional
    public void updateMemo(String newContent) {
        Memo memo = memoRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    Memo newMemo = new Memo();
                    newMemo.setContent("");
                    return memoRepository.save(newMemo);
                });

        // 인증된 사용자 이름 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SiteUserDetails userDetails = (SiteUserDetails) authentication.getPrincipal();
        String modifiedBy = userDetails.getUser().getName();

        // 내용이 변경되었을 때만 히스토리 저장
        if (!Objects.equals(memo.getContent(), newContent)) {
            MemoHistory history = new MemoHistory();
            history.setContent(memo.getContent());
            history.setModifiedAt(LocalDateTime.now());
            history.setModifiedBy(modifiedBy);
            memoHistoryRepository.save(history);
        }

        // 현재 메모 업데이트
        memo.setContent(newContent);
        memoRepository.save(memo);
    }

    public List<MemoHistory> getMemoHistories() {
        return memoHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "modifiedAt"));
    }
}
