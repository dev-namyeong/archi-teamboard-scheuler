package com.example.demo.menus;

import com.example.demo.department.Department;
import com.example.demo.submenus.SubMenus;
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
public class Menus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 메뉴 ID

    @Column(nullable = false)
    private String name; // 메뉴이름

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubMenus> subMenus; // 메뉴에 해당하는 하위 메뉴들

    public Menus(String name) {
        this.name = name;
    }
}
