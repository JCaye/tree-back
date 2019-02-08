package com.desafio.tree.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PutNodeDTO extends PostNodeDTO {
    @NotNull
    private Long id;
}
