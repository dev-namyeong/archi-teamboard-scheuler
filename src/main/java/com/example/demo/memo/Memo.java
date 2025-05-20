package com.example.demo.memo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Memo {
    @Id
    private Long id = 1l;

    @Lob
    private String content;
    
}
