package com.cqrs.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String name;
}
