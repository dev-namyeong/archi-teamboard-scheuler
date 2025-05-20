package com.example.demo.department;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class DepartmentInitializer {

    private final DepartmentRepository departmentRepository;

    @PostConstruct
    public void initDepartments(){
        if(departmentRepository.count()==0){
            departmentRepository.save(new Department("건축"));
            departmentRepository.save(new Department("토목"));
            departmentRepository.save(new Department("설비"));
            departmentRepository.save(new Department("안전보건"));
            departmentRepository.save(new Department("공무"));
            System.out.println("== 부서 데이터 저장 완료 ==");

        }else{
            System.out.println("==부서 데이터가 이미 존재합니다.==");
        }
    }
}
