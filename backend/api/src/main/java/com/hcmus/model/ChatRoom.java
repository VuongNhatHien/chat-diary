package com.hcmus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoom {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private String userId;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Boolean named = false;
}