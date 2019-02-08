package com.desafio.tree.service;

import com.desafio.tree.dto.PostNodeDTO;
import com.desafio.tree.dto.PutNodeDTO;
import com.desafio.tree.exceptions.DescendantOfSelfException;
import com.desafio.tree.exceptions.NodeNotFoundException;
import com.desafio.tree.exceptions.ParentNotFoundExcpetion;
import com.desafio.tree.model.Node;
import com.desafio.tree.repository.NodeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeServiceTests {
    @Autowired
    private NodeService service;

    @MockBean
    private NodeRepository repository;

    @Test
    public void saveNode_whenValid_thenSuccess(){
        PostNodeDTO postNodeDTO = new PostNodeDTO();

        Node node = new Node();
        node.setId(4L);
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Node()));
        when(repository.save(Mockito.any(Node.class))).thenReturn(node);

        Long value = service.saveNode(postNodeDTO);
        Assert.assertEquals(value, node.getId());
    }

    @Test
    public void saveNode_whenParentNotFound_thenFail(){
        Long parentId = 4L;

        PostNodeDTO postNodeDTO = new PostNodeDTO();
        postNodeDTO.setParentId(parentId);

        Node node = new Node();
        node.setId(4L);
        when(repository.findById(parentId)).thenReturn(Optional.empty());
        when(repository.save(Mockito.any(Node.class))).thenReturn(node);

        try {
            service.saveNode(postNodeDTO);
            Assert.fail();
        } catch (RuntimeException exception) {
            Assert.assertEquals(exception.getClass(), ParentNotFoundExcpetion.class);
        }
    }

    @Test
    public void editNode_whenValid_thenSuccess(){
        Long nodeId = 3L;
        Long parentId = 6L;

        PutNodeDTO putNodeDTO = new PutNodeDTO();
        putNodeDTO.setId(nodeId);
        putNodeDTO.setParentId(parentId);

        Node node = new Node();
        node.setId(nodeId);

        Node parentNode = new Node();
        parentNode.setId(parentId);
        parentNode.setParent(null);

        when(repository.findById(parentId)).thenReturn(Optional.of(parentNode));
        when(repository.findById(nodeId)).thenReturn(Optional.of(node));
        when(repository.save(Mockito.any(Node.class))).thenReturn(node);

        Assert.assertEquals(nodeId, service.editNode(putNodeDTO));
    }

    @Test
    public void editNode_whenDescendantOfSelf_thenFail(){
        Long nodeId = 3L;
        Long parentId = 6L;

        PutNodeDTO putNodeDTO = new PutNodeDTO();
        putNodeDTO.setId(nodeId);
        putNodeDTO.setParentId(parentId);

        Node node = new Node();
        node.setId(nodeId);

        Node parentNode = new Node();
        parentNode.setId(parentId);
        parentNode.setParent(node);

        when(repository.findById(parentId)).thenReturn(Optional.of(parentNode));
        when(repository.findById(nodeId)).thenReturn(Optional.of(node));

        try {
            service.editNode(putNodeDTO);
            Assert.fail();
        } catch (RuntimeException exception) {
            Assert.assertEquals(exception.getClass(), DescendantOfSelfException.class);
        }
    }

    @Test
    public void editNode_whenNodeNotFound_thenFail(){
        Long nodeId = 3L;
        Long parentId = 6L;

        PutNodeDTO putNodeDTO = new PutNodeDTO();
        putNodeDTO.setId(nodeId);
        putNodeDTO.setParentId(parentId);

        Node parentNode = new Node();
        parentNode.setId(parentId);

        Node node = new Node();
        node.setId(nodeId);
        node.setParent(parentNode);

        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        try {
            service.editNode(putNodeDTO);
            Assert.fail();
        } catch (RuntimeException exception) {
            Assert.assertEquals(exception.getClass(), NodeNotFoundException.class);
        }
    }

    @Test
    public void editNode_whenParentNotFound_thenFail(){
        Long nodeId = 3L;
        Long parentId = 6L;

        PutNodeDTO putNodeDTO = new PutNodeDTO();
        putNodeDTO.setId(nodeId);
        putNodeDTO.setParentId(parentId);

        Node node = new Node();
        node.setId(nodeId);

        Node parentNode = new Node();
        parentNode.setId(parentId);
        parentNode.setParent(node);

        when(repository.findById(parentId)).thenReturn(Optional.empty());
        when(repository.findById(nodeId)).thenReturn(Optional.of(node));
        when(repository.save(Mockito.any(Node.class))).thenReturn(node);

        try {
            service.editNode(putNodeDTO);
            Assert.fail();
        } catch (RuntimeException exception) {
            Assert.assertEquals(exception.getClass(), ParentNotFoundExcpetion.class);
        }
    }

    @Test
    public void fetchChildren_whenValid_thenSuccess(){
        Long nodeId = 5L;

        Node node = new Node();
        node.setId(nodeId);
        node.setChildren(new ArrayList<Node>());

        when(repository.findById(nodeId)).thenReturn(Optional.of(node));

        Assert.assertTrue(service.fetchChildren(nodeId).isEmpty());
    }

    @Test
    public void fetchChildren_whenNodeNotFound_thenFail(){
        Long nodeId = 5L;

        when(repository.findById(nodeId)).thenReturn(Optional.empty());

        try {
            service.fetchChildren(nodeId);
            Assert.fail();
        } catch (RuntimeException exception) {
            Assert.assertEquals(exception.getClass(), NodeNotFoundException.class);
        }
    }

    @Test
    public void fetchTree_whenValid_thenSuccess(){
        List<Node> tree = new ArrayList<>();

        when(repository.findAllByParentId(null)).thenReturn(tree);

        Assert.assertTrue(tree.equals(service.fetchFullStructure()));
    }

}
