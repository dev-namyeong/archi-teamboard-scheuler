package com.example.demo.menus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenusService {

    private final MenusRepository menusRepository;

    public List<Menus> getAllMenus() {
        return menusRepository.findAllWithSubMenus();

    }

    public List<Menus> findAllMenusWithSubMenus() {
        return menusRepository.findAllWithSubMenus();
    }


}

