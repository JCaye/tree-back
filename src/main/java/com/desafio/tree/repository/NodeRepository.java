package com.desafio.tree.repository;

import com.desafio.tree.model.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends CrudRepository<Node, Long> {
    List<Node> findAllByParentId(Long id);
}
