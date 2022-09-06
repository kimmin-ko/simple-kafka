package com.example.pipeline;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEventVO {
    private String timestamp;
    private String userAgent;
    private String colorName;
    private String userName;
}
