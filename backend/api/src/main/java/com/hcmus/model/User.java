package com.hcmus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User {

    @Id
    private String id = UUID.randomUUID().toString();
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    @JsonIgnore
    @Builder.Default
    private String roles = "ROLE_USER";
}