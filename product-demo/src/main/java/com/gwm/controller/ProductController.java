package com.gwm.controller;

import com.gwm.po.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @GetMapping("/{id}")
    public Product detail(@PathVariable Integer id){
        System.out.println("product-demo的detail方法执行中···");
        return new Product(id,"橘子",10,30.1);
    }

}
