package com.example.demo.board;

import com.example.demo.department.Department;
import com.example.demo.post.Post;
import com.example.demo.submenus.SubMenus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @OneToOne
    @JoinColumn(name = "submenu_id", unique = true)
    private SubMenus subMenu;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "board_upload_scope",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    @Builder.Default
    private List<Department> uploadScope = new ArrayList<>();

}
