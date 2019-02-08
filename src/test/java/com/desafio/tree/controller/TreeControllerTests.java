package com.desafio.tree.controller;

import com.desafio.tree.dto.ChildrenDTO;
import com.desafio.tree.dto.PostNodeDTO;
import com.desafio.tree.dto.PutNodeDTO;
import com.desafio.tree.exceptions.DescendantOfSelfException;
import com.desafio.tree.exceptions.NodeNotFoundException;
import com.desafio.tree.exceptions.ParentNotFoundExcpetion;
import com.desafio.tree.model.Node;
import com.desafio.tree.service.NodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TreeControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private NodeService service;

    @Test
    public void createNode_whenValid_thenSuccess() throws Exception {
        Map<String, String > node = makeNode();

       when(service.saveNode(Mockito.any(PostNodeDTO.class))).thenReturn(Mockito.anyLong());

        mvc.perform(
                post("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void createNode_whenParentNotFound_thenFail() throws Exception{
        Map<String, String > node = makeNode();
        when(service.saveNode(Mockito.any(PostNodeDTO.class))).thenThrow(ParentNotFoundExcpetion.class);

        mvc.perform(
                post("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo(parentNotFoundMessage())));
    }

    @Test
    public void editNode_whenValid_thenSuccess() throws Exception{
        Map<String, String> node = makeNode();
        Long id = 1L;
        node.put("id", (id.toString()));

        when(service.editNode(Mockito.any(PutNodeDTO.class))).thenReturn(id);

        mvc.perform(
                put("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(id.toString())));
    }

    @Test
    public void editNode_whenDescendantOfSelf_thenFail() throws Exception{
        Map<String, String> node = makeNode();
        Long id = 1L;
        node.put("id", (id.toString()));

        when(service.editNode(Mockito.any(PutNodeDTO.class))).thenThrow(DescendantOfSelfException.class);

        mvc.perform(
                put("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo(descendantOfSelfMessage())));
    }

    @Test
    public void editNode_whenNodeNotFound_thenFail() throws Exception{
        Map<String, String> node = makeNode();
        Long id = 1L;
        node.put("id", (id.toString()));

        when(service.editNode(Mockito.any(PutNodeDTO.class))).thenThrow(NodeNotFoundException.class);

        mvc.perform(
                put("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo(nodeNotFoundMessage())));
    }

    @Test
    public void editNode_whenParentNodeNotFound_thenFail() throws Exception{
        Map<String, String> node = makeNode();
        Long id = 1L;
        node.put("id", (id.toString()));

        when(service.editNode(Mockito.any(PutNodeDTO.class))).thenThrow(ParentNotFoundExcpetion.class);

        mvc.perform(
                put("/node")
                        .content(mapper.writeValueAsString(node))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo(parentNotFoundMessage())));
    }

    @Test
    public void retrieveChildren_WhenEmpty_thenSuccess() throws Exception {
        List<ChildrenDTO> emptyList = new ArrayList<>();
        when(service.fetchChildren(Mockito.anyLong())).thenReturn(emptyList);

        mvc.perform(get("/node/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(mapper.writeValueAsString(emptyList))));
    }

    @Test
    public void retrieveChildren_WhenPresent_thenSuccess() throws Exception {
        ChildrenDTO child1 = makeChildren();
        ChildrenDTO child2 = makeChildren();
        child2.setId(4L);
        List<ChildrenDTO> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        when(service.fetchChildren(Mockito.anyLong())).thenReturn(children);

        mvc.perform(get("/node/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(mapper.writeValueAsString(children))));
    }

    @Test
    public void retrieveChildren_whenNodeNotFound_thenFail() throws Exception{
        when(service.fetchChildren(Mockito.anyLong())).thenThrow(NodeNotFoundException.class);

        mvc.perform(
                get("/node/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo(nodeNotFoundMessage())));
    }

    @Test
    public void retrieveTree_WhenValid_thenSuccess() throws Exception {
        Node node1 = new Node();
        node1.setId(1L);
        node1.setParent(null);
        node1.setDescription("new description");
        node1.setDetail("new detail");
        node1.setCode("new code");
        node1.setChildren(new ArrayList<>());

        Node node2 = new Node();
        node2.setId(2L);
        node2.setParent(null);
        node2.setDescription("description 2");
        node2.setDetail("detail 2");
        node2.setCode("code 2");
        node2.setChildren(new ArrayList<>());

        List<Node> tree = new ArrayList<>();
        tree.add(node1);
        tree.add(node2);

        when(service.fetchFullStructure()).thenReturn(tree);

        mvc.perform(
                get("/node"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(mapper.writeValueAsString(tree))));
    }

    private Map<String, String> makeNode(){
        Map<String, String> node = new HashMap<>();
        node.put("code", "test_code");
        node.put("description", "test_description");
        node.put("detail", "test_detail");
        node.put("parentId", null);
        return node;
    }

    private ChildrenDTO makeChildren(){
        ChildrenDTO child = new ChildrenDTO();
        child.setParentId(7L);
        child.setId(2L);
        child.setDetail("child detail");
        child.setDescription("child description");
        child.setCode("child code");
        return child;
    }

    private String descendantOfSelfMessage() throws Exception {
        ObjectNode errorMessage = mapper.createObjectNode();
        errorMessage.put("code", 400);
        errorMessage.put("error", "Provided node descends from self!");
        return mapper.writeValueAsString(errorMessage);
    }


    private String nodeNotFoundMessage() throws Exception {
        ObjectNode errorMessage = mapper.createObjectNode();
        errorMessage.put("code", 404);
        errorMessage.put("error", "Node not found!");
        return mapper.writeValueAsString(errorMessage);
    }


    private String parentNotFoundMessage() throws Exception {
        ObjectNode errorMessage = mapper.createObjectNode();
        errorMessage.put("code", 404);
        errorMessage.put("error", "Parent node not found!");
        return mapper.writeValueAsString(errorMessage);
    }
}
