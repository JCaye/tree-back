package com.desafio.tree.controller;

import com.desafio.tree.dto.ChildrenDTO;
import com.desafio.tree.dto.PostNodeDTO;
import com.desafio.tree.dto.PutNodeDTO;
import com.desafio.tree.model.Node;
import com.desafio.tree.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/node")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:4200"})
public class TreeController {
    @Autowired
    private NodeService service;

    @GetMapping("/proofoflife/{echothis}")
    public String getTest(@PathVariable String echothis){
        return echothis;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long postNode(@RequestBody PostNodeDTO node){
        return service.saveNode(node);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Long putNode(@RequestBody PutNodeDTO node){
        return service.editNode(node);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Node> getTree(){
        return service.fetchFullStructure();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ChildrenDTO> getChildren(@PathVariable Long id){
        return service.fetchChildren(id);
    }
}
