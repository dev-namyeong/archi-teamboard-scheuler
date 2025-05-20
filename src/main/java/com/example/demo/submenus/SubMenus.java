package com.example.demo.submenus;

import com.example.demo.board.Board;
import com.example.demo.department.Department;
import com.example.demo.menus.Menus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sub_menus") // DB 테이블 이름과 정확히 맞춤
public class SubMenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 하위 메뉴 ID

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menus menu; // 해당 메뉴

    @Column(nullable = false)
    private String name; // 하위 메뉴 이름

    @Column(name = "is_board", nullable = false)
    private boolean isBoard = false; // 게시판 여부

    @ManyToMany
    @JoinTable(
            name = "submenu_upload_departments",
            joinColumns = @JoinColumn(name = "submenu_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> uploadDepartments = new ArrayList<>(); // 업로드 허용 부서들

    @OneToOne(mappedBy = "subMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Board board;

    @Column(name = "template_path", nullable = true)
    private String templatePath;

    public SubMenus(Menus menu, String name, boolean isBoard, List<Department> uploadDepartments, String templatePath) {
        this.menu = menu;
        this.name = name;
        this.isBoard = isBoard;
        this.uploadDepartments = uploadDepartments;
        this.templatePath = templatePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubMenus)) return false;
        SubMenus that = (SubMenus) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
