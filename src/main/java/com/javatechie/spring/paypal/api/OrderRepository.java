package com.javatechie.spring.paypal.api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javatechie.spring.paypal.api.*;

@Repository
//public interface EmployeeRepository extends JpaRepository<Employee, Long>{

//}
public interface OrderRepository extends JpaRepository<Order,Long> {

}
