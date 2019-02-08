package com.desafio.tree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent")
    @JsonBackReference
    @Nullable
    private Node parent;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "parent")
    @Nullable
    @JsonManagedReference
    private List<Node> children = new ArrayList<>();

    private String code;

    private String description;

    private String detail;
}
