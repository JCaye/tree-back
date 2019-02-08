package com.desafio.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TreeDTO extends PutNodeDTO {
    private List<TreeDTO> nextLevel;
}
