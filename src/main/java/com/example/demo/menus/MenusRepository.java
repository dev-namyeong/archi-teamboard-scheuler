package com.example.demo.menus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenusRepository extends JpaRepository<Menus, Long> {

    @Query("SELECT m FROM Menus m LEFT JOIN FETCH m.subMenus")
    List<Menus> findAllWithSubMenus();

    Optional<Menus> findByName(String name);
}
