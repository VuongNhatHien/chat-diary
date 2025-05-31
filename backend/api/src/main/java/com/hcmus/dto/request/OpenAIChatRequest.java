package com.hcmus.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpenAIChatRequest {

    @NotBlank
    private String message;
}
