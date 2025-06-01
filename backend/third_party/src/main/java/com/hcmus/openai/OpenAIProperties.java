package com.hcmus.openai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAIProperties {
    private String domain;
    private String secretKey;
    private String model;
}
