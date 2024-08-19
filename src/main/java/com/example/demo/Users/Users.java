package com.example.demo.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")//, uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}
public class Users {
    @Id
    @GeneratedValue
    Long id;
    @Column(nullable = false)
    String username;
    String name;
    String password;
}
