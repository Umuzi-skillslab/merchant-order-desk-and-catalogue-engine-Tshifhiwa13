package com.paynestsystem.app;

import com.paynestsystem.domain.*;
import com.paynestsystem.service.OrderService;

public class PayNestApplication{

    public static void main(String[] args) {
        Product hammer = new Product(1, "Hammer", 149.90);
        Product drill = new Product(2, "Electric Drill", 1999.99);
        Product nails = new Product(3, "Box of Nails", 120.05);
        Customer customer = new Customer(1, "Tshifhiwa Makherana", "fifi@example.co.za");
        
  
        Order order = new Order(35, customer);

        order.addItem(hammer, 1);
        order.addItem(drill, 1);
        order.addItem(nails, 2);
       
         OrderService orderService = new OrderService();
        orderService.printSummary(order);
    }
}

