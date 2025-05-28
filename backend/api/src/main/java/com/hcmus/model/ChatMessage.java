package com.hcmus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmus.constant.GeneralConstant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class ChatMessage {

    String userId;
    Integer roomId;
    String text;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    public boolean belongToChatBot() {
        return userId.equals(GeneralConstant.CHATBOT_USER_ID);
    }

    @JsonIgnore
    public boolean belongToUser() {
        return !belongToChatBot();
    }
}