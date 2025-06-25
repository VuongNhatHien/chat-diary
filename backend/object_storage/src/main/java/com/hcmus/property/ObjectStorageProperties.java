package com.hcmus.property;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ObjectStorageProperties {
    private String bucket;
}
