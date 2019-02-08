package com.desafio.tree.exceptions;

public class NodeNotFoundException extends ResourceNotFoundException {
    @Override
    public String getMessage(){
        return "Node not found!";
    }
}
