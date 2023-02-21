package com.gwm.controller;

import com.gwm.po.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/{id}")
    public Order detail(@PathVariable Integer id){
        System.out.println("order-demo的detail方法执行中···");
        return new Order(id,"123456",30.1,"xiaoming","橘子");
    }
}
