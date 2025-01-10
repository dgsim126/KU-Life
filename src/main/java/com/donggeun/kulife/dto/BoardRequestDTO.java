package com.donggeun.kulife.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {
    private String title;
    private String content;
    private int userId; // User의 ID를 전달받음
}
