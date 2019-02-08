package com.desafio.tree.exceptions;

public class ParentNotFoundExcpetion extends ResourceNotFoundException {
    @Override
    public String getMessage(){
        return "Parent node not found!";
    }
}
