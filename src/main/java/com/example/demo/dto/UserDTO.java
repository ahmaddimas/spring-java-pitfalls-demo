package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.entity.User;

public class UserDTO {
    private Long id;
    private String name;
    private int orderCount;
    
    public UserDTO() {
    }
    
    public UserDTO(Long id, String name, int orderCount) {
        this.id = id;
        this.name = name;
        this.orderCount = orderCount;
    }
    
    // Static factory method to convert entity to DTO
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getOrders() != null ? user.getOrders().size() : 0
        );
    }
    
    // Static method to convert a list of entities to DTOs
    public static List<UserDTO> fromEntities(List<User> users) {
        return users.stream()
            .map(UserDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
