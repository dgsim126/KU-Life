package com.donggeun.kulife.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDTO {
    private int id;
    private String title;
    private String content;
    private String createdAt;
    private boolean isActive;
    private String username; // 작성자 이름
}
