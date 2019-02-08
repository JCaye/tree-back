package com.desafio.tree.service;

import com.desafio.tree.dto.ChildrenDTO;
import com.desafio.tree.dto.PostNodeDTO;
import com.desafio.tree.dto.PutNodeDTO;
import com.desafio.tree.dto.TreeDTO;
import com.desafio.tree.exceptions.DescendantOfSelfException;
import com.desafio.tree.exceptions.NodeNotFoundException;
import com.desafio.tree.exceptions.ParentNotFoundExcpetion;
import com.desafio.tree.model.Node;
import com.desafio.tree.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeService {
    @Autowired
    private NodeRepository repository;

    public Long saveNode(PostNodeDTO node) {
        Node toSave = new Node();
        toSave.setCode(node.getCode());
        toSave.setDescription(node.getDescription());
        toSave.setDetail(node.getDetail());
        toSave.setParent(
            node.getParentId() == null ? null :
            repository.findById(
                    node.getParentId()
            ).orElseThrow(
                    ParentNotFoundExcpetion::new
            ));
        return repository.save(toSave).getId();
    }

    public Long editNode(PutNodeDTO node) {
        if (descendantOfSelf(node.getId(), node.getParentId()))
            throw new DescendantOfSelfException();

        Node toEdit = repository.findById(node.getId()).orElseThrow(NodeNotFoundException::new);
        toEdit.setDetail(node.getDetail());
        toEdit.setCode(node.getCode());
        toEdit.setDescription(node.getDescription());
        toEdit.setParent(node.getParentId() == null ?
                null :
                repository
                        .findById(node.getParentId())
                        .orElseThrow(ParentNotFoundExcpetion::new));
        return repository.save(toEdit).getId();
    }

    public List<Node> fetchFullStructure() {
        return repository.findAllByParentId(null);
    }

    public List<ChildrenDTO> fetchChildren(Long id) {

        return repository
                .findById(id)
                .orElseThrow(NodeNotFoundException::new)
                .getChildren()
                .stream()
                .map(child -> {
                    ChildrenDTO dto = new ChildrenDTO();
                    dto.setHasChildren(child.getChildren() != null && !child.getChildren().isEmpty());
                    dto.setCode(child.getCode());
                    dto.setDescription(child.getDescription());
                    dto.setDetail(child.getDetail());
                    dto.setId(child.getId());
                    dto.setParentId(child.getParent().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private boolean descendantOfSelf(Long id, Long parentId) {
        Node parent = repository.findById(parentId).orElse(null);
        while (parent != null) {
            if (id.equals(parent.getId()))
                return true;
            parent = parent.getParent();
        }
        return false;
    }
}
