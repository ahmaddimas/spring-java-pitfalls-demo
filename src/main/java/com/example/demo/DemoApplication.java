package com.example.demo;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DemoApplication(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Create users
        User alice = new User();
        alice.setName("Alice");
        
        User bob = new User();
        bob.setName("Bob");
        
        User charlie = new User();
        charlie.setName("Charlie");
        
        userRepository.save(alice);
        userRepository.save(bob);
        userRepository.save(charlie);
        
        // Create orders
        Order order1 = new Order();
        order1.setProduct("Laptop");
        order1.setUser(alice);
        
        Order order2 = new Order();
        order2.setProduct("Phone");
        order2.setUser(alice);
        
        Order order3 = new Order();
        order3.setProduct("Tablet");
        order3.setUser(bob);
        
        Order order4 = new Order();
        order4.setProduct("Watch");
        order4.setUser(bob);
        
        Order order5 = new Order();
        order5.setProduct("Headphones");
        order5.setUser(charlie);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        
        System.out.println("Sample data initialized!");
    }
}