package com.example.demo.submenus;

import com.example.demo.menus.Menus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubMenusRepository extends JpaRepository<SubMenus, Long> {
    List<SubMenus> findByMenu(Menus menu);

    Optional<SubMenus> findByName(String name);

    Optional<SubMenus> findByMenuIdAndName(Long menuId, String name);

}
