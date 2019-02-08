package com.desafio.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildrenDTO extends PutNodeDTO {
    private boolean hasChildren;
}
