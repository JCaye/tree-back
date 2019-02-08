package com.desafio.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseNodeDTO implements Serializable {
    private String code;
    private String description;
    private String detail;
    private Long parentId;
}
